package com.lectorie.lectorie.repository;


import com.lectorie.lectorie.model.ChatRoom;
import com.lectorie.lectorie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.sender.id = :senderId AND c.recipient.id = :recipientId")
    Optional<ChatRoom> findbySenderIdAndRecipientId(@Param("senderId") String senderId, @Param("recipientId") String recipientId);

    @Query("SELECT c FROM ChatRoom c WHERE c.sender.id = :senderId OR c.recipient.id = :recipientId")
    List<ChatRoom> findBySenderIdOrRecipientId(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
}
