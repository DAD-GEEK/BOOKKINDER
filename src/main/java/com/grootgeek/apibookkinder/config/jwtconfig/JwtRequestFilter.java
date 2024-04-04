package com.grootgeek.apibookkinder.config.jwtconfig;


import com.grootgeek.apibookkinder.dto.ResponseApiDto;
import com.grootgeek.apibookkinder.service.JwtUtilService;
import com.grootgeek.apibookkinder.service.UsuarioDetailsService;
import com.grootgeek.apibookkinder.utils.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UsuarioDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;
    private final Logger utilLogs;

    public JwtRequestFilter(UsuarioDetailsService userDetailsService, JwtUtilService jwtUtilService, Logger utilLogs) {
        this.userDetailsService = userDetailsService;
        this.jwtUtilService = jwtUtilService;
        this.utilLogs = utilLogs;
    }

    private void exception(HttpServletResponse response, ResponseApiDto<Object> responseError) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseError);
        response.getWriter().write(jsonResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String jwtInvalid = "JWT invalid";
        ResponseApiDto<Object> responseError = new ResponseApiDto<>();
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtilService.extractUsername(jwt);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtilService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }   
            }
            filterChain.doFilter(request, response);

        } catch (ServletException e) {
            utilLogs.logApiError("Error ---> " + e.getMessage());
            responseError.responseError(false, "TK-001", e.getMessage());
            exception(response, responseError);
            logger.error(e);

        } catch (ExpiredJwtException e) {
            utilLogs.logApiError("Token expired ---> " + e.getMessage());
            responseError.responseError(false, "TK-002", jwtInvalid + " expired");
            exception(response, responseError);
            logger.error(e);

        } catch (SignatureException e) {
            utilLogs.logApiError("Token error signature ---> " + e.getMessage());
            responseError.responseError(false, "TK-003", jwtInvalid + " error signature");
            exception(response, responseError);
            logger.error(e);
        }
    }
}