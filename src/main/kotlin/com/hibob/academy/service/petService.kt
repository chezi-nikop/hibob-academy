package com.hibob.academy.service

import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetDataInsert
import com.hibob.academy.dao.PetType
import com.hibob.academy.dao.PetsDao
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Component

@Component
class PetsService(private val petDao: PetsDao) {

    fun getAllPetsByType(type: PetType, companyId: Long): List<PetData> {
        return petDao.getAllPetsByType(type, companyId)
    }

    fun createPet(petData: PetDataInsert) {
        petDao.createPet(petData)
    }

    fun updatePetOwnerId(petId: Long, ownerId: Long, companyId: Long) {
        val checkUpdate = petDao.updatePetOwnerId(petId, ownerId, companyId)
        if (checkUpdate.equals(0)) throw BadRequestException("the information you entered does not match the database")
    }

    fun getPetsByOwnerId(ownerId: Long, companyId: Long): List<PetData> {
        return petDao.getPetsByOwnerId(ownerId, companyId)
    }

    fun countPetsByType(companyId: Long): Map<PetType, Int> {
        return petDao.countPetsByType(companyId)
    }

    fun updateOwnerForPets(ownerId: Long, petIds: List<Long>, companyId: Long) {
        if (petIds.isEmpty()) throw BadRequestException("petIds cannot be empty")
        val checkUpDate = petDao.updateOwnerForPets(ownerId, petIds, companyId)
        if (checkUpDate.equals(0)) throw BadRequestException("No pets were updated, please check the provided ownerId and petIds")
    }

    fun insertMultiplePets(pets: List<PetDataInsert>) {
        if (pets.isEmpty()) throw BadRequestException("pets cannot be empty")
        val checkUpDate = petDao.insertMultiplePets(pets)
        if (checkUpDate.equals(0)) throw BadRequestException("No pets were inserted, please check the provided data")
    }

    fun updateOwnerForPets(ownerId: Long, petIds: List<Long>) {
        if (petIds.isEmpty()) throw IllegalArgumentException("petIds cannot be empty")
        val checkUpDate = petDao.updateOwnerForPets(ownerId, petIds)
        if (checkUpDate.equals(0)) throw IllegalArgumentException("No pets were updated, please check the provided ownerId and petIds")
    }

    fun insertMultiplePets(pets: List<PetDataInsert>) {
        if (pets.isEmpty()) throw IllegalArgumentException("pets cannot be empty")
        val checkUpDate = petDao.insertMultiplePets(pets)
        if (checkUpDate.equals(0)) throw IllegalArgumentException("No pets were inserted, please check the provided data")
    }
}