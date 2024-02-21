package com.example.credit.application.system.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val erros: MutableMap<String, String?> = HashMap<String, String?>()
        ex.bindingResult.allErrors.stream().forEach { erro ->
            val fiedlName: String = (erro as FieldError).field
            val messageError: String? = erro.defaultMessage
            erros[fiedlName] = messageError
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = LocalDateTime.now(),
                exception = ex.javaClass.toString(),
                details = erros
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerValidationExceptions(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ExceptionDetails(
            title = "Conflict! Consult the documentation",
            status = HttpStatus.CONFLICT.value(),
            timestamp = LocalDateTime.now(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ))
    }

    @ExceptionHandler(BusinessException::class)
    fun handlerValidationExceptions(ex: BusinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionDetails(
                title = "Conflict! Consult the documentation",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = LocalDateTime.now(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message)
            ))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlerValidationExceptions(ex: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionDetails(
                title = "Conflict! Consult the documentation",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = LocalDateTime.now(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message)
            ))
    }
}