package com.hibob.academy.resource

import com.hibob.academy.dao.PetDataInsert
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.PetsService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller


@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PetsResource(private val petsService: PetsService) {

    @GET
    @Path("/{companyId}")
    fun getAllPetsByType(type: PetType,companyId: Long): Response {
        val allOPets = petsService.getAllPetsByType(type, companyId)
        return Response.ok(allOPets).build()
    }

    @POST
    fun createPet(pet: PetDataInsert): Response {
        petsService.createPet(pet)
        return Response.ok().build()
    }

    @PUT
    @Path("/pets/{petId}/{ownerId}/{companyId}")
    fun updatePetOwnerId(@PathParam("petId") petId: Long, @PathParam("ownerId") ownerId: Long, @PathParam("companyId") companyId: Long): Response {
        val owner = petsService.updatePetOwnerId(petId, ownerId, petId)
        return Response.ok(owner).build()
    }
}