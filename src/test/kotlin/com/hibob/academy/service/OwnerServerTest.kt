package com.hibob.academy.service

import com.hibob.academy.dao.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate


class OwnerServerTest {
    private val ownerDao = mock<OwnerDao>()
    private val petDao = mock<PetsDao>()
    private val ownerService = OwnerService(ownerDao, petDao)

    @Test
    fun `getAllOwners should return list of owners`() {
        val companyId = 1L
        val owner1 = OwnerData(id = 1L, name = "chezi", companyId, employeeId = "1")
        val owner2 = OwnerData(id = 1L, name = "chezi", companyId, employeeId = "2")
        val listOfOwner = listOf(owner1, owner2)

        whenever(ownerDao.getAllOwners(companyId)).thenReturn(listOfOwner)

        assertEquals(listOfOwner, ownerService.getAllOwners(companyId))
    }

    @Test
    fun `createOwnerIfNotExists should call DAO to create owner`() {
        val companyId = 1L
        val owner1 = OwnerDataInsert(name = "chezi", companyId, employeeId = "1")

        ownerService.createOwnerIfNotExists(owner1)

        verify(ownerDao).createOwnerIfNotExists(any())
    }

    @Test
    fun `getOwnerByPetId should return owner when exists`() {
        val companyId = 1L
        val petId = 1L
        val pet = PetData(petId, ownerId = 1L, name = "dog", type = PetType.DOG, companyId, dateOfArrival = LocalDate.now())
        val owner1 = OwnerData(id = 1L, name = "chezi", companyId, employeeId = "1")

        whenever(ownerDao.getOwnerByPetId(petId, companyId)).thenReturn(owner1)

        assertEquals(owner1, ownerService.getOwnerByPetId(petId, companyId))
    }

    @Test
    fun `getOwnerByPetId should throw exception when owner not found`() {
        val companyId = 1L
        val petId = 1L

        whenever(ownerDao.getOwnerByPetId(petId, companyId)).thenReturn(null)

        assertEquals("the data you entered is incorrect in relation to the data that exists in the database", ownerService.getOwnerByPetId(petId, companyId))
    }

    @Test
    fun `getOwnerByPetId should throw exception when pet not found`() {
        val companyId = 1L
        val petId = 1L

        whenever(ownerDao.getOwnerByPetId(petId, companyId)).thenReturn(null)

        assertEquals("the data you entered is incorrect in relation to the data that exists in the database", ownerService.getOwnerByPetId(petId, companyId))
    }
}




