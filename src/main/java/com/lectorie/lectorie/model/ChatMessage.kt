package com.lectorie.lectorie.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime

@Entity
data class ChatMessage @JvmOverloads constructor(

    @Id
    @UuidGenerator
    val id: String? = null,
    val chatId: String,
    val senderId: String,
    val recipientId: String,
    val senderName: String,
    val recipientName: String,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    fun changeChatId(chatId: String) = this.copy(chatId = chatId)
    fun changeSenderId(senderId: String) = this.copy(senderId = senderId)
    fun changeRecipientId(recipientId: String) = this.copy(recipientId = recipientId)
    fun changeSenderName(senderName: String) = this.copy(senderName = senderName)
    fun changeRecipientName(recipientName: String) = this.copy(recipientName = recipientName)
    fun changeContent(content: String) = this.copy(content = content)
    fun changeTimestamp(timestamp: LocalDateTime) = this.copy(timestamp = timestamp)

}
