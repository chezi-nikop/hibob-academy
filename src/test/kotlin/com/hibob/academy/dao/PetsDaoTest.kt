package com.hibob.academy.dao

import org.junit.jupiter.api.Assertions.*

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@BobDbTest
class PetsDaoTest @Autowired constructor(private val sql: DSLContext)  {

    private val petDao = PetsDao(sql)
    val tablePets = PetsTable.instance
    val companyId = 1L
    val data: LocalDate = LocalDate.now()
    val ownerId = 1L

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(tablePets).where(tablePets.companyId.eq(companyId)).execute()
    }

    @Test
    fun `get all pets by type wen we have pets in the data base`() {
        val petTest= PetDataInsert(null, name = "A", PetType.DOG, companyId)

        val testPetId = petDao.createPet(petTest)

        val allPetsInData = petDao.getAllPetsByType(PetType.DOG, companyId)

        val checkTest = PetData(testPetId, petTest.ownerId, petTest.name, petTest.type, petTest.companyId, allPetsInData[0].dateOfArrival)

        assertTrue(checkTest in allPetsInData)
    }

    @Test
    fun `get all pets by type wen we dont have pets in the data base`() {

        val allPetsInData = petDao.getAllPetsByType(PetType.DOG, companyId)

        assertTrue(allPetsInData.isEmpty())
    }

    @Test
    fun `create a new pet that doesn't exist in the database`() {
        val petTest = PetDataInsert(ownerId, name = "A", PetType.DOG, companyId)

        val testPetId = petDao.createPet(petTest)

        val allPetsInData = petDao.getAllPetsByType(PetType.DOG, companyId)

        val checkTest = PetData(testPetId, petTest.ownerId, petTest.name, petTest.type, petTest.companyId, allPetsInData[0].dateOfArrival)

        assertTrue(checkTest in allPetsInData)
    }

    @Test
    fun `return the pet using its id when it exists in the database`() {
        val petTest = PetDataInsert(ownerId, name = "A", PetType.DOG, companyId)

        val newPetId = petDao.createPet(petTest)

        val returnTest = petDao.getPet(newPetId, companyId)

        val checkTest = PetData(newPetId, petTest.ownerId, petTest.name, petTest.type, petTest.companyId, petDao.getAllPetsByType(PetType.DOG, companyId)[0].dateOfArrival)

        assertEquals(checkTest, returnTest)

    }

    @Test
    fun `return null wen we trying to get pet that does not exist in the database`() {
        val petTest = PetDataInsert(ownerId, name = "A", PetType.DOG, companyId)
        petDao.createPet(petTest)

        val newPetId = -1L

        assertNull(petDao.getPet(newPetId, companyId))
    }

    @Test
    fun `update the pet ownerId wen the ownerId exist`() {
        val petTest = PetDataInsert(ownerId, name = "A", PetType.DOG, companyId)
        val newPetId = petDao.createPet(petTest)

        val newOwnerId = 2L

        val checkTest = petDao.updatePetOwnerId(newPetId, newOwnerId, companyId)

        assertNull(checkTest)
    }

    @Test
    fun `update the pet owner with the owner id wen the ownerId is null`() {
        val petTest = PetDataInsert(null, name = "A", PetType.DOG, companyId)
        val newPetId = petDao.createPet(petTest)

        val newOwnerId = 2L

        petDao.updatePetOwnerId(newPetId, newOwnerId, companyId)

        assertEquals(petDao.getPet(newPetId, companyId)?.ownerId , newOwnerId)
    }
}