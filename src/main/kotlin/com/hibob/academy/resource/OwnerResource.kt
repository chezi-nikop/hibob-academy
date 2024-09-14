package com.hibob.academy.resource

import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.springframework.stereotype.Controller
import jakarta.ws.rs.core.Response

@Controller
@Path("/api/owners")
//Produces(MediaType.APPLICATION_JSON)

class OwnerResource {

    @Path("/{ownerId}")
    @GET
    fun getOwner(@PathParam("ownerId") ownerId: Long): Response {
        return Response.ok(ownerId).build()
    }

    @Path("/{ownerId}")
    @POST
    fun postOwner(@PathParam("ownerId") ownerId: Long):Response {
        return Response.ok(ownerId).build()
    }

    @Path("/{ownerId}")
    @PUT
    fun putOwner(@PathParam("ownerId") ownerId: Long):Response {
        return Response.ok(ownerId).build()
    }

    @Path("/{ownerId}")
    @DELETE
    fun deleteOwner(@PathParam("ownerId") ownerId: Long): Response {
        return Response.ok(ownerId).build()
    }
}
