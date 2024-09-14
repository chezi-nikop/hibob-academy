package com.hibob.academy.resource

import com.hibob.rsetApiDataClasses.Pet

import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.*
import java.time.LocalDate

@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

class PetsResource {

    private val allPets: MutableList<Pet> = mutableListOf()

    @GET
    @Path("/{petId}")
    fun getPetById(@PathParam("petId") petId: Long): Response {
        val pet = allPets.find { p -> p.id == petId }
        pet?.let {
            return Response.ok(pet).build()
        } ?: return Response.status(Response.Status.NOT_FOUND).entity("Pet not found").build()
    }

    @POST
    fun postPet(pet: Pet): Response {
        val newPetId = (allPets.maxByOrNull { p -> p.id }?.id ?: 0) + 1

        allPets.add(pet.copy(id = newPetId, dateOfArrival = LocalDate.now()))

        return Response.status(Response.Status.CREATED).entity(Response.Status.CREATED).build()
    }

    @PUT
    @Path("/{petId}")
    fun putPet(@PathParam("petId") petId: Long ,updatePet: Pet): Response {
        val index = allPets.indexOfFirst { p -> p.id == petId }
        if (index >= 0) {
            val petToUpdate = allPets.removeAt(index).copy(id =updatePet.id, dateOfArrival = LocalDate.now())
            return Response.ok(petToUpdate).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Pet not found").build()
        }
    }

    @DELETE
    @Path("/{PetId}")
    fun deletePet(@PathParam("PetId") petId: Long): Response {
        val index = allPets.indexOfFirst { p -> p.id == petId }
        if (index >= 0) {
            allPets.removeAt(index)
            return Response.status(Response.Status.OK).entity("Pet deleted").build()
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Pet not found").build()
    }
}
