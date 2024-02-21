package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CreditDto
import com.example.credit.application.system.dto.CreditView
import com.example.credit.application.system.dto.CreditViewList
import com.example.credit.application.system.entity.Credit
import com.example.credit.application.system.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto : CreditDto) : ResponseEntity<String> {
        val savedCredit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Credit ${savedCredit.creditCode} - Customer ${savedCredit.customer?.firstName} saved!")
    }

    @GetMapping
    fun findByCustomerId(@RequestParam(value = "customerId") customerId: Long) : ResponseEntity<List<CreditViewList>> {
        val creditViewList : List<CreditViewList> = this.creditService.findAllByCustomer(customerId).stream().map { credit : Credit -> CreditViewList(credit) }.collect(
            Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID) : ResponseEntity<CreditView> {
    val credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}