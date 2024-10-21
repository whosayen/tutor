package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {


    List<ChatMessage> findByChatId(String s);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatId = :chatId ORDER BY cm.timestamp DESC")
    List<ChatMessage> findLastMessageByChatId(@Param("chatId") String chatId, Pageable pageable);

}
