package com.hibob.academy.employeeFeedback.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.dao.EmployeeIn
import com.hibob.academy.employeeFeedback.dao.EmployeeOut
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.ws.rs.core.NewCookie
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmployeeService(private val employeeDao: EmployeeDao) {

    companion object {
        const val SECRET_KEY = "secjgfthgfth67ythgf657rtythfggfdfgdfasjdhsuytuytuyuttuuuutuytyuajfh3243hgasfssdfdfsdesrytftyr657ret"
    }
    val now = Date()
    fun createJwtToken(employeeOut: EmployeeOut): String {
        return Jwts.builder().setHeaderParam("type", "JWT")
            .claim("id", employeeOut.id)
            .claim("company_id", employeeOut.companyId)
            .claim("role", employeeOut.role)
            .setExpiration(Date(now.time + (60 * 60 * 24)*1000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact()
    }

    fun loginEmployee(employeeIn: EmployeeIn): String {
        val returnEmployee = employeeDao.loginEmployee(employeeIn)

        val newToken = createJwtToken(returnEmployee)

        return newToken
    }

    fun logOutEmployee(): NewCookie {
        return NewCookie()
    }
}