package com.homework.basic.presentation.filter;

import com.homework.basic.application.jwt.JwtUtils;
import com.homework.basic.application.jwt.UserDetailsImpl;
import com.homework.basic.application.jwt.UserDetailsServiceImpl;
import com.homework.basic.presentation.error.UserException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "authorization filter")
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        log.info("필터 호출.");
        if (FreePath(request)) {
            log.info("필터 패스");
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader(JwtUtils.AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(bearerToken)) throw new UserException(HttpStatus.UNAUTHORIZED, "token is blank.");

        String token = bearerToken.substring(7);
        if (!jwtUtils.validationToken(token)) throw new UserException(HttpStatus.UNAUTHORIZED, "not validation token.");

        try {
            Claims claims = jwtUtils.extractToken(token);
            setAuthentication(claims.get("username").toString());
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "security context error?");
        }
    }

    private void setAuthentication(String username) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);

        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private Authentication createAuthentication(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private boolean FreePath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/user/signup") || requestURI.startsWith("/api/user/sign");
    }
}
