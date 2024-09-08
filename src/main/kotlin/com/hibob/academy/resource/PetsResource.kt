package com.hibob.academy.resource

import com.hibob.academy.restApi.Pets
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotAllowedException
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.*


import org.springframework.web.client.RestTemplate

@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
class PetsResource {

    @GET
    fun getPetType(): Response {
        return Response.ok().build()
    }

    @POST
    @Path("/{id}")
    fun postPet(@PathParam("id") id: Long): Response {
        return Response.status(Response.Status.CREATED).build()

    }

    @PUT
    @Path("/{id}")
    fun putPet(@PathParam("id") id: Long): Response {
        return Response.ok("Posted").build()

    }

    @DELETE
    @Path("/{id}")
    fun deletePet(@PathParam("id") id: Long): Response {
        return Response.ok("Deleted").build()
    }
}
