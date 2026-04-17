package org.example.domains.transactions.controller

import org.example.domains.transactions.model.DepositRequest
import org.example.domains.transactions.model.DepositResponse
import org.example.domains.transactions.model.TransferRequest
import org.example.domains.transactions.model.TransferResponse
import org.example.domains.transactions.service.TransactionsService
import org.example.types.dto.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactional")
class TransferController(
    private val transactionsService: TransactionsService
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody(required = true) request: DepositRequest): Response<DepositResponse> {
        return transactionsService.deposit(request.toUlid, request.toAccountId, request.value)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody(required = true) request: TransferRequest): Response<TransferResponse> {
        return transactionsService.transfer(request.fromUlid, request.fromAccountId, request.toAccountId, request.value)
    }
}