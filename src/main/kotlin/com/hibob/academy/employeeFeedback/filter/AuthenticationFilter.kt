package com.hibob.academy.employeeFeedback.filter

import com.hibob.academy.employeeFeedback.dao.EmployeeDataForCookie
import com.hibob.academy.employeeFeedback.dao.RoleType
import com.hibob.academy.employeeFeedback.service.EmployeeService.Companion.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.springframework.stereotype.Component

@Component
@Provider
class AuthenticationFilterEmployee : ContainerRequestFilter {
    companion object {
        private const val LOGIN_PATH = "api/employees/login"
        const val COOKIE_NAME = "employee_cookie"
        const val ACTIVE_EMPLOYEE = "active_employee"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        //first login
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        //verify the JWT token from the cookie
        val cookie = requestContext.cookies[COOKIE_NAME]?.value //taking all the cookie from the req
        val claims = verify(cookie, requestContext)

        claims?.let {
            val companyId = (it["company_id"] as? Number)?.toLong()
                ?: throw NotAuthorizedException("Company id not found", Response.Status.UNAUTHORIZED)
            val employeeId = (it["id"] as? Number)?.toLong()
                ?: throw NotAuthorizedException("Employee id not found", Response.Status.UNAUTHORIZED)
            val role = RoleType.valueOf((it["role"] as String).uppercase())
            requestContext.setProperty("active_employee", EmployeeDataForCookie(employeeId, role, companyId))
        }
    }

    //It uses the secret key SECRET_KEY in order to interpret the JWT. It can verify the validity and signature of the JWT.
    private val jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()

    private fun verify(cookie: String?, requestContext: ContainerRequestContext): Claims? {
        if (cookie.isNullOrEmpty()) {
            //abortWith func used in case the authentication token is missing or incorrect please. returns HTTP 401
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing token").build())
            return null
        }
        return try {
            // Parse the token and extract claims
            jwtParser.parseClaimsJws(cookie).body
        } catch (exception: Exception) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build()
            )
            null
        }
    }
}