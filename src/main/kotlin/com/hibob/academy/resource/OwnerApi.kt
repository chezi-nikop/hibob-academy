package com.hibob.academy.resource

import com.hibob.academy.dao.OwnerDataInsert
import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component

@Component
@Path("/example")
class OwnerResource(private val ownerService: OwnerService) {

    @GET
    @Path("/{companyId}")
    fun getAllOwners(companyId: Long): Response {
        val allOwners = ownerService.getAllOwners(companyId)
        return Response.ok(allOwners).build()
    }

    @POST
    fun creatOwner(owner: OwnerDataInsert) {
        ownerService.createOwnerIfNotExists(owner)
    }

    @GET
    @Path("/pet/{petId}/company/{companyId}")
    fun getOwnerByPetId(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
        val owner = ownerService.getOwnerByPetId(companyId, petId)
        return Response.ok(owner).build()
    }
}
