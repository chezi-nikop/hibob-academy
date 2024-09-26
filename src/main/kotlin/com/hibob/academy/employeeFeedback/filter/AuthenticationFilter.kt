package com.hibob.academy.employeeFeedback.filter

import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
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
    }

    override fun filter(requestContext: ContainerRequestContext) {
        //first login
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        //verify the JWT token from the cookie
        val cookie = requestContext.cookies[COOKIE_NAME]?.value //taking all the cookie from the req
        val claims = verify(cookie, requestContext)

        claims?.let {
            requestContext.setProperty("companyId", it["companyId"])
            requestContext.setProperty("employeeId", it["employeeId"])
            requestContext.setProperty("role", it["role"])
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