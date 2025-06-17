package com.tennis.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.Authentication;

@Component
public class JwtTokenProvider {

	private final long jwtExpirationInMs = 86400000;

	private final String jwtSecret = "tajnaLozinka123tajnaLozinka123tajnaLozinka123tajnaLozinka123456789!";

	private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

	public String generateToken(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		List<String> roles = userPrincipal.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority()).toList();

		return Jwts.builder().setSubject(userPrincipal.getUsername()).claim("roles", roles).setIssuedAt(now)
				.setExpiration(expiryDate).signWith(key, SignatureAlgorithm.HS512).compact();
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			System.err.println("❌ Token validation failed: " + e.getMessage());
			return false;
		}
	}
}