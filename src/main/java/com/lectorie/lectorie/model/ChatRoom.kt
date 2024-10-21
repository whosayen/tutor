package com.lectorie.lectorie.model

import jakarta.persistence.*

@Entity
data class ChatRoom @JvmOverloads constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val chatId: String,

    @ManyToOne(fetch = FetchType.EAGER)
    val sender: User,
    @ManyToOne(fetch = FetchType.EAGER)
    val recipient: User,
) {
    fun changeChatId(chatId: String) = this.copy(chatId = chatId)
    fun changeSender(sender: User) = this.copy(sender = sender)
    fun changeRecipient(recipient: User) = this.copy(recipient = recipient)

}

