package com.hibob.academy.employeeFeedback.dao

import java.time.LocalDate

data class FeedbackIn(
    val employeeId: Long?,
    val content: String,
    val status: FeedbackStatus = FeedbackStatus.UNREVIEWED,
    val companyId: Long
)

data class FeedbackOut(
    val id: Long,
    val employeeId: Long?,
    val content: String,
    val status: FeedbackStatus,
    val companyId: Long,
    val date: LocalDate
)

enum class FeedbackStatus {
    REVIEWED,
    UNREVIEWED
}