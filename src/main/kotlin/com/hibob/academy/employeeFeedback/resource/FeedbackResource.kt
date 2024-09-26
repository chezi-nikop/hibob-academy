package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.FeedbackDataIn
import com.hibob.academy.employeeFeedback.dao.FeedbackDataInfo
import com.hibob.academy.employeeFeedback.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import com.hibob.academy.employeeFeedback.validation.PermissionValidator.Companion.permissionValidator

@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService) {

    @POST
    fun addFeedback(feedbackInfo: FeedbackDataInfo, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = permissionValidator.getInfoFromCookie(requestContext)

        val feedBackId = feedbackService.addFeedback(FeedbackDataIn(if (feedbackInfo.isAnonymous) null else employeeInfo.id, feedbackInfo.content, employeeInfo.companyId))

        return Response.ok(feedBackId).build()
    }

    @GET
    @Path("/{feedbackId}")
    fun getFeedbackById(@PathParam("feedbackId") id: Long, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = permissionValidator.getInfoFromCookie(requestContext)

        val feedback = feedbackService.getFeedbackById(id, employeeInfo.companyId)

        val permission = permissionValidator.checkPermission(requestContext)

        if (!permission && feedback.employeeId != employeeInfo.id ) throw BadRequestException("only hr or admin or employee that write the feedback can view info")

        return Response.ok(feedback).build()
    }

    @GET
    @Path("/status/{id}")
    fun getFeedbackStatus(@PathParam("id") feedbackId: Long, @Context requestContext: ContainerRequestContext ): Response {

        val employeeInfo = permissionValidator.getInfoFromCookie(requestContext)

        val status = feedbackService.getFeedbackStatus(feedbackId, employeeInfo.id, employeeInfo.companyId)

        return Response.ok(status).build()
    }
}