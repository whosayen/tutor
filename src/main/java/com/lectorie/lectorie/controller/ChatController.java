package com.lectorie.lectorie.controller;


import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.ChatMessageRequest;
import com.lectorie.lectorie.model.ChatMessage;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.service.ChatMessageService;
import com.lectorie.lectorie.util.UserUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserUtil userUtil;

    public ChatController(ChatMessageService chatMessageService, SimpMessagingTemplate simpMessagingTemplate, UserUtil userUtil) {
        this.chatMessageService = chatMessageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userUtil = userUtil;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageRequest chatMessageRequest, Principal principal) throws DataFormatException, IOException {
        User senderUser = userUtil.extractUser(principal);
        ChatMessage savedMsg = chatMessageService.save(chatMessageRequest, senderUser);

        simpMessagingTemplate.convertAndSendToUser(savedMsg.getRecipientId(), "/queue/messages",
                new ChatNotification(
                    savedMsg.getId(),
                    UserDto.convert(senderUser),
                    ChatUserSettingsDto.convert(senderUser.getUserSettings()),
                    savedMsg.getContent()
        ));
    }

    @GetMapping("/last/messages")
    public ResponseEntity<List<UserLastMessageDto>> getLastMessages(Principal currentUser) {
        return ResponseEntity.ok(chatMessageService.getUsersAndLastMessages(currentUser));
    }

    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDto>> findChatMessages (@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<ChatMessageDto> findMessage (@PathVariable String id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @PostMapping("/message/start")
    public ResponseEntity<ChatMessageDto> startChatMessage(Principal currentUser, @Valid @RequestBody ChatMessageRequest chatMessageRequest) {
        return ResponseEntity.ok(chatMessageService.startChat(currentUser, chatMessageRequest));
    }
}
