package com.hibob.academy.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import com.hibob.academy.resource.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component


@Component
class SessionService {
    companion object {
        val secretKey = "secjgfthgfth67ythgf657rtythfggfdfgdfasjdhsajfh3243hgasfssdfdfsdesrytftyr657ret"
    }
    val now = Date()
    fun createJwtToken(user: User): String {
        return Jwts.builder().setHeaderParam("typ", "JWT")
            .claim("email", user.email)
            .claim("username", user.userName)
            .claim("isAdmin", user.isAdmin)
            .setExpiration(Date(now.time + 60 * 60 * 24))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()),SignatureAlgorithm.HS512)
            .compact()
    }

 /*   fun validateJwtToken(token: String): Jws<Claims>? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJwt(token)
        } catch (e: Exception) {}
        null
    }*/
}