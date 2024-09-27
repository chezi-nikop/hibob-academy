package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun `updateFeedbackStatus should return update count when valid IDs are provided`() {
        val updateFeedback = UpdateStatus(feedbackId = feedbackId, status = FeedbackStatus.REVIEWED)
        val expectedUpdateCount = 1
        whenever(feedbackDao.updateFeedbackStatus(updateFeedback, companyId)).thenReturn(expectedUpdateCount)

        val result = feedbackService.updateFeedbackStatus(updateFeedback, companyId)
        assertEquals(expectedUpdateCount, result)
    }

    @Test
    fun `updateFeedbackStatus should throw NotAuthorizedException when feedbackId is not positive`() {
        val invalidFeedback = UpdateStatus(feedbackId = -1L, status = FeedbackStatus.REVIEWED)

        val exception = assertThrows<BadRequestException> {
            feedbackService.updateFeedbackStatus(invalidFeedback, companyId)
        }
        assertEquals("ID must be positive.", exception.message)
    }

    @Test
    fun `getFeedbackByFilter should return list of feedbacks when valid filter is provided`() {
        val filter = FeedbackFilter()
        val expectedFeedbackList = listOf(
            FeedbackDataOut(
                id = Random.nextLong(1, Long.MAX_VALUE),
                employeeId,
                "Feedback 1",
                FeedbackStatus.REVIEWED,
                companyId,
                date = LocalDate.now()
            ),
            FeedbackDataOut(
                id = Random.nextLong(1, Long.MAX_VALUE),
                employeeId,
                "Feedback 2",
                FeedbackStatus.REVIEWED,
                companyId,
                date = LocalDate.now()
            )
        )
        whenever(feedbackDao.getFeedbackByFilter(filter, companyId)).thenReturn(expectedFeedbackList)

        val result = feedbackService.getFeedbackByFilter(filter, companyId)

        assertEquals(expectedFeedbackList, result)
    }

    @Test
    fun `getFeedbackByFilter should return empty list when no feedbacks match the filter`() {

        val filter = FeedbackFilter(department = "null")
        whenever(feedbackDao.getFeedbackByFilter(filter, companyId)).thenReturn(emptyList())

        val result = feedbackService.getFeedbackByFilter(filter, companyId)

        assertTrue(result.isEmpty())
    }
}