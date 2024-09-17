package com.hibob.academy.service

import com.hibob.academy.dao.*
import java.lang.IllegalArgumentException

class OwnerService(private val ownerDao: OwnerDao, private val petDao: PetsDao) {

    fun getAllOwnersByCompanyId(companyId: Long): List<OwnerData> {
        return ownerDao.getAllOwners(companyId)
    }

    fun createOwnerIfNotExists(ownerData: OwnerDataInsert, companyId: Long): Long {
        return ownerDao.createOwnerIfNotExists(ownerData) ?: throw IllegalArgumentException("There is already an owner with such data in the system")
    }

    fun getOwnerById(id: Long, companyId: Long): OwnerData {
        return ownerDao.getOwnerById(id, companyId) ?: throw IllegalArgumentException("There is no owner with such id or companyId")
    }

    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData {
        return ownerDao.getOwnerByPetId(petId, companyId) ?: throw IllegalArgumentException("the data you entered is incorrect in relation to the data that exists in the database")
    }
}