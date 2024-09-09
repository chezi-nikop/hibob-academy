package com.hibob.academy.resource

import com.hibob.academy.dao.Example
import com.hibob.academy.service.ExampleService
import com.hibob.academy.service.SessionService
import jakarta.ws.rs.Path
import org.springframework.stereotype.Component
import jakarta.ws.rs.core.Response
import org.springframework.web.bind.annotation.PostMapping
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Controller
import java.awt.PageAttributes
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.Produces
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST


data class User(val email: String, val userName: String, val isAdmin: Boolean)


@Controller
@Path("/users")
class JwtResource(private val sessionService: SessionService) {

    @POST
    @Path("/Login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun addUser(user: User): String {
       // val sessionService = SessionService()

        //val jwtToken = sessionService.createJwtToken(user)

        //return Response.status(Response.Status.CREATED).entity(jwtToken).build()
        return sessionService.createJwtToken(user)
    }
}