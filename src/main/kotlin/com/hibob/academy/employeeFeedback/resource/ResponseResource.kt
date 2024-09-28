package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.ResponseDataIn
import com.hibob.academy.employeeFeedback.dao.ResponseDataInfo
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller
import com.hibob.academy.employeeFeedback.service.ResponseService
import com.hibob.academy.employeeFeedback.validation.PermissionValidator
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Controller
@Path("/api/response")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ResponseResource(private val responseService: ResponseService) {

    @POST
    fun addResponse(response: ResponseDataInfo, @Context requestContext: ContainerRequestContext): Response {
        val responderPermission = PermissionValidator.Permission.checkPermission(requestContext)
        if (!responderPermission) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource")
        }

        val responderInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)
        val responseId = responseService.addResponse(
            ResponseDataIn(
                responderInfo.id,
                response.feedbackId,
                response.content,
                responderInfo.companyId
            )
        )

        return Response.ok(responseId).build()
    }

    @GET
    @Path("/{feedbackId}")
    fun getResponse(
        @PathParam("feedbackId") feedbackId: Long,
        @Context requestContext: ContainerRequestContext
    ): Response {
        val employeeInfo = PermissionValidator.Permission.getInfoFromCookie(requestContext)
        val response = responseService.getResponse(feedbackId, employeeInfo.companyId)

        val hrOrAdmin = PermissionValidator.Permission.checkPermission(requestContext)
        val feedbackWriter = PermissionValidator.Permission.checkFeedbackWriter(response, requestContext)

        if (!hrOrAdmin && !feedbackWriter) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource")
        }
        return Response.ok(response).build()
    }
}