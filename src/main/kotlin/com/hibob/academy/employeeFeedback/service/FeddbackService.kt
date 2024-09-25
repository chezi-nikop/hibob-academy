package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.*
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Component

@Component
class FeedbackService(private val feedbackDao: FeedbackDao) {

    fun addFeedback(feedback: FeedbackDataIn): Long {
        val minContent = 30
        val maxContent = 225
        if (feedback.content.length < minContent || feedback.content.length > maxContent) throw BadRequestException("Feedback must have between 30 and 225 characters.")

        val checkAdding = feedbackDao.addFeedback(feedback)
        if (checkAdding > 0) return checkAdding
        throw BadRequestException("can't add feedback")
    }

    fun getFeedbackById(id: Long, companyId: Long): FeedbackDataOut {
        validatePositiveIds(id, companyId)
        val returnedFeedback = feedbackDao.getFeedbackById(id, companyId)
        return returnedFeedback
    }

    fun getFeedbackStatus(id: Long, companyId: Long): FeedbackStatus {
        validatePositiveIds(id, companyId)
        val returnedStatus = feedbackDao.getFeedbackStatus(id, companyId)
        return enumValueOf<FeedbackStatus>(returnedStatus)
    }

    private fun validatePositiveIds(id: Long, companyId: Long) {
        if (id <= 0 || companyId <= 0) {
            throw BadRequestException("ID and companyId must be positive.")
        }
    }
}