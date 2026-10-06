package com.ecom.security.jwt;

import com.ecom.security.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Slf4j: Generates a 'log' object so we can log warnings/errors to the console
@Slf4j
// @Component: Tells Spring Boot to manage this filter as a Spring Bean
@Component
// @RequiredArgsConstructor: Lombok creates a constructor for all 'final' fields (jwtUtils & customUserDetailsService)
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    // Helper to generate, read, and validate JWT tokens
    private final JwtUtils jwtUtils;

    // Service to load user records from our database by email
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * This method runs automatically on EVERY incoming HTTP request before it reaches your Controllers.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // STEP 1: Extract the raw JWT token string from the request's "Authorization" header
            String jwt = parseJwt(request);

            // STEP 2: Check if token exists AND if its signature + expiry date are valid
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {

                // STEP 3: Read the user's email/username embedded inside the token payload
                String username = jwtUtils.getUsernameFromToken(jwt);

                // STEP 4: Fetch the user and their assigned roles (authorities) from the database
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // STEP 5: Create Spring Security's official "Authenticated Badge" containing the user and roles
                // We pass null for password/credentials because they are already authenticated via JWT
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach request-specific metadata (like client IP address) to the badge
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // STEP 6: Store the badge into Spring's SecurityContext for the duration of this request.
                // Now Spring Security knows: "This request is from a verified user with these roles."
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // If anything fails (e.g., malformed token, database error), log it without crashing the app
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // STEP 7: Forward the request to the next filter in the chain (and eventually your Controller).
        // If there was no token, the request still continues, but as an anonymous/unauthenticated guest.
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to extract the JWT string from the HTTP "Authorization" header.
     * Expected format: "Authorization: Bearer eyJhbGciOi..."
     */
    private String parseJwt(HttpServletRequest request) {
        // Read the "Authorization" header sent by the client (Postman, Frontend, etc.)
        String headerAuth = request.getHeader("Authorization");

        // Verify that the header exists and starts with "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Cut off the "Bearer " prefix (7 characters) and return only the raw token string
            return headerAuth.substring(7);
        }

        // If no Bearer header is present, return null
        return null;
    }
}
