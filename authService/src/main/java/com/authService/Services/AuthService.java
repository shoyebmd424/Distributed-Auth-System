package com.authService.Services;

import com.authService.Entity.AuthRequest;
import com.authService.Entity.AuthResponse;
import com.authService.Entity.UserCredentials;
import com.authService.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    // ⚠️ Key should be at least 256 bits (32 bytes)
    private final String SECRET = "mysecretkey123456mysecretkey123456"; // 32-byte key

    /**
     * Registers a new user by encoding password and saving to DB.
     */
    public void register(AuthRequest request) {
        UserCredentials user = UserCredentials.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    /**
     * Authenticates user and returns JWT token.
     */
    public AuthResponse login(AuthRequest request) {
        UserCredentials user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isMatch) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return new AuthResponse(token);
    }

    /**
     * Returns the signing key from the secret.
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Extracts username (subject) from JWT token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }
}
