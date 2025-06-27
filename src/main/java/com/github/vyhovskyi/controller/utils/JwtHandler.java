package com.github.vyhovskyi.controller.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtHandler {
    private static final String SECRET = "secret";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String create(String username) {
            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                    .sign(ALGORITHM);

    }

    public static String verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT decoded = verifier.verify(token);

            return decoded.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
