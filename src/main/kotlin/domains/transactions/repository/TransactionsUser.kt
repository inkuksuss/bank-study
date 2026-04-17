package org.example.domains.transactions.repository

import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionsUser: JpaRepository<User, String> {

    fun findByUlid(ulid: String): User
}