package com.tradex.tradex.SecurityConfig;

import com.tradex.tradex.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // We will wire this up next!

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Grab the "Authorization" header from the incoming HTTP request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. If there is no header, or it doesn't start with "Bearer ", let the request pass
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token
        jwt = authHeader.substring(7);

        // Extract the username from the token
        username = jwtService.extractUsername(jwt);

        // If we found a username, and they aren't already authenticated in this cycle
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Fetch the user from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // If the token is cryptographically valid:
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Create the official Spring Security Pass
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // assign it with the details of the request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Hand the VIP pass to the Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request to the Controller!
        filterChain.doFilter(request, response);
    }
}