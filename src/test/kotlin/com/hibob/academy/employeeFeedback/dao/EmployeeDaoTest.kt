package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.random.Random

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)
    private val companyId1 = Random.nextLong()

    @Test
    fun `should throw exception when employee does not exist`() {
        val nonExistentEmployee = EmployeeDataForLogin(
            firstName = "",
            lastName = "",
            companyId = companyId1
        )

        val exception = assertThrows<RuntimeException> {
            employeeDao.loginEmployee(nonExistentEmployee)
        }
        assertEquals("Failed to fetch employee", exception.message)
    }

    @Test
    fun `should return employee when exists`() {
        val employee = EmployeeUserDetails(
            firstName = "chezi",
            lastName = "nikop",
            role = RoleType.ADMIN,
            companyId = companyId1,
        )

        employeeDao.addEmployee(employee)

        val returnEmployee =
            employeeDao.loginEmployee(EmployeeDataForLogin(employee.firstName, employee.lastName, employee.companyId))

        assertEquals(employee, returnEmployee)
    }

    @BeforeEach
    @AfterEach
    fun clearTable() {
        employeeDao.deleteTableEmployee(companyId1)
    }
}