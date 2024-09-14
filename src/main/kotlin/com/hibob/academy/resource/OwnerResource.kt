package com.hibob.academy.resource

import com.hibob.rsetApiDataClasses.Owner
import jakarta.ws.rs.*
import  jakarta.ws.rs.*
import org.springframework.stereotype.Controller
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.MediaType

import jakarta.ws.rs.core.MediaType

@Controller
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)

class OwnerResource {
    private val allOwner: MutableList<Owner> = mutableListOf()

    @GET
    fun getOwner(@PathParam("ownerId") ownerId: Long): Response {
        val owner = owners.find { it.id == ownerId }
        return if (owner != null) {
            Response.ok(owner).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @Path("/{ownerId}")
    fun getOwnerById(@PathParam("ownerId") ownerId: Long): Response {
        val owner = allOwner.find { it.id == ownerId }
        owner?.let {
            return Response.ok(owner).build()
        } ?: return Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
    }

    @POST
    fun postOwner(@PathParam("ownerId") ownerId: Long):Response {
        return Response.ok(ownerId).build()
    }

    fun creatOwner(owner: Owner): Response {
        val newOwnerId = (allOwner.maxByOrNull { o -> o.id }?.id ?: 0) + 1

        allOwner.add(owner.copy(id = newOwnerId))

        return Response.ok().entity(allOwner).build()
    }

    @PUT
    fun putOwner(@PathParam("ownerId") ownerId: Long):Response {
        return Response.ok(ownerId).build()
    }

    @Path("/{ownerId}")
    fun updateOwner(@PathParam("ownerId") ownerId: Long ,updateOwner: Owner): Response {
        val index = allOwner.indexOfFirst { it.id == ownerId }
        if (index >= 0) {
            val ownerToUpdate = allOwner.removeAt(index).copy(id = updateOwner.id)
            return Response.ok(ownerToUpdate).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
        }
    }

    @DELETE
    @Path("/{ownerId}")
    fun deleteOwner(@PathParam("ownerId") ownerId: Long): Response {
        val index = allOwner.indexOfFirst { it.id == ownerId }
        if (index >= 0) {
            allOwner.removeAt(index)
            return Response.status(Response.Status.OK).entity("Owner deleted").build()
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
    }
}
