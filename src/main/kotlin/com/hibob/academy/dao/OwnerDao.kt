package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper

class OwnerDao(private val sql: DSLContext) {

    private val owner = OwnerTable.instance
    val petsTable = PetsTable()

    private val ownerMapper = RecordMapper<Record, OwnerData>
    { record ->
        OwnerData(
            record[owner.ownerId],
            record[owner.name],
            record[owner.companyId],
            record[owner.employeeId]
        )
    }

    fun getAllOwners(companyId: Long): List<OwnerData> {
        return sql.select(owner.ownerId, owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .where(owner.companyId.eq(companyId))
            .fetch(ownerMapper)
    }

    fun createOwnerIfNotExists(newOwnerData: OwnerDataInsert): Long? {
        return  sql.insertInto(owner)
            .set(owner.name, newOwnerData.name)
            .set(owner.companyId, newOwnerData.companyId)
            .set(owner.employeeId, newOwnerData.employeeId)
            .onConflict(owner.companyId, owner.employeeId)
            .doNothing()
            .returning(owner.ownerId)
            .fetchOne()?.let { it[owner.ownerId] }
    }

    fun getOwnerById(id: Long, companyId: Long): OwnerData? {
        return sql.select(owner.ownerId, owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .where(owner.ownerId.equal(id))
            .and(owner.companyId.equal(companyId))
            .fetchOne(ownerMapper)
    }

    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData? {
        return sql.select(owner.ownerId, owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .join(petsTable).on(petsTable.ownerId.eq(owner.ownerId))
            .where(petsTable.petId.eq(petId))
            .and(petsTable.companyId.eq(companyId))
            .fetchOne(ownerMapper)
    }

    fun getOwnerById(id: Long): OwnerData? {
        return sql.select(owner.name, owner.companyId, owner.employeeId)
            .from(owner)
            .where(owner.ownerId.equal(id))
            .fetchOneInto(OwnerData::class.java)
    }
}