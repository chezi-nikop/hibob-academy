package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.ResponseDao
import com.hibob.academy.employeeFeedback.dao.ResponseDataIn
import org.springframework.stereotype.Component
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.ResponseDataOut
import jakarta.ws.rs.BadRequestException

@Component
class ResponseService(private val responseDao: ResponseDao, private val feedbackDao: FeedbackDao) {

    fun addResponse(response: ResponseDataIn): Long {
        checkFeedbackIdEmployeeId(response)
        return responseDao.addResponse(response)
    }

    fun getResponse(feedbackId: Long, companyId: Long): List<ResponseDataOut> {
        if (feedbackId <= 0) throw BadRequestException("ID must be greater than or equal to zero")
        return responseDao.getResponse(feedbackId, companyId)
    }

    fun checkFeedbackIdEmployeeId(response: ResponseDataIn) {
        val feedbackInfo = feedbackDao.getFeedbackById(response.feedbackId, response.companyId)

        if (feedbackInfo.employeeId == null) throw NoSuchMethodError("You can't give a response to anonymous")

        if (feedbackInfo.employeeId == response.responderId) throw NoSuchMethodError("You can't give a response to yourself")
    }
}