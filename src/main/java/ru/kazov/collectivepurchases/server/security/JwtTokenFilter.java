package ru.kazov.collectivepurchases.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kazov.collectivepurchases.server.models.dto.ErrorResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            final String jwtToken = authorizationHeader.substring(7);
            JwtAuthenticationToken authToken = JwtAuthenticationToken.unauthenticated(jwtToken, null);

            try {
                authenticationManager.authenticate(authToken);
            } catch (Exception ex) {

                ErrorResponse errorResponse = new ErrorResponse("Authentication error", request.getRequestURL().toString());
                String json = new ObjectMapper().writeValueAsString(errorResponse);
                response.setContentType("application/json");
                response.setStatus(401);
                response.getWriter().write(json);
                response.flushBuffer();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
