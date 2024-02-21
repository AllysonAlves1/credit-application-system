package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CustomerDto
import com.example.credit.application.system.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.util.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()
    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should save a customer`() {
        // given
        val customer = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customer)
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType("application/json")
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("42142583040"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Main Street"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a costumer with same CPF and return 409 status`() {
        // given
        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType("application/json")
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.dao.DataIntegrityViolationException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a costumer with firstName empty and return 400 status`() {
        // given
        val customerDto = buildCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType("application/json")
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return 200 status`() {
        // given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${customer.id}")
                .contentType("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("42142583040"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Main Street"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer with invalid id and return 400 status`() {
        // given
        val id = 2L
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$id")
                .contentType("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class com.example.credit.application.system.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete a customer by id and return 204 status`() {
        // given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .contentType("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer by id and return 400 status`() {
        // given
        val id: Long = Random().nextLong()
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$id")
                .contentType("application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class com.example.credit.application.system.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update customer and return 200 status`() {
        // given
        val customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto = buildCustomerDto(firstName = "Jane", lastName = "Doe")
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.patch(URL)
                .param("customerId", customer.id.toString())
                .contentType("application/json")
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jane"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update customer with invalid id and return 400 status`() {
        // given
        val id: Long = Random().nextLong()
        val customerUpdateDto = buildCustomerDto(firstName = "Jane", lastName = "Doe")
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.patch(URL)
                .param("customerId", id.toString())
                .contentType("application/json")
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDto(
        firstName: String = "John",
        lastName: String = "Doe",
        cpf: String = "42142583040",
        email: String = "john@gmail.com",
        password: String = "123456",
        zipCode: String = "123456",
        street: String = "Main Street",
        income: BigDecimal = BigDecimal.valueOf(1000.0)
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        zipCode = zipCode,
        street = street,
        income = income
    )
}