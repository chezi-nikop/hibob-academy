package com.hibob.academy.employeeFeedback.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import com.hibob.academy.employeeFeedback.dao.*
import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random


class EmployeeServiceTest {
    private val employeeDao = mock<EmployeeDao>()
    private val employeeService = EmployeeService(employeeDao)

    private val companyId = Random.nextLong()
    private val employeeId = Random.nextLong()

    @Test
    fun `loginEmployee should return a JWT token when employee exists`() {
        val employeeIn = EmployeeDataForLogin(firstName = "chezi", lastName = "nikop", companyId = companyId)
        val employeeOut = EmployeeDataForCookie(employeeId, RoleType.ADMIN, companyId)

        whenever(employeeDao.loginEmployee(employeeIn)).thenReturn(employeeOut)

        val returnEmployee = employeeService.loginEmployee(employeeIn)

        val parsedClaims = Jwts.parser()
            .setSigningKey(EmployeeService.SECRET_KEY)
            .parseClaimsJws(returnEmployee)
            .body

        assertEquals(employeeOut.id, parsedClaims["id"])
        assertEquals(employeeOut.companyId, parsedClaims["company_id"])
        assertEquals(employeeOut.role, RoleType.valueOf(parsedClaims["role"] as String))

        verify(employeeDao).loginEmployee(employeeIn)
    }
}