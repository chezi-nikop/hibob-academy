package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val ownerDao = OwnerDao(sql)
    val tableOwner = OwnerTable.instance
    val companyId = 1L
    val name = "chezi"

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(tableOwner).where(tableOwner.companyId.eq(companyId)).execute()
    }

    @Test
    fun `get all owners wen we got owners in the database`() {
        val newOwner = OwnerDataInsert( name, companyId, employeeId = "1")
        ownerDao.createOwnerIfNotExists(newOwner)

        val ownerId = ownerDao.getAllOwners(companyId)[0].ownerId

        val checkOwner = OwnerData(ownerId, name, companyId, employeeId = "1" )

        val allOwners = ownerDao.getAllOwners(companyId)

        assertTrue(checkOwner in allOwners)
        assertTrue(allOwners.size.equals(1))
    }

    @Test
    fun `get all owners wen we dont have owners in the database`() {
        val allOwners = ownerDao.getAllOwners(companyId)

        assertTrue(allOwners.isEmpty())
    }

    @Test
    fun `create a new owner that doesn't exist in the database`() {
        val newOwner = OwnerDataInsert(name, companyId, employeeId = "1")
        ownerDao.createOwnerIfNotExists(newOwner)

        val ownerId = ownerDao.getAllOwners(companyId)[0].ownerId

        val checkOwner = OwnerData(ownerId, name, companyId, employeeId = "1")

        assertTrue(checkOwner.equals(ownerDao.getAllOwners(companyId)[0]))
    }

    @Test
    fun `create a new owner that exist in the database`() {
        val newOwner = OwnerDataInsert(name, companyId, employeeId = "1")

        ownerDao.createOwnerIfNotExists(newOwner)
        ownerDao.createOwnerIfNotExists(newOwner)

        val ownerId = ownerDao.getAllOwners(companyId)[0].ownerId

        val checkOwner = OwnerData(ownerId, name, companyId, employeeId = "1")
        val ownerList = listOf(checkOwner)

        val returnList = ownerDao.getAllOwners(companyId)

        assertTrue(ownerList.containsAll(returnList))
        assertTrue(returnList.size.equals(1))
    }

    @Test
    fun `get owner by id wen we have owner in the database`() {
        val newOwner = OwnerDataInsert(name, companyId, employeeId = "1")

        ownerDao.createOwnerIfNotExists(newOwner)

        val ownerId = ownerDao.getAllOwners(companyId)[0].ownerId

        val checkOwner = OwnerData(ownerId, name, companyId, employeeId = "1")
        val returnOwner = ownerDao.getOwnerById(ownerId, companyId)

        assertTrue(checkOwner.equals(returnOwner))
    }

    @Test
    fun `trying to get owner by id wen we dont have this owner in the database`() {
        val newOwner = OwnerDataInsert(name, companyId, employeeId = "1")
        ownerDao.createOwnerIfNotExists(newOwner)

        val newOwnerId = -1L

        assertEquals(null, ownerDao.getOwnerById(newOwnerId, companyId))
    }

    @Test
    fun `get owner info by PetId wen the pet has owner in the dataBase`() {
        val ownerTest = OwnerDataInsert(name = "chezi", companyId, employeeId = "1")
        ownerDao.createOwnerIfNotExists(ownerTest)

        val ownerId = ownerDao.getAllOwners(companyId)[0].ownerId

        val checkOwner = OwnerData(ownerId, name, companyId, employeeId = "1")

        val petDao = PetsDao(sql)
        val petTest = PetDataInsert(ownerId , name = "A", PetType.DOG , companyId)
        val newPetId = petDao.createPet(petTest)

        val checkOwnerTest = ownerDao.getOwnerByPetId(newPetId, companyId)

        assertTrue(checkOwner.equals(checkOwnerTest))
    }

    @Test
    fun `try to get owner by petId when no pet exists in the database`() {
        val ownerFromPetId = ownerDao.getOwnerByPetId(petId = 1L, companyId)

        assertNull(ownerFromPetId)
    }

    @Test
    fun `try to get owner by petId when no owner exists in the database`() {
        val petDao = PetsDao(sql)
        val petTest = PetDataInsert(null , name = "A", PetType.DOG , companyId)
        val newPetId = petDao.createPet(petTest)

        val ownerFromPetId = ownerDao.getOwnerByPetId(newPetId, companyId)

        assertNull(ownerFromPetId)
    }
}