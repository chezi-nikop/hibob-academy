package com.hibob.academy.resource

import com.hibob.rsetApiDataClasses.Pets

import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.*

@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PetsResource {

    @GET
    fun getPetType(): Response {
        return Response.ok("ok").build()
    }

    @POST
    fun postPet( id: Long): Response {
        return Response.ok("posted").build()

    }

    @PUT
    @Path("/{id}")
    fun putPet(@PathParam("id") id: Long): Response {
        return Response.ok("Path").build()

    }

    @DELETE
    @Path("/{id}")
    fun deletePet(@PathParam("id") id: Long): Response {
        return Response.ok("Deleted").build()
    }
}
