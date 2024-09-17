package com.hibob.academy.dao

import org.junit.jupiter.api.Assertions.*

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class PetsDaoTest @Autowired constructor(private val sql: DSLContext)  {

    private val petDao = PetsDao(sql)
    val tablePets = PetsTable.instance
    private val companyId = 1L

    val petTest1 = PetDataInsert(ownerId = 1L, name = "bi", type = PetType.DOG, companyId)
    val petTest2 = PetDataInsert(null, name = "bi", type = PetType.DOG, companyId)


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(tablePets).where(tablePets.companyId.eq(companyId)).execute()
    }

    @Test
    fun `get all pets by type wen we have pets in the data base`() {
        petDao.createPet(petTest1)
        val date = petDao.getAllPetsByType(PetType.DOG, companyId)[0].dateOfArrival
        val id = petDao.getAllPetsByType(PetType.DOG, companyId)[0].id

        val expectedResult = listOf(PetData(id, petTest1.ownerId, petTest1.name, petTest1.type, petTest1.companyId, date))

        assertEquals(expectedResult, petDao.getAllPetsByType(PetType.DOG, companyId))
    }

    @Test
    fun `get all pets by type wen we dont have pets in the data base`() {
        val expectedResult = emptyList<PetData>()
        val allPetsInData = petDao.getAllPetsByType(PetType.DOG, companyId)

        assertEquals(expectedResult, allPetsInData)
    }


    @Test
    fun `update the pet ownerId wen the ownerId exist`() {
        petDao.createPet(petTest1)
        val date = petDao.getAllPetsByType(PetType.DOG, companyId)[0].dateOfArrival
        val id = petDao.getAllPetsByType(PetType.DOG, companyId)[0].id

        val newOwnerId = 2L
        petDao.updatePetOwnerId(id, newOwnerId, companyId)

        val expectedResult = listOf(PetData(id, newOwnerId, petTest1.name, petTest1.type, petTest1.companyId, date))

        assertEquals(expectedResult, petDao.getAllPetsByType(PetType.DOG, companyId))
    }

    @Test
    fun `update the pet ownerId wen the ownerId is null`() {
        petDao.createPet(petTest2)
        val date = petDao.getAllPetsByType(PetType.DOG, companyId)[0].dateOfArrival
        val id = petDao.getAllPetsByType(PetType.DOG, companyId)[0].id

        val newOwnerId = 2L
        petDao.updatePetOwnerId(id, newOwnerId, companyId)

        val expectedResult = listOf(PetData(id, newOwnerId, petTest1.name, petTest1.type, petTest1.companyId, date))

        assertEquals(expectedResult, petDao.getAllPetsByType(PetType.DOG, companyId))
    }
}