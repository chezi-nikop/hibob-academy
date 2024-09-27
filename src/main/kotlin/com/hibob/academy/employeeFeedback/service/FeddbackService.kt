package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.NotAuthorizedException
import org.springframework.stereotype.Component

@Component
class FeedbackService(private val feedbackDao: FeedbackDao) {

    fun addFeedback(feedback: FeedbackDataIn): Long {
        val minContent = 30
        if (feedback.content.length < minContent) throw NotAuthorizedException("Feedback must have 30 characters.")

        val checkAdding = feedbackDao.addFeedback(feedback)
        return checkAdding
    }

    fun getFeedbackById(feedbackId: Long, companyId: Long): FeedbackDataOut {
        validatePositiveIds(feedbackId)
        val returnedFeedback = feedbackDao.getFeedbackById(feedbackId, companyId)
        return returnedFeedback
    }

    fun getFeedbackStatus(feedbackId: Long, employeeId: Long, companyId: Long): String {
        validatePositiveIds(feedbackId)
        val returnedStatus = feedbackDao.getFeedbackStatus(feedbackId, employeeId, companyId)
        return returnedStatus
    }

    fun updateFeedbackStatus(updateFeedback: UpdateStatus, companyId: Long): Int {
        validatePositiveIds(updateFeedback.feedbackId)
        val updateStatus = feedbackDao.updateFeedbackStatus(updateFeedback, companyId)
        return updateStatus
    }

    fun getFeedbackByFilter(filter: FeedbackFilter, companyId: Long): List<FeedbackDataOut> {
        return feedbackDao.getFeedbackByFilter(filter, companyId)
    }

    private fun validatePositiveIds(feedbackId: Long) {
        if (feedbackId <= 0) {
            throw NotAuthorizedException("ID must be positive.")
        }
    }
}