package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.FeedbackDataIn
import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService) {

    @POST
    fun addFeedback(content: String, isAnonymous: Boolean, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        if (isAnonymous) {
            val feedBackId = feedbackService.addFeedback(FeedbackDataIn(null, content, employeeInfo.companyId))
            return Response.ok(feedBackId).build()
        }
        val feedBackId = feedbackService.addFeedback(FeedbackDataIn(employeeInfo.employeeId, content, employeeInfo.companyId))
        return Response.ok(feedBackId).build()
    }

    @GET
    @Path("/feedbackById/id/{id}")
    fun getFeedbackById(@PathParam("id") id: Long, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        val feedback = feedbackService.getFeedbackById(id, employeeInfo.companyId)

        if(!confirmationRole(employeeInfo.role)) throw BadRequestException("only hr or admin can view info")

        return Response.ok(feedback).build()
    }

    @GET
    @Path("/status/{id}")
    fun getFeedbackStatus(@PathParam("id") id: Long, @Context requestContext: ContainerRequestContext ): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        val status = feedbackService.getFeedbackStatus(id, employeeInfo.employeeId, employeeInfo.companyId)

        return Response.ok(status).build()
    }

    data class CookieData(val companyId: Long, val employeeId: Long, val role: String)

    fun confirmationId(employIdCookie: Long, employeeIdFeedback: Long) : Boolean {
        return employIdCookie == employeeIdFeedback
    }

    fun confirmationRole(roleCookie: String) : Boolean {
        return RoleType.stringToEnum(roleCookie) == RoleType.HR || RoleType.stringToEnum(roleCookie) == RoleType.ADMIN
    }

    fun getInfoFromCookie(requestContext: ContainerRequestContext): CookieData {
        val companyId = requestContext.getProperty("companyId") as Long
        val employeeId = requestContext.getProperty("employeeId") as Long
        val role = requestContext.getProperty("role") as String

        return CookieData(companyId, employeeId, role)
    }
}