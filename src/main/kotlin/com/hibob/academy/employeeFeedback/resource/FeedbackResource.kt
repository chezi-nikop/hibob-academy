package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.FeedbackDataIn
import com.hibob.academy.employeeFeedback.dao.FeedbackDataInfo
import com.hibob.academy.employeeFeedback.dao.FeedbackFilter
import com.hibob.academy.employeeFeedback.dao.UpdateStatus
import com.hibob.academy.employeeFeedback.service.FeedbackService
import com.hibob.academy.employeeFeedback.validation.PermissionValidator
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.server.ResponseStatusException

@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FeedbackResource(private val feedbackService: FeedbackService) {

    @POST
    fun addFeedback(feedbackInfo: FeedbackDataInfo, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)

        val feedBackId = feedbackService.addFeedback(
            FeedbackDataIn(
                if (feedbackInfo.isAnonymous) null else employeeInfo.id,
                feedbackInfo.content,
                employeeInfo.companyId
            )
        )

        return Response.ok(feedBackId).build()
    }

    @GET
    @Path("/{feedbackId}")
    fun getFeedbackById(@PathParam("feedbackId") id: Long, @Context requestContext: ContainerRequestContext): Response {

        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)

        val feedback = feedbackService.getFeedbackById(id, employeeInfo.companyId)

        val permission = PermissionValidator.Permission.checkPermission(requestContext)

        if (!permission && feedback.employeeId != employeeInfo.id) throw NotAuthorizedException("only hr or admin or employee that write the feedback can view info")

        return Response.ok(feedback).build()
    }

    @GET
    @Path("/status/{id}")
    fun getFeedbackStatus(
        @PathParam("id") feedbackId: Long,
        @Context requestContext: ContainerRequestContext
    ): Response {

        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)

        val status = feedbackService.getFeedbackStatus(feedbackId, employeeInfo.id, employeeInfo.companyId)

        return Response.ok(status).build()
    }

    @PUT
    @Path("/update/status")
    fun updateFeedbackStatus(updateFeedback: UpdateStatus, @Context requestContext: ContainerRequestContext): Response {
        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)
        val hrOrAdmin = PermissionValidator.Permission.checkPermission(requestContext)

        if (!hrOrAdmin) throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "You don't have permission to access this resource"
        )

        val updatedStatus = feedbackService.updateFeedbackStatus(updateFeedback, employeeInfo.companyId)

        return Response.ok(updatedStatus).build()
    }

    @GET
    @Path("/view")
    fun getFeedbackByFilter(filter: FeedbackFilter, @Context requestContext: ContainerRequestContext): Response {
        PermissionValidator.Permission.validFilter(filter)

        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)
        val hrOrAdmin = PermissionValidator.Permission.checkPermission(requestContext)

        if (!hrOrAdmin) throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "You don't have permission to access this resource"
        )

        val feedbacks = feedbackService.getFeedbackByFilter(filter, employeeInfo.companyId)

        return Response.ok(feedbacks).build()
    }
}