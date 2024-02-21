package com.example.credit.application.system.service

import com.example.credit.application.system.entity.Credit
import java.util.UUID

interface ICreditService {
    fun save(credit: Credit): Credit
    fun findAllByCustomer(customerId: Long): List<Credit>
    fun findByCreditCode(customerId: Long, creditCode: UUID): Credit
}