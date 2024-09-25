package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.dao.EmployeeDataForLogin
import com.hibob.academy.employeeFeedback.service.EmployeeService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import com.hibob.academy.employeeFeedback.filter.AuthenticationFilterEmployee.Companion.COOKIE_NAME

@Controller
@Path("/api/employees")
class EmployeeResource(private val employeeService: EmployeeService) {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    fun login(employeeLogin: EmployeeDataForLogin): Response {
        val newToken = employeeService.loginEmployee(employeeLogin)

        val newCookie = NewCookie.Builder(COOKIE_NAME).value(newToken).path("/api").build()

        return Response.ok("login successfully").cookie(newCookie).build()
    }

    @GET
    @Path("/logout")
    fun logout(): Response {
        val logoutCookie = employeeService.logOutEmployee()

        return Response.ok("logout successfully").cookie(logoutCookie).build()
    }
}