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
    private val companyId = 1L
    private val otherCompanyId = 2L

    val petTest1 = PetDataInsert(ownerId = 1L, name = "bi", type = PetType.DOG, companyId)
    val petTest2 = PetDataInsert(null, name = "bi", type = PetType.DOG, companyId)

    @BeforeEach
    @AfterEach
    fun cleanup() {
        petDao.deleteTable(companyId)
        petDao.deleteTable(otherCompanyId)
    }

    @Test
    fun `get all pets by type wen we have pets in the data base`() {
        petDao.createPet(petTest1)
        petDao.createPet(petTest2)

        val returnPets = petDao.getAllPetsByType(PetType.DOG, companyId)

        val pet1 = returnPets[0]
        val pet2 = returnPets[1]

        val expectedResult = listOf(
            PetData(pet1.id, petTest1.ownerId, petTest1.name, petTest1.type, petTest1.companyId, pet1.dateOfArrival),
            PetData(pet2.id, petTest2.ownerId, petTest2.name, petTest2.type, petTest2.companyId, pet2.dateOfArrival)
        )

        assertEquals(expectedResult, returnPets)
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
        val pet =  petDao.getAllPetsByType(PetType.DOG, companyId)[0]

        val newOwnerId = 2L
        petDao.updatePetOwnerId(pet.id, newOwnerId, companyId)

        val expectedResult = listOf(PetData(pet.id, newOwnerId, petTest1.name, petTest1.type, petTest1.companyId, pet.dateOfArrival))

        assertEquals(expectedResult, petDao.getAllPetsByType(PetType.DOG, companyId))
    }

    @Test
    fun `update the pet ownerId wen the ownerId is null`() {
        petDao.createPet(petTest2)
        val pet = petDao.getAllPetsByType(PetType.DOG, companyId)[0]

        val newOwnerId = 2L
        petDao.updatePetOwnerId(pet.id, newOwnerId, companyId)

        val expectedResult = listOf(PetData(pet.id, newOwnerId, petTest1.name, petTest1.type, petTest1.companyId, pet.dateOfArrival))

        val returnedResult = petDao.getAllPetsByType(PetType.DOG, companyId)

        assertEquals(expectedResult, returnedResult)
    }

    @Test
    fun `get pets by ownerId when pets exist in the database`() {
        val ownerId = 1L
        val petTest3 = PetDataInsert(ownerId, name = "ci", type = PetType.CAT, companyId)

        petDao.createPet(petTest1)
        petDao.createPet(petTest3)

        val returnPet1 = petDao.getAllPetsByType(PetType.DOG, ownerId)[0]
        val returnPet3 = petDao.getAllPetsByType(PetType.CAT, ownerId)[0]

        val expectedResult = listOf(
            PetData(returnPet1.id, ownerId, petTest1.name, petTest1.type, petTest1.companyId, returnPet1.dateOfArrival),
            PetData(returnPet3.id, ownerId, petTest3.name, petTest3.type, petTest3.companyId, returnPet3.dateOfArrival)
        )

        val allPets = petDao.getPetsByOwnerId(ownerId, companyId)

        assertEquals(expectedResult, allPets)
    }

    @Test
    fun `get pets by ownerId when no pets exist for the owner`() {
        val ownerId = 1L

        val petsByOwner = petDao.getPetsByOwnerId(ownerId, companyId)

        assertTrue(petsByOwner.isEmpty())
    }

    @Test
    fun `get pets by ownerId when owner has pets in different company`() {
        val ownerId = 1L

        val petTest3 = PetDataInsert(ownerId, name = "ci", type = PetType.CAT, otherCompanyId)

        petDao.createPet(petTest1)
        petDao.createPet(petTest3)

        val returnPet1 = petDao.getAllPetsByType(PetType.DOG, ownerId)[0]

        val expectedResult = listOf(PetData(returnPet1.id, ownerId, petTest1.name, petTest1.type, petTest1.companyId, returnPet1.dateOfArrival))

        val actualResult = petDao.getPetsByOwnerId(ownerId, companyId)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `count pets by type when pets exist in the database`() {
        val petTest3 = PetDataInsert(ownerId = 1L, name = "bi", type = PetType.CAT, companyId)

        petDao.createPet(petTest1)
        petDao.createPet(petTest2)
        petDao.createPet(petTest3)

        val countPetByType = petDao.countPetsByType(companyId)

        assertEquals(2, countPetByType[PetType.DOG])
        assertEquals(1, countPetByType[PetType.CAT])
    }

    @Test
    fun `count pets by type when no pets exist in the database`() {
        val countPetByType = petDao.countPetsByType(companyId)

        assertTrue(countPetByType.isEmpty())
    }

    @Test
    fun `updateOwnerForPets should update owner for all pet IDs`() {
        petDao.createPet(petTest1)
        petDao.createPet(petTest2)

        val pets = petDao.getAllPetsByType(PetType.DOG, companyId)

        val returnPet1 = pets[0]
        val returnPet2 = pets[1]

        val ownerId = 2L
        val allPets = listOf(returnPet1.id, returnPet2.id)

        petDao.updateOwnerForPets(ownerId, allPets, companyId)

        val expectedList = listOf(
            PetData(returnPet1.id, ownerId, petTest1.name, petTest1.type, petTest1.companyId, returnPet1.dateOfArrival),
            PetData(returnPet2.id, ownerId, petTest2.name, petTest2.type, petTest2.companyId, returnPet2.dateOfArrival)
            )

        val returnPets = petDao.getAllPetsByType(PetType.DOG, companyId)

        assertEquals(expectedList, returnPets)
    }

    @Test
    fun `insertMultiplePets should insert all pets into the database`() {
        val pets = listOf(petTest1, petTest2)
        petDao.insertMultiplePets(pets)

        val returnPets = petDao.getAllPetsByType(PetType.DOG, companyId)

        val returnPet1 = returnPets[0]
        val returnPet2 = returnPets[1]

        val expectedList = listOf(
            PetData(returnPet1.id, petTest1.ownerId, petTest1.name, petTest1.type, petTest1.companyId, returnPet1.dateOfArrival),
            PetData(returnPet2.id, petTest2.ownerId, petTest2.name, petTest2.type, petTest2.companyId, returnPet2.dateOfArrival)
        )

        assertEquals(expectedList, returnPets)
    }
}