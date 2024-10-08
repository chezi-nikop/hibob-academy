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

    private val employeeMapper = RecordMapper<Record, EmployeeDataForCookie>
    { record ->
        EmployeeDataForCookie(
            record[employeeTable.id],
            RoleType.stringToEnum(record[employeeTable.role]),
            record[employeeTable.companyId]
        )
    }

    fun loginEmployee(employee: EmployeeDataForLogin): EmployeeDataForCookie {
        val employeeForCookie = sql.select()
            .from(employeeTable)
            .where(employeeTable.firstName.eq(employee.firstName))
            .and(employeeTable.lastName.eq(employee.lastName))
            .and(employeeTable.companyId.eq(employee.companyId))
            .fetchOne(employeeMapper)
        return employeeForCookie ?: throw NotFoundException("Failed to fetch employee")
    }

    fun addEmployee(employee: EmployeeUserDetails): Long {
        val employeeId = sql.insertInto(employeeTable)
            .set(employeeTable.firstName, employee.firstName)
            .set(employeeTable.lastName, employee.lastName)
            .set(employeeTable.role, RoleType.enumToString(employee.role))
            .set(employeeTable.companyId, employee.companyId)
            .set(employeeTable.department, employee.department)
            .returning(employeeTable.id)
            .fetchOne()
        return employeeId?.get(employeeTable.id) ?: throw NotFoundException("Failed to insert employee")
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
}