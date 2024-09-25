package com.hibob.academy.employeeFeedback.dao

import javassist.NotFoundException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Component

@Component
class EmployeeDao(private val sql: DSLContext) {
    private val employeeTable = EmployeeTable.instance
    private val companyTable = CompanyTable.instance

    private val employeeMapper = RecordMapper<Record, EmployeeOut>
    { record ->
        EmployeeOut(
            record[employeeTable.id],
            record[employeeTable.firstName],
            record[employeeTable.lastName],
            RoleType.stringToEnum(record[employeeTable.rol]),
            record[employeeTable.companyId]
        )
    }

    fun getEmployee(employee: EmployeeIn) : EmployeeOut {
        return sql.select(employeeTable)
            .from(employeeTable)
            .where(employeeTable.firstName.eq(employee.firstName))
            .and(employeeTable.lastName.eq(employee.lastName))
            .and(employeeTable.companyId.eq(employee.companyId))
            .fetchOne(employeeMapper) ?: throw RuntimeException("Failed to fetch employee")
    }

    fun addEmployee(employee: EmployeeOut) {
        sql.insertInto(employeeTable)
            .set(employeeTable.id, employee.id)
            .set(employeeTable.firstName, employee.firstName)
            .set(employeeTable.lastName, employee.lastName)
            .set(employeeTable.rol, RoleType.enumToString(employee.role))
            .set(employeeTable.companyId, employee.companyId)
            .execute()
    }

    fun addCompany(name: String): Long {
        val companyId = sql.insertInto(companyTable)
            .set(companyTable.name, name)
            .returning(companyTable.id)
            .fetchOne()
        return companyId?.get(companyTable.id) ?: throw NotFoundException("Failed to insert company")
    }

    fun deleteTableEmployee(companyId: Long) {
        sql.deleteFrom(employeeTable)
            .where(employeeTable.companyId.eq(companyId))
            .execute()
    }

    fun deleteTableCompany(companyId: Long) {
        sql.deleteFrom(companyTable)
            .where(companyTable.id.eq(companyId))
            .execute()
    }
}