package com.dawson.document.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawson.document.entities.JWTDetails;
import com.dawson.document.repositories.JWTRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTTokenService implements Serializable {
	
	@Autowired
	private JWTRepository jwtRepository;

	private static final long serialVersionUID = -5938397934595463019L;
	private static final long TOKEN_VALIDITY = 60L * 60 * 24;
	private static final String SECRET_KEY = "b2136ad4b470b18e5767e2720a0b789022cc240d2a99ed307b687dc31b388ab3afd24b288205eb6e84c6980de31247172ffe6df6e24746ee61671319ff6e88d8";

    // Generate a JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, username);
        jwtRepository.save(
        		JWTDetails.builder()
        		.username(username)
        		.build());
        return token;
    }

    // Validate a JWT token
    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        if(isTokenExpired(token)) {
        	jwtRepository.deleteById(tokenUsername);
        }
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract any claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Create a JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
