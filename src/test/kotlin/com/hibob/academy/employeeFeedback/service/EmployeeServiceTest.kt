package com.hibob.academy.employeeFeedback.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import com.hibob.academy.employeeFeedback.dao.*
import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.Assertions.*


class EmployeeServiceTest {
    private val employeeDao = mock<EmployeeDao>()
    private val employeeService = EmployeeService(employeeDao)

    @Test
    fun `getEmployee should return a JWT token when employee exists`() {
        val employeeIn = EmployeeIn(firstName = "chezi", lastName = "nikop", companyId = 1L)
        val employeeOut = EmployeeOut(id = 1L, employeeIn.firstName,employeeIn.lastName, role = RoleType.ADMIN, employeeIn.companyId)

        whenever(employeeDao.getEmployee(employeeIn)).thenReturn(employeeOut)

        val returnEmployee = employeeService.getEmployee(employeeIn)

        val parsedClaims = Jwts.parser()
            .setSigningKey(EmployeeService.SECRET_KEY)
            .parseClaimsJws(returnEmployee)
            .body

        assertEquals(employeeOut.id, parsedClaims["id"])
        assertEquals(employeeOut.companyId, parsedClaims["company_id"])
        assertEquals(employeeOut.role, parsedClaims["role"])
    }
}