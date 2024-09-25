package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.junit.jupiter.api.assertThrows
class FeedbackServiceTest {
    private val feedbackDao = mock<FeedbackDao>()
    private val feedbackService = FeedbackService(feedbackDao)


    @Test
    fun `addFeedback should throw BadRequestException when content length is less than 30`() {
        val feedback = FeedbackDataIn(employeeId = 1L, content = "Too short", )
        assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback)
        }
    }
}