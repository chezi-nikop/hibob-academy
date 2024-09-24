package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.FeedbackIn
import com.hibob.academy.employeeFeedback.dao.FeedbackOut
import com.hibob.academy.employeeFeedback.dao.FeedbackStatus
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Component

@Component
class FeedbackService(private val feedbackDao: FeedbackDao) {

    fun addFeedback(feedback: FeedbackIn): Long {
        val minContent = 30
        val maxContent = 225
        if (feedback.content.length < minContent || feedback.content.length > maxContent) throw BadRequestException("Feedback must have at least 30 characters or less than 225")

        val checkAdding = feedbackDao.addFeedback(feedback)
        if (checkAdding > 0) return checkAdding
        throw BadRequestException("can't add feedback")
    }

    fun getFeedbackById(id: Long, companyId: Long): FeedbackOut {
        val returnedFeedback = feedbackDao.getFeedbackById(id, companyId)
        return returnedFeedback
    }

    fun getFeedbackStatus(id: Long, companyId: Long): FeedbackStatus {
        val returnedStatus = feedbackDao.getFeedbackStatus(id, companyId)
        return enumValueOf<FeedbackStatus>(returnedStatus)
    }
}