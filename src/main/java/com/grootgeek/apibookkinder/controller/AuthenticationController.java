package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.ResponseApiDto;
import com.grootgeek.apibookkinder.dto.TokenResponseDto;
import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import com.grootgeek.apibookkinder.service.JwtUtilService;
import com.grootgeek.apibookkinder.service.UsuarioDetailsService;
import com.grootgeek.apibookkinder.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping
public class AuthenticationController {

    @Value("${token.type}")
    private String typeToken;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtUtilService jwtUtilService;
    private final Logger utilLogs;

    @Autowired
    public AuthenticationController(UsuarioDetailsService usuarioDetailsService, JwtUtilService jwtUtilService, Logger utilLogs) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtUtilService = jwtUtilService;
        this.utilLogs = utilLogs;

    }
    @PostMapping("/publico/authenticate")
    public ResponseApiDto<Object> authenticate(@RequestBody UserAPIEntity authenticationReq, HttpServletRequest request) {
        String logAuthenticate;
        ResponseApiDto<Object> response = new ResponseApiDto<>();
        utilLogs.logApi(0, "Autenticando al usuario " + authenticationReq.getUserName(), AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        final Boolean validatePassword = usuarioDetailsService.validatePassword(authenticationReq.getUserName(), authenticationReq.getPassword());
        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUserName());
        if (Boolean.TRUE.equals(validatePassword) && userDetails != null) {
            final String jwt = jwtUtilService.generateToken(userDetails);
            TokenResponseDto tokenInfo = new TokenResponseDto(typeToken, jwt);
            response.responseSuccess(true, "000", "Token created successfully", tokenInfo);
            logAuthenticate = response.getMessage() + ": " + response.getData() + " ";
        } else {
            response.responseError(false, "AUTH-001", "Incorrect user or password");
            logAuthenticate = response.getCode() + " " + response.getMessage() + " username: " + authenticationReq.getUserName();
            utilLogs.logApiError(logAuthenticate);
        }
        utilLogs.logApi(1, logAuthenticate, AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }

    @GetMapping("/publico/authenticate")
    public ResponseApiDto<Object> authenticatetwo(HttpServletRequest request) {
        String logAuthenticate;
        UserAPIEntity authenticationReq = new UserAPIEntity();
        authenticationReq.setUserName("lejodurango@gmail.com");
        authenticationReq.setPassword("i96/x0rqiJ8m");
        ResponseApiDto<Object> response = new ResponseApiDto<>();
        utilLogs.logApi(0, "Autenticando al usuario " + authenticationReq.getUserName(), AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        final Boolean validatePassword = usuarioDetailsService.validatePassword(authenticationReq.getUserName(), authenticationReq.getPassword());
        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUserName());
        if (Boolean.TRUE.equals(validatePassword) && userDetails != null) {
            final String jwt = jwtUtilService.generateToken(userDetails);
            TokenResponseDto tokenInfo = new TokenResponseDto(typeToken, jwt);
            response.responseSuccess(true, "000", "Token created successfully", tokenInfo);
            logAuthenticate = response.getMessage() + ": " + response.getData() + " ";
        } else {
            response.responseError(false, "AUTH-001", "Incorrect user or password");
            logAuthenticate = response.getCode() + " " + response.getMessage() + " username: " + authenticationReq.getUserName();
            utilLogs.logApiError(logAuthenticate);
        }
        utilLogs.logApi(1, logAuthenticate, AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }

    @PostMapping("/ValidateToken")
    public ResponseApiDto<Object> validateToken(HttpServletRequest request) {
        ResponseApiDto<Object> response = new ResponseApiDto<>();
        utilLogs.logApi(0, "Validando Token", AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        response.responseSuccess(true, "001", "Successfully authorized", null);
        String logValidateToken = "Token Valido: " + response.getMessage();
        utilLogs.logApi(1, logValidateToken, AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }
}
