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
        val feedback = FeedbackDataIn()
        assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback)
        }
    }

    @Test
    fun `addFeedback should throw BadRequestException when content length is more than 225`() {
        val feedback = FeedbackDataIn(content = "A".repeat(226))
        assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback)
        }
    }

    @Test
    fun `addFeedback should return feedback ID when valid feedback is provided`() {
        val feedback = FeedbackDataIn(content = "This is valid feedback content.")
        `when`(feedbackDao.addFeedback(feedback)).thenReturn(1L)

        val result = feedbackService.addFeedback(feedback)

        assertEquals(1L, result)
        verify(feedbackDao, times(1)).addFeedback(feedback)
    }

    @Test
    fun `addFeedback should throw BadRequestException when feedback cannot be added`() {
        val feedback = FeedbackDataIn(content = "This is valid feedback content.")
        `when`(feedbackDao.addFeedback(feedback)).thenReturn(0L)

        assertThrows<BadRequestException> {
            feedbackService.addFeedback(feedback)
        }
    }

    @Test
    fun `getFeedbackById should throw BadRequestException when ID or companyId is non-positive`() {
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackById(-1, 1)
        }
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackById(1, -1)
        }
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackById(0, 1)
        }
    }

    @Test
    fun `getFeedbackById should return feedback when valid IDs are provided`() {
        val feedbackDataOut = FeedbackDataOut(id = 1L, content = "Feedback content")
        `when`(feedbackDao.getFeedbackById(1L, 1L)).thenReturn(feedbackDataOut)

        val result = feedbackService.getFeedbackById(1L, 1L)

        assertEquals(feedbackDataOut, result)
        verify(feedbackDao, times(1)).getFeedbackById(1L, 1L)
    }
    @Test
    fun `getFeedbackStatus should throw BadRequestException when ID or companyId is non-positive`() {
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackStatus(-1, 1)
        }
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackStatus(1, -1)
        }
        assertThrows<BadRequestException> {
            feedbackService.getFeedbackStatus(0, 1)
        }
    }

    @Test
    fun `getFeedbackStatus should return FeedbackStatus when valid IDs are provided`() {
        `when`(feedbackDao.getFeedbackStatus(1L, 1L)).thenReturn("APPROVED")

        val result = feedbackService.getFeedbackStatus(1L, 1L)

        assertEquals(FeedbackStatus.APPROVED, result)
        verify(feedbackDao, times(1)).getFeedbackStatus(1L, 1L)
    }

    @Test
    fun `getFeedbackStatus should throw BadRequestException when invalid status is returned`() {
        `when`(feedbackDao.getFeedbackStatus(1L, 1L)).thenReturn("INVALID_STATUS")

        assertThrows<BadRequestException> {
            feedbackService.getFeedbackStatus(1L, 1L)
        }
    }
}