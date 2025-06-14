package com.tennis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "tajna_lozinka";

	@SuppressWarnings("deprecation")
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h token
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean validateToken(String token, String userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
}