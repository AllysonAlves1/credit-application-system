package com.example.credit.application.system.service.impl

import com.example.credit.application.system.entity.Customer
import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) : ICustomerService {
    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(customerId: Long): Customer = this.customerRepository.findById(customerId)
        .orElseThrow {
            throw BusinessException("Id ${customerId} not found")
        }

    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }
}