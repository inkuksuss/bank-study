package org.example.types.entity

import PrimaryKeyEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
class User (
    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    private val ulid: String,

    @Column(name = "username", length = 50, nullable = false, unique = true)
    val username: String,

    @Column(name = "access_token", length = 255)
    val accessToken: String? = null,

    @OneToMany(mappedBy = "user")
    val accounts: List<Account> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
): PrimaryKeyEntity() {
    override fun getId(): String? {
        return ulid
    }
}