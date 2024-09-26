package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.random.Random

class FeedbackServiceTest {
    private val feedbackDao = mock<FeedbackDao>()
    private val feedbackService = FeedbackService(feedbackDao)
    private val employeeId = Random.nextLong(1, Long.MAX_VALUE)
    private val companyId = Random.nextLong(1, Long.MAX_VALUE)
    private val feedbackId = Random.nextLong(1, Long.MAX_VALUE)
    private val content = "chezi nikop"
    private val goodConstant = "my name is chezi nikop im 27 years old and i live in haifa"

    private val feedback1 = FeedbackDataIn(employeeId, content, companyId)
    private val feedback2 = FeedbackDataIn(employeeId, goodConstant, companyId)


    @Test
    fun `addFeedback should throw BadRequestException when content length is less than 30`() {
        val exception = assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback1)
        }
        assertEquals("Feedback must have 30 characters.", exception.message)
    }

    @Test
    fun `addFeedback should return feedback ID when valid feedback is provided`() {
        whenever(feedbackDao.addFeedback(feedback2)).thenReturn(feedbackId)

        val result = feedbackService.addFeedback(feedback2)

        assertEquals(feedbackId, result)
    }

    @Test
    fun `addFeedback should throw BadRequestException when feedback cannot be added`() {
        whenever(feedbackDao.addFeedback(feedback2)).thenReturn(0L)

        val exception = assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback2)
        }

        assertEquals("can't add feedback", exception.message)
    }

    @Test
    fun `getFeedbackById should return feedback when valid IDs are provided`() {
        val expectedReturn =
            FeedbackDataOut(id = 1L, employeeId, content, FeedbackStatus.UNREVIEWED, companyId, date = LocalDate.now())
        whenever(feedbackDao.getFeedbackById(feedbackId, companyId)).thenReturn(expectedReturn)

        val result = feedbackService.getFeedbackById(feedbackId, companyId)

        assertEquals(expectedReturn, result)
    }

    @Test
    fun `getFeedbackStatus should return FeedbackStatus when valid IDs are provided`() {
        whenever(feedbackDao.getFeedbackStatus(feedbackId, employeeId, companyId)).thenReturn("UNREVIEWED")

        val result = feedbackService.getFeedbackStatus(feedbackId, employeeId, companyId)

        assertEquals(FeedbackStatus.UNREVIEWED.name, result)
    }
}