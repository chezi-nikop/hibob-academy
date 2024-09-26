package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Test
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.random.Random

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val feedbackDao = FeedbackDao(sql)

    private val companyId = Random.nextLong()
    private val feedbackId = Random.nextLong()
    private val employeeId = Random.nextLong()
    val feedback1 =
        FeedbackDataIn(employeeId = employeeId, content = "Excellent work", companyId)

    @Test
    fun `add feedback and verify insertion`() {
        val feedbackId = feedbackDao.addFeedback(feedback1)
        val returnedFeedback = feedbackDao.getFeedbackById(feedbackId, companyId)

        val expectedResult = FeedbackDataOut(
            feedbackId,
            feedback1.employeeId,
            feedback1.content,
            FeedbackStatus.UNREVIEWED,
            companyId,
            returnedFeedback.date
        )

        assertEquals(expectedResult, returnedFeedback)
    }

    @Test
    fun `get feedback throws exception when no feedback exists`() {

        val exception = assertThrows<NotFoundException> {
            feedbackDao.getFeedbackById(feedbackId, companyId)
        }

        assertEquals("Failed to fetch feedback", exception.message)
    }

    @Test
    fun `get feedback status when feedback exists`() {
        val feedbackId1 = feedbackDao.addFeedback(feedback1)

        val status = feedbackDao.getFeedbackStatus(feedbackId1, employeeId, companyId)

        assertNotNull(status)
    }

    @Test
    fun `get feedback status throws exception when feedback does not exist`() {
        val exception = assertThrows<NotFoundException> {
            feedbackDao.getFeedbackStatus(feedbackId, employeeId, companyId)
        }
        assertEquals("feedbackId does not exist in the system", exception.message)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        feedbackDao.deleteTable(companyId)
    }
}