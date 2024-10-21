package com.lectorie.lectorie.config;


import com.lectorie.lectorie.exception.custom.TutorRegistrationIncompleteException;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.repository.TokenRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.lectorie.lectorie.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    private static final List<String> REGISTRATION_SKIP_CHECK_URLS = Arrays.asList(
            "/api/v1/tutor/change-subject-taught",
            "/api/v1/tutor/change-spoken-language",
            "/api/v1/tutor/change-hourly-rate",
            "/api/v1/tutor/change-description",
            "/api/v1/tutor/change-video",
            "/api/v1/tutor/change-availability",
            "/api/v1/tutor/check-approved"
    );


    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NotBlank HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.getExpired() && !t.getRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            if (!REGISTRATION_SKIP_CHECK_URLS.contains(request.getRequestURI())) {
                User user = userRepository.findByEmail(userEmail)
                        .orElseThrow();
                Tutor tutor = user.getTutor();

                if (tutor != null && !tutor.isApproved()) {
                    throw new TutorRegistrationIncompleteException("tutor is not finished registration part.", 4011);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

