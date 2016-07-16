/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

/**
 * JWT token
 *
 * @author Huxley Goh
 * @version 1.0
 */
public class JWT {

    private final static int timeout = (1800000 * 2) * 24; // 24 hours in milliseconds

    /**
     * Method to create JWT token
     *
     * @param contact_id
     * @param username
     * @param permission
     * @param key String key required to verify JWT, not Base64 Encoded
     * @return String signed token
     */
    protected static String createJWT(String username, String key) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + timeout);
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(username)
                .signWith(signatureAlgorithm, signingKey)
                .setExpiration(exp);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Method to parse JWT token
     *
     * @param jwt String JWT token received from client
     * @param key String key required to verify JWT, not Base64 Encoded
     * @return String array containing contact_id,username,permission in
     * specific order
     */
    protected static String parseJWT(String jwt, String key) {

        try {
            //This line will throw an exception if it is not a signed JWS (as expected)
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwt).getBody();

            return claims.getSubject();

        } catch (JwtException | NullPointerException e) {
            return null;
        }

    }

}
