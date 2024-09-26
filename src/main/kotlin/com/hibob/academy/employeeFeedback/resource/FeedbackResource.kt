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
import com.hibob.academy.employeeFeedback.validation.PermissionValidator.*

@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService) {

    @POST
    fun addFeedback(feedback: FeedbackData, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        val feedBackId = feedbackService.addFeedback(FeedbackDataIn(if (isAnonymous) null else employeeInfo.id, content, employeeInfo.companyId))
        return Response.ok(feedBackId).build()
    }

    @GET
    @Path("/{feedbackId}")
    fun getFeedbackById(@PathParam("feedbackId") id: Long, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        val feedback = feedbackService.getFeedbackById(id, employeeInfo.companyId)

        if(!confirmRole(employeeInfo.role)) throw BadRequestException("only hr or admin can view info")

        return Response.ok(feedback).build()
    }

    @GET
    @Path("/status/{id}")
    fun getFeedbackStatus(@PathParam("id") feedbackId: Long, @Context requestContext: ContainerRequestContext ): Response {

        val employeeInfo = getInfoFromCookie(requestContext)

        val status = feedbackService.getFeedbackStatus(feedbackId, employeeInfo.id, employeeInfo.companyId)

        return Response.ok(status).build()
    }

    fun confirmRole(roleCookie: RoleType) : Boolean {
        return roleCookie == RoleType.HR || roleCookie == RoleType.ADMIN
    }


}