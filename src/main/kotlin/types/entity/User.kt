package org.example.types.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table
data class User {

    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    val ulid: String,

    @Column(name = "platform", nullable = false, length = 25)
    var platform: String,

    @Column(name = "username", length = 50, nullable = false, unique = true)
    val username: String,

    @Column(name = "access_token", length = 255)
    val accessToken: String? = null,

    @OneToMany(mappedBy = "account")
    val accounts: List<Account> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

//    @Column(name = "is_deleted", nullable = false)
//    val isDeleted: Boolean = false,
//
//    @Column(name = "updated_at", nullable = false)
//    val updatedAt: LocalDateTime = LocalDateTime.now()
}