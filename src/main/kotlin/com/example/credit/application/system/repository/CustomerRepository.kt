package com.example.credit.application.system.repository

import com.example.credit.application.system.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, Long>{
}