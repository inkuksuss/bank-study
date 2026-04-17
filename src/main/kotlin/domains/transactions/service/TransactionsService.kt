package org.example.domains.transactions.service

import org.example.common.cache.RedisClient
import org.example.common.cache.RedisKeyProvider
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.example.domains.transactions.model.DepositResponse
import org.example.domains.transactions.model.TransferResponse
import org.example.domains.transactions.repository.TransactionsAccount
import org.example.domains.transactions.repository.TransactionsUser
import org.example.types.dto.Response
import org.example.types.dto.ResponseProvider
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime


@Service
class TransactionsService(
    private val transactionsUser: TransactionsUser,
    private val transactionsAccount: TransactionsAccount,
    private val redisClient: RedisClient,
    private val transactional: Transactional,
    private val logger: Logger = Logging.getLogger(TransactionsService::class.java)
) {

    fun deposit(userUlid: String, accountID: String, value: BigDecimal): Response<DepositResponse> = Logging.logFor(logger) { it
        it["userUlid"] = userUlid
        it["accountID"]= accountID
        it["value"] = value

        val key = RedisKeyProvider.bankMutexKey(userUlid, accountID)
        redisClient.invokeWithMutex(key) {
            return@invokeWithMutex transactional.run {
                val user = transactionsUser.findByUlid(userUlid)

                val account = transactionsAccount.findByUlidAndUSer(accountID, user)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                account.balance = account.balance.add(value)
                account.updatedAt = LocalDateTime.now()
                transactionsAccount.save(account)

                ResponseProvider.success(DepositResponse(afterBalance = account.balance))
            }
        }
    }

    fun transfer(fromUlid: String, fromAccountID: String, toAccountID: String, value: BigDecimal) = Logging.logFor(logger){ it
        it["fromUlid"] = fromUlid
        it["fromAccountID"]= fromAccountID
        it["toAccountID"] = toAccountID
        it["value"] = value

        val key = RedisKeyProvider.bankMutexKey(fromUlid, fromAccountID)

        redisClient.invokeWithMutex(key) {
            return@invokeWithMutex transactional.run {
                val fromAccount = transactionsAccount.findByUlid(fromAccountID)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                if (fromAccount.user.id != fromUlid) {

                } else if (fromAccount.balance < value) {

                } else if (value <= BigDecimal.ZERO) {

                }

                val toAccount = transactionsAccount.findByUlid(toAccountID)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                fromAccount.balance = fromAccount.balance.subtract(value)
                toAccount.balance = toAccount.balance.add(value)

                transactionsAccount.save(fromAccount)
                transactionsAccount.save(toAccount)

                ResponseProvider.success(TransferResponse(
                    afterFromBalance = fromAccount.balance,
                    afterToBalance = toAccount.balance))
            }
        }


    }
}