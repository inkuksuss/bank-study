package org.example.domains.transactions.repository

import org.example.types.entity.Account
import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface TransactionsAccount: JpaRepository<Account, String> {

    fun findByUlidAndUSer(ulid: String, user: User): Account?

    fun findByUlid(accountUlid: String): Account?
}