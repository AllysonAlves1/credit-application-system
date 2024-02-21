package com.example.credit.application.system.exception

import java.time.LocalDateTime

data class ExceptionDetails(
    val title: String,
    val status: Int,
    val timestamp: LocalDateTime,
    val exception: String,
    val details: MutableMap<String, String?>
) {
}