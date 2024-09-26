package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.ResponseDataInfo
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller
import com.hibob.academy.employeeFeedback.service.ResponseService
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response

@Controller
@Path("/api/response")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ResponseResource(private val responseService: ResponseService) {

    @POST
    fun addResponse(response: ResponseDataInfo, @Context requestContext: ContainerRequestContext) : Response {
        //נוציא את המידע הנדרש
        //נרצה לבדוק האם הוא רשאי להגיב
        //נשלח את הפרמטרים הנדרשים
        return Response.ok().build()
    }

    @GET
    @Path("/response/{feedbackId}/{companyId}")
    fun getResponse(@PathParam("feedbackId") feedbackId: Long, @Context requestContext: ContainerRequestContext): Response {
        //נוציא את המידע הנדרש
        //נשלח את הפרמטרים הנדרשים
        //נבדוק האם הוא זה כתב תגובה או רשאי לקרוא תגובות
        return Response.ok().build()
    }
}