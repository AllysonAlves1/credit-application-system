package com.example.credit.application.system.service

import com.example.credit.application.system.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(customerId: Long): Customer
    fun delete(id: Long)
}