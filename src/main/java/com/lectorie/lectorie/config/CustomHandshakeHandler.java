package com.lectorie.lectorie.config;

import com.lectorie.lectorie.repository.TokenRepository;
import com.lectorie.lectorie.service.JwtService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public CustomHandshakeHandler(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = extractToken(request);
        if (token == null) {
            return null;
        }

        String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            return null;
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        boolean isTokenValid = tokenRepository.findByToken(token)
                .map(t -> !t.getExpired() && !t.getRevoked())
                .orElse(false);

        if (!jwtService.isTokenValid(token, userDetails) || !isTokenValid) {
            return null;
        }

        // Return the authentication token as Principal
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private String extractToken(ServerHttpRequest request) {
        // Get the URI from the request
        URI uri = request.getURI();
        String query = uri.getQuery();

        // Parse the query string
        if (query != null && query.contains("access_token")) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "access_token".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}
