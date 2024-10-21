package com.lectorie.lectorie.service;

import com.lectorie.lectorie.exception.custom.UserNotFoundException;
import com.lectorie.lectorie.model.ChatRoom;
import com.lectorie.lectorie.repository.ChatRoomRepository;
import com.lectorie.lectorie.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository repository;
    private final UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return repository.findbySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChat(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChat(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);
        ChatRoom senderRecipient = new ChatRoom(
                chatId,
                userRepository.findById(senderId).orElseThrow(() -> new UserNotFoundException("sender not found", 999)),
                userRepository.findById(recipientId).orElseThrow(() -> new UserNotFoundException("sender not found", 999))
                );

        ChatRoom recipientSender = new ChatRoom(
                chatId,
                userRepository.findById(recipientId).orElseThrow(() -> new UserNotFoundException("sender not found", 999)),
                userRepository.findById(senderId).orElseThrow(() -> new UserNotFoundException("sender not found", 999))
                );

        repository.save(senderRecipient);
        repository.save(recipientSender);
        return chatId;
    }
}
