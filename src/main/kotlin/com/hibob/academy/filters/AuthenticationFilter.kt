package com.hibob.academy.filters

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts


import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component
import jakarta.ws.rs.ext.Provider


@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext) {

        if (requestContext.uriInfo.path == "jwt/users/login") return //first login

        //verify the JWT token from the cookie
        val cookie = requestContext.cookies //taking all the cookie from the req
        val jwtClaims = verify(cookie["chezi_cookie_name"]?.value)

        if (jwtClaims == null) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build()
            ) //abortWith func used in case the authentication token is missing or incorrect please. returns HTTP 401
        }
    }

    private val jwtParser = Jwts.parser().setSigningKey("secret") //It uses the secret key SECRET_KEY in order to interpret the JWT. It can verify the validity and signature of the JWT.

    //func that receives cookie and checks the integrity of the JWT. If the cookie is valid, it returns the Claims from the JWT. If not, return null.
    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try {
                jwtParser.parseClaimsJws(it) //Try to interpret the cookie, if it is correct, the system pulls out the claims
            } catch (ex: Exception) {
                null
            }
        }
}


