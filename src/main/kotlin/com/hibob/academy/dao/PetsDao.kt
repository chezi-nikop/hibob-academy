package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Component
import org.jooq.impl.DSL

@Component
class PetsDao(private val sql: DSLContext) {

    private val petTable = PetsTable.instance

    private val petMapper = RecordMapper<Record, PetData>
    { record ->
        PetData(
            record[petTable.id],
            record[petTable.ownerId],
            record[petTable.name],
            enumValueOf<PetType>(record[petTable.type]),
            record[petTable.companyId],
            record[petTable.dateOfArrival]
        )
    }

    fun getAllPetsByType(type: PetType, companyId: Long) : List<PetData> {
        return sql.select(petTable.id, petTable.ownerId, petTable.name, petTable.type, petTable.companyId, petTable.dateOfArrival)
            .from(petTable)
            .where(petTable.type.eq(typeEnumToString(type)))
            .and(petTable.companyId.eq(companyId))
            .fetch(petMapper)
    }

    fun createPet(newPetData: PetDataInsert) {
        sql.insertInto(petTable)
            .set(petTable.ownerId, newPetData.ownerId)
            .set(petTable.name, newPetData.name)
            .set(petTable.type, typeEnumToString(newPetData.type))
            .set(petTable.companyId, newPetData.companyId)
            .execute()
    }

    fun updatePetOwnerId(petId: Long, ownerId: Long, companyId: Long) =
        sql.update(petTable)
            .set(petTable.ownerId, ownerId)
            .where(petTable.id.eq(petId))
            .and(petTable.companyId.eq(companyId))
            .execute()

    fun getPetsByOwnerId(ownerId: Long, companyId: Long): List<PetData> =
        sql.select(petTable.id, petTable.ownerId, petTable.name, petTable.type, petTable.companyId, petTable.dateOfArrival)
            .from(petTable)
            .where(petTable.companyId.eq(companyId))
            .and(petTable.ownerId.eq(ownerId))
            .fetch(petMapper)

    fun countPetsByType(): Map<PetType, Int> {
        return sql.select(petTable.type, DSL.count())
            .from(petTable)
            .groupBy(petTable.type)
            .fetch()
            .associate { (type, count) -> enumValueOf<PetType>(type) to count }
    }

    fun updateOwnerForPets(ownerId: Long, petIds: List<Long>) {
        sql.update(petTable)
            .set(petTable.ownerId, ownerId)
            .where(petTable.id.`in`(petIds))
            .execute()
    }

    fun insertMultiplePets(pets: List<PetDataInsert>) {
        val insert = sql.insertInto(petTable)
            .columns(petTable.ownerId, petTable.name, petTable.type, petTable.companyId)
            .values(
                DSL.param(petTable.ownerId),
                DSL.param(petTable.name),
                DSL.param(petTable.type),
                DSL.param(petTable.companyId)
                )

        val batch = sql.batch(insert)

        pets.forEach { batch.bind(it.ownerId, it.name, it.type, it.companyId) }

        batch.execute()
    }
}