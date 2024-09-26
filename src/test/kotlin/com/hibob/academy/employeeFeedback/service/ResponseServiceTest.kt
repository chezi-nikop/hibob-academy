package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.ResponseDao
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import kotlin.random.Random
import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.assertThrows

class ResponseServiceTest {
    private val responseDao = mock<ResponseDao>()
    private val feedbackDao = mock<FeedbackDao>()

    private val responseService = ResponseService(responseDao, feedbackDao)

    private val feedbackId = Random.nextLong(1, Long.MAX_VALUE)
    private val responderId = Random.nextLong(1, Long.MAX_VALUE)
    private val companyId = Random.nextLong(1, Long.MAX_VALUE)
    private val content = "This is a valid response content"
    private val responseId = Random.nextLong(1, Long.MAX_VALUE)

    private val validResponse = ResponseDataIn(
        responderId = responderId,
        feedbackId = feedbackId,
        content = content,
        companyId = companyId
    )

    private val feedbackData = FeedbackDataOut(
        id = feedbackId,
        employeeId = Random.nextLong(1, Long.MAX_VALUE),
        content = "Sample feedback content",
        status = FeedbackStatus.UNREVIEWED,
        companyId = companyId,
        date = LocalDate.now()
    )

    @Test
    fun `addResponse should add response when valid data is provided`() {
        whenever(feedbackDao.getFeedbackById(feedbackId, companyId)).thenReturn(feedbackData)
        whenever(responseDao.addResponse(validResponse)).thenReturn(responseId)

        val result = responseService.addResponse(validResponse)

        assertEquals(responseId, result)
    }

    @Test
    fun `addResponse should throw NoSuchMethodError when responding to anonymous feedback`() {

        val anonymousFeedback = feedbackData.copy(employeeId = null)
        whenever(feedbackDao.getFeedbackById(feedbackId, companyId)).thenReturn(anonymousFeedback)

        val exception = assertThrows<NoSuchMethodError> {
            responseService.addResponse(validResponse)
        }
        assertEquals("You can't give a response to anonymous", exception.message)
    }

    @Test
    fun `addResponse should throw NoSuchMethodError when responder tries to respond to own feedback`() {

        val ownFeedback = feedbackData.copy(employeeId = responderId)
        whenever(feedbackDao.getFeedbackById(feedbackId, companyId)).thenReturn(ownFeedback)

        val exception = assertThrows<NoSuchMethodError> {
            responseService.addResponse(validResponse)
        }
        assertEquals("You can't give a response to yourself", exception.message)
    }

    @Test
    fun `getResponse should return list of responses when valid feedback ID is provided`() {
        // Arrange
        val responseData = listOf(
            ResponseDataOut(
                id = 1L,
                responderId = responderId,
                feedbackId = feedbackId,
                content = content,
                companyId = companyId,
                date = LocalDate.now()
            ),
            ResponseDataOut(
                id = 2L,
                responderId = responderId,
                feedbackId = feedbackId,
                content = content,
                companyId = companyId,
                date = LocalDate.now()
            )
        )
        whenever(responseDao.getResponse(feedbackId, companyId)).thenReturn(responseData)

        val result = responseService.getResponse(feedbackId, companyId)

        assertEquals(responseData, result)
    }

    @Test
    fun `getResponse should throw BadRequestException when feedbackId is less than or equal to zero`() {

        val exception = assertThrows<BadRequestException> {
            responseService.getResponse(0L, companyId)
        }
        assertEquals("ID must be greater than or equal to zero", exception.message)
    }
}