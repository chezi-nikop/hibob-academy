package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record

class PetsDao(private val sql: DSLContext) {

    private val pet = PetsTable.instance

    private val petMapper = RecordMapper<Record, PetData>
    { record ->
        PetData(
            record[pet.petId],
            record[pet.ownerId],
            record[pet.name],
            enumValueOf<PetType>(record[pet.type]),
            record[pet.companyId],
            record[pet.dateOfArrival]
        )
    }

    fun getAllPetsByType(type: PetType, companyId: Long) : List<PetData> {
        return sql.select(pet.ownerId, pet.petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.type.eq(typeEnumToString(type)))
            .and(pet.companyId.eq(companyId))
            .fetch(petMapper)
    }

    fun createPet(newPetData: PetDataInsert) : Long {
        return sql.insertInto(pet)
            .set(pet.ownerId, newPetData.ownerId)
            .set(pet.name, newPetData.name)
            .set(pet.type, typeEnumToString(newPetData.type))
            .set(pet.companyId, newPetData.companyId)
            .returning(pet.petId)
            .fetchOne()!![pet.petId]
    }

    fun getPet(petId: Long, companyId: Long): PetData? {
        return sql.select(pet.ownerId, pet.petId, pet.name, pet.type, pet.companyId, pet.dateOfArrival)
            .from(pet)
            .where(pet.petId.eq(petId))
            .and(pet.companyId.eq(companyId))
            .fetchOne(petMapper)
    }

    fun updatePetOwnerId(petId: Long, ownerId: Long, companyId: Long) : Long? {
        return sql.update(pet)
            .set(pet.ownerId, ownerId)
            .where(pet.petId.eq(petId)
            .and(pet.ownerId.isNull))
            .and(pet.companyId.eq(companyId))
            .returning(pet.petId)
            .fetchOne()?.let { it[pet.petId] }
    }
}