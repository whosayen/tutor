package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.ChatMessageDto;
import com.lectorie.lectorie.dto.request.ChatMessageRequest;
import com.lectorie.lectorie.dto.UserDto;
import com.lectorie.lectorie.exception.custom.ChatRoomNotFoundException;
import com.lectorie.lectorie.exception.custom.MessageException;
import com.lectorie.lectorie.exception.custom.UserNotFoundException;
import com.lectorie.lectorie.model.ChatMessage;
import com.lectorie.lectorie.model.ChatRoom;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.repository.ChatMessageRepository;
import com.lectorie.lectorie.repository.ChatRoomRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.lectorie.lectorie.util.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.lectorie.lectorie.dto.UserLastMessageDto;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository repository, ChatRoomService chatRoomService, UserUtil userUtil, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.repository = repository;
        this.chatRoomService = chatRoomService;
        this.userUtil = userUtil;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatMessage save(ChatMessageRequest chatMessageRequest, User senderUser) {
        var chatId = chatRoomService.getChatRoomId(senderUser.getId(),
                chatMessageRequest.recipientId(),
                true
                ).orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found",3990));

        User recipientUser = userRepository.findById(chatMessageRequest.recipientId())
                .orElseThrow(() -> new UserNotFoundException("User not found", 3990));

        var newChatMessage = new ChatMessage(
                null, // not sure can be null
                chatId,
                senderUser.getId(),
                chatMessageRequest.recipientId(),
                senderUser.getUserSettings().getFirstName() + " " + senderUser.getUserSettings().getLastName(),
                recipientUser.getUserSettings().getFirstName() + " " + recipientUser.getUserSettings().getLastName(),
                chatMessageRequest.content()
        );

        return repository.save(newChatMessage);
    }

    public List<ChatMessageDto> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);

        return chatId
                .map(id -> repository.findByChatId(id).stream()
                        .map(ChatMessageDto::convert)
                        .toList())
                .orElse(new ArrayList<>());
    }


    public ChatMessageDto findById(String id) {
        return repository
                .findById(id)
                .map(ChatMessageDto::convert)
                .orElseThrow(() -> new MessageException("can't find message (" + id + ")",3990));
    }

    public List<UserLastMessageDto> getUsersAndLastMessages(Principal currentUser) {
        User user = userUtil.extractUser(currentUser);
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdOrRecipientId(user.getId(), user.getId());

        Map<String, UserLastMessageDto> userLastMessageMap = new HashMap<>();

        for (ChatRoom chatRoom : chatRooms) {
            User otherUser = chatRoom.getSender().getId().equals(user.getId()) ? chatRoom.getRecipient() : chatRoom.getSender();
            List<ChatMessage> lastMessages = repository.findLastMessageByChatId(chatRoom.getChatId(), PageRequest.of(0, 1));

            if (!lastMessages.isEmpty()) {
                ChatMessage lastMessage = lastMessages.get(0);

                if (!userLastMessageMap.containsKey(otherUser.getId()) ||
                        userLastMessageMap.get(otherUser.getId()).lastMessage().timestamp().isBefore(lastMessage.getTimestamp())) {
                    userLastMessageMap.put(otherUser.getId(), new UserLastMessageDto(UserDto.convert(otherUser), ChatMessageDto.convert(lastMessage)));
                }
            }
        }

        return new ArrayList<>(userLastMessageMap.values());
    }

    public ChatMessageDto startChat(Principal currentUser, ChatMessageRequest chatMessageRequest) {
        User user = userUtil.extractUser(currentUser);

        return ChatMessageDto.convert(save(chatMessageRequest, user));
    }
}
