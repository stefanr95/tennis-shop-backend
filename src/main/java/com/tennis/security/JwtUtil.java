package com.tennis.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "+3MCVxbRt4Rqc3XRXbbIadvSSd8U6eHaMaWRkVhfBKjHm2NGvsZuaDoE6+F+0x0kmaYwqPFD0UM7DQ/hD7mLgA==";

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UserDetails userDetails) {
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		return Jwts.builder().setSubject(userDetails.getUsername()).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}

	public String generateToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return generateToken(userDetails);
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public List<String> extractRoles(String token) {
		Claims claims = getClaims(token);
		return claims.get("roles", List.class);
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}