package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Test
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val feedbackDao = FeedbackDao(sql)
    private val employeeDao = EmployeeDao(sql)

    private val companyId = Random.nextLong()
    private val feedbackId = Random.nextLong()
    private val employeeId = Random.nextLong()
    val feedback1 = FeedbackDataIn(employeeId = employeeId, content = "Excellent work", companyId)

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

    @Test
    fun `update feedback status successfully`() {
        val feedbackId = feedbackDao.addFeedback(feedback1)
        val updateStatus = UpdateStatus(FeedbackStatus.REVIEWED, feedbackId)

        val rowsAffected = feedbackDao.updateFeedbackStatus(updateStatus, companyId)

        assertEquals(1, rowsAffected)
        val updatedFeedback = feedbackDao.getFeedbackById(feedbackId, companyId)
        assertEquals(FeedbackStatus.REVIEWED, updatedFeedback.status)
    }

    @Test
    fun `update feedback status fails when feedback does not exist`() {
        val updateStatus = UpdateStatus(FeedbackStatus.REVIEWED, companyId)

        val rowsAffected = feedbackDao.updateFeedbackStatus(updateStatus, companyId)

        assertEquals(0, rowsAffected)
    }

    @Test
    fun `update feedback status fails when companyId does not match`() {
        val feedbackId = feedbackDao.addFeedback(feedback1)
        val updateStatus = UpdateStatus(FeedbackStatus.REVIEWED, feedbackId)

        val rowsAffected = feedbackDao.updateFeedbackStatus(updateStatus, Random.nextLong())

        val feedback = feedbackDao.getFeedbackById(feedbackId, companyId)

        assertEquals(0, rowsAffected)
        assertEquals(FeedbackStatus.UNREVIEWED, feedback.status)
    }

    @Test
    fun `get feedback by filter with no filters returns all feedbacks`() {
        val feedbackId1 = feedbackDao.addFeedback(feedback1)
        val feedback2 = FeedbackDataIn(employeeId = employeeId, content = "Great job", companyId = companyId)
        val feedbackId2 = feedbackDao.addFeedback(feedback2)

        val expectedFeedback1 = feedbackDao.getFeedbackById(feedbackId1, companyId)
        val expectedFeedback2 = feedbackDao.getFeedbackById(feedbackId2, companyId)

        val expectedFeedback = listOf(expectedFeedback1, expectedFeedback2)

        val returnFeedbacks = feedbackDao.getFeedbackByFilter(FeedbackFilter(), companyId)

        assertEquals(expectedFeedback.toSet(), returnFeedbacks.toSet())
    }

    @Test
    fun `get feedback by department filter returns feedbacks from correct department`() {
        val newEmployee = EmployeeUserDetails("chezi", "nikop", RoleType.ADMIN, companyId, "A" )
        val employeeId =  employeeDao.addEmployee(newEmployee)
        val feedbackId1 = feedbackDao.addFeedback(feedback1.copy(employeeId = employeeId))

        val filter = FeedbackFilter(department = "A")

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(1, feedbacks.size)
        assertEquals(feedbackId1, feedbacks[0].id)
    }

    @Test
    fun `get feedback by date range filter returns feedbacks within range`() {
        val feedbackId1 = feedbackDao.addFeedback(feedback1)

        val expectedFeedback = feedbackDao.getFeedbackById(feedbackId1, companyId)
        val startDate = expectedFeedback.date.minusDays(1)
        val endDate = expectedFeedback.date.plusDays(1)

        val filter = FeedbackFilter(startDate = startDate, endDate = endDate)

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(listOf(expectedFeedback), feedbacks)
    }

    @Test
    fun `get feedback by start date filter returns feedbacks from start date onwards`() {
        val feedbackId1 = feedbackDao.addFeedback(feedback1)

        val expectedFeedback = feedbackDao.getFeedbackById(feedbackId1, companyId)
        val startDate = expectedFeedback.date.minusDays(1)

        val filter = FeedbackFilter(startDate = startDate)

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(listOf(expectedFeedback), feedbacks)
    }

    @Test
    fun `get feedback by end date filter returns feedbacks up to end date`() {
        val feedbackId1 = feedbackDao.addFeedback(feedback1)

        val expectedFeedback = feedbackDao.getFeedbackById(feedbackId1, companyId)
        val endDate = expectedFeedback.date.plusDays(1)

        val filter = FeedbackFilter(endDate = endDate)

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(listOf(expectedFeedback), feedbacks)
    }

    @Test
    fun `get feedback by anonymous filter returns only anonymous feedbacks`() {
        val anonymousFeedback = FeedbackDataIn(employeeId = null, content = "Anonymous feedback", companyId = companyId)
        feedbackDao.addFeedback(anonymousFeedback)
        feedbackDao.addFeedback(feedback1)

        val filter = FeedbackFilter(isAnonymous = true)

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(1, feedbacks.size)
        assertNull(feedbacks[0].employeeId)
    }

    @Test
    fun `get feedback by combined filters returns correct feedback`() {
        val newEmployee1 = EmployeeUserDetails("chezi", "nikop", RoleType.ADMIN, companyId, "A" )
        val employeeId1 =  employeeDao.addEmployee(newEmployee1)
        val feedbackId1 = feedbackDao.addFeedback(feedback1.copy(employeeId = employeeId1))

        val newEmployee2 = EmployeeUserDetails("beni", "lev", RoleType.ADMIN, companyId, "B" )
        employeeDao.addEmployee(newEmployee2)
        feedbackDao.addFeedback(feedback1.copy(employeeId = null))

        val expectedFeedback = feedbackDao.getFeedbackById(feedbackId1, companyId)
        val filter = FeedbackFilter(department = "A", isAnonymous = false)

        val feedbacks = feedbackDao.getFeedbackByFilter(filter, companyId)

        assertEquals(listOf(expectedFeedback), feedbacks)
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        feedbackDao.deleteTable(companyId)
    }
}