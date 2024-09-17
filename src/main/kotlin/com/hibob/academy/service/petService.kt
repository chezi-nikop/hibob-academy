package com.hibob.academy.service

import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetDataInsert
import com.hibob.academy.dao.PetType
import com.hibob.academy.dao.PetsDao

class PetsService(private val petDao: PetsDao) {
    fun getAllPetsByCompanyId(type: PetType, companyId: Long): List<PetData> {
        return petDao.getAllPetsByType(type, companyId)
    }

    fun createPet(pet: PetDataInsert): Long {
        return petDao.createPet(pet)
    }

    fun getPet(petId: Long, companyId: Long): PetData? {
        return petDao.getPet(petId, companyId) ?: throw IllegalArgumentException("The data you entered is incorrect in relation to the data that exists in the database")

    }

    fun updatePetOwnerId(petId: Long, ownerId: Long, companyId: Long) {
        if (petDao.updatePetOwnerId(petId, ownerId, companyId) == null) {
            throw IllegalArgumentException("The data you entered is incorrect in relation to the data that exists in the database")
        }
    }
}