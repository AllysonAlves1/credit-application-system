package com.example.credit.application.system.dto

import com.example.credit.application.system.entity.Credit
import com.example.credit.application.system.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field: NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field: Future val dayFirstOfInstallment: LocalDate,
    val numberOfInstallments: Int,
    @field: NotNull(message = "Invalid input") val customerId: Long
) {
    fun toEntity(): Credit {
        return Credit(
            creditValue = this.creditValue,
            dayFirstInstallment = this.dayFirstOfInstallment,
            numberOfInstallments = this.numberOfInstallments,
            customer = Customer(id = this.customerId)
        )
    }
}
