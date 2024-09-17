package com.hibob.academy.filters

import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component
import jakarta.ws.rs.ext.Provider

@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {

    companion object {
        private const val LOGIN_PATH = "jwt/users/login"
        const val COOKIE_NAME = "chezi_cookie_name"  // Replace with actual cookie name
    }
    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == LOGIN_PATH) return //first login

        //verify the JWT token from the cookie
        val cookie = requestContext.cookies //taking all the cookie from the req
        verify(cookie[COOKIE_NAME]?.value, requestContext)
    }

    private val jwtParser = Jwts.parser().setSigningKey(SECRET_KEY) //It uses the secret key SECRET_KEY in order to interpret the JWT. It can verify the validity and signature of the JWT.

    fun verify(cookie: String?, requestContext: ContainerRequestContext) {
        if (cookie.isNullOrEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing token").build()) //abortWith func used in case the authentication token is missing or incorrect please. returns HTTP 401
            return
        }
        try {
            jwtParser.parseClaimsJws(cookie) //Try to interpret the cookie, if it is correct, the system pulls out the claims
        } catch (exception: Exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build())
        }
    }
}


