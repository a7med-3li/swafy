package com.vamo.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Lazy;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(@Lazy JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            
            // Parse and validate the token using the already-configured Nimbus JwtDecoder
            Jwt jwt = jwtDecoder.decode(token);

            // Extract claims
            String userId = jwt.getSubject(); // Custom user-id stored in subject
            String rolesClaim = jwt.getClaimAsString("roles");
            
            List<SimpleGrantedAuthority> authorities = null;
            if (rolesClaim != null && !rolesClaim.isEmpty()) {
                authorities = Arrays.stream(rolesClaim.split(" "))
                        .map(role -> {
                            if (!role.startsWith("ROLE_")) {
                                return new SimpleGrantedAuthority("ROLE_" + role);
                            }
                            return new SimpleGrantedAuthority(role);
                        })
                        .collect(Collectors.toList());
            }

            // Create custom Authentication object
            JwtAuthentication authentication = new JwtAuthentication(userId, authorities);
            
            // Put additional details like remote address
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Inject into SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Token is invalid; clear context to guarantee no authenticated state
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
