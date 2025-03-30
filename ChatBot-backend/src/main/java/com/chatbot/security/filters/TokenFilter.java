package com.chatbot.security.filters;

import com.chatbot.security.JwtCore;
import com.chatbot.security.UserProfileDetails;
import com.chatbot.security.contexts.AuthContext;
import com.chatbot.security.contexts.AuthContextHolder;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username;

        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer "))
                jwt = headerAuth.substring(7);

            if (jwt != null) {
                try {
                    username = jwtCore.getUsernameFromJwt(jwt);
                } catch (ExpiredJwtException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized");
                    return;
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userProfileDetails = (UserProfileDetails) userDetailsService.loadUserByUsername(username);
                    var authenticationToken = new UsernamePasswordAuthenticationToken(userProfileDetails, null, userProfileDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    AuthContextHolder.set(new AuthContext(userProfileDetails.getId(), userProfileDetails.getRealUsername(), userProfileDetails.getEmail()));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthContextHolder.remove();
        }
    }

}
