package org.personalfinancetrackertwo.personal_finance_tracker_two.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.io.Decoders.BASE64;

/**
 * JwtUtil is a Utility class responsible for generating Json Web Tokens (JWT) for
 * Authentication and Authorization of a User.  The JWT will be sent with every
 * subsequent http request for authentication and will is stored within the browser
 * until expiration. Methods for generating a JWT, extracting
 * claims, checking token validity, etc are included.
 */
@Component
public class JwtUtil {

    // This is a secret key that is a 32 character or 256 bit key that is pulled from an environment variable
    private static final String JWT_SECRET = System.getenv("JWT_SECRET_KEY");

    /**
     * Responsible for generating the Json Web Token (JWT).
     * @param claims claims added to the JWT
     * @param userDetails contains user information
     * @return newly generated signed Json Web Token
     */
    public String generateJwt(Map<String, Object> claims, UserDetails userDetails) {

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Responsible for generating the Json Web Token (JWT) with default information (no extra claims).
     * @param userDetails contains user information
     * @return newly generated signed Json Web Token
     */
    public String generateJwt(UserDetails userDetails) {
        return generateJwt(new HashMap<>(), userDetails);
    }

    /**
     * Responsible for checking if the Json Web Token (JWT) is still valid.
     * @param token - The users JWT being checked
     * @param userDetails - contains user details to compare to the details within the JWT
     * @return - true (if token is valid), false (if token is invalid)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // extracts the username from the token
        final String username = extractUsername(token);

        // returns boolean of whether username inside the token equals the username inside the user details and if the token isn't expired.
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Responsible for extracting the username from the Json Web Token (JWT).
     * @param token - The token being extracted from.
     * @return - The Username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Responsible for extracting a specific claim
     * @param token - The JWT being extracted from
     * @param claimsResolver - functional interface used to extract specific claims
     * @return - specific claim that returns from the claimsResolver.apply(claims) call.
     * @param <T> - generic of type T, can return any type of claim (String, int, etc).
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Responsible for extracting all claims from a Json Web Token (JWT).
     * @param token - the JWT being extracted from
     * @return - all claims stored within a Claims object.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Responsible for generating the secret key that is used for signing the Json Web Token (JWT).
     * @return
     */
    private SecretKey getSignInKey() {
        // decodes the JWT_SECRET into bytes
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);

        // wraps keyBytes array into a SecretKey object using the HmacSHA256 encryption algorithm
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * Responsible for checking if the Json Web Token (JWT) is expired.
     * @param token - the JWT being checked.
     * @return - true if expired, false if valid
     */
    private boolean isTokenExpired(String token) {
        // checks if tokens expiration is before the current date
        return extractExpiration(token).before(new Date());

    }

    /**
     * Responsible for extracting the expiration date of the Json Web Token (JWT).
     * @param token - the JWT being extracted from
     * @return - Date Object that is the expiration date of the JWT.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
