package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.ResponseDao
import com.hibob.academy.employeeFeedback.dao.ResponseDataIn
import org.springframework.stereotype.Component
import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.dao.ResponseDataOut
import jakarta.ws.rs.BadRequestException

@Component
class ResponseService(private val responseDao: ResponseDao, private val feedbackDao: FeedbackDao) {

    fun addResponse(response: ResponseDataIn, companyId: Long) : Long {
        val check = checkFeedbackIdEmployeeId(response.feedbackId, companyId, response.responseId)
        val id = responseDao.addResponse(response)
        return id
    }

    fun getResponse(id: Long) : List<ResponseDataOut> {
        if (id <= 0) throw BadRequestException("ID must be greater than or equal to zero")
        return responseDao.getResponse(id)
    }

    fun checkFeedbackIdEmployeeId(feedbackId: Long, companyId: Long, responseId: Long) : Boolean {
        val feedbackInfo = feedbackDao.getFeedbackById(feedbackId, companyId)

        if ( feedbackInfo.employeeId == responseId) throw NoSuchMethodError("You can't give a response to yourself")

        return true
    }
}