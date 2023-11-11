package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.RespuestaApiDto;
import com.grootgeek.apibookkinder.dto.TokenResponseDto;
import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import com.grootgeek.apibookkinder.service.JwtUtilService;
import com.grootgeek.apibookkinder.service.UsuarioDetailsService;
import com.grootgeek.apibookkinder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping
public class AutenticationController {

    @Value("${token.type}")
    private String typetoken;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtUtilService jwtUtilService;
    private final Utils utilLogs;

    @Autowired
    public AutenticationController(UsuarioDetailsService usuarioDetailsService, JwtUtilService jwtUtilService, Utils utilLogs) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtUtilService = jwtUtilService;
        this.utilLogs = utilLogs;

    }

    @PostMapping("/publico/authenticate")
    public RespuestaApiDto<Object> authenticate(@RequestBody UserAPIEntity authenticationReq, HttpServletRequest request) {
        String log;
        RespuestaApiDto<Object> response = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Autenticando al usuario " + authenticationReq.getUserName(), AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        final Boolean validatePassword = usuarioDetailsService.validatePassword(authenticationReq.getUserName(), authenticationReq.getPassword());
        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUserName());
        if (validatePassword && userDetails != null) {
            final String jwt = jwtUtilService.generateToken(userDetails);
            TokenResponseDto tokenInfo = new TokenResponseDto(typetoken, jwt);
            response.setSuccess(true);
            response.setCodigo("000");
            response.setMessage("Token created successfully");
            response.setData(tokenInfo);
            log = response.getMessage() + ": " + response.getData() + " ";
        } else {
            response.setCodigo("TK-001");
            response.setMessage("Incorrect user or password");
            log = response.getCodigo() + " " + response.getCodigo() + " username: " + authenticationReq.getUserName();
            utilLogs.logApiError(log);
        }
        utilLogs.logApi(1, log, AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }

    @PostMapping("/ValidateToken")
    public RespuestaApiDto<Object> validateToken(HttpServletRequest request) {
        RespuestaApiDto<Object> response = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Validando Token", AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        response.setSuccess(true);
        response.setCodigo("000");
        response.setMessage("Successfully authorized");
        String tramaRespon = "Token Valido: " + response.getMessage();
        utilLogs.logApi(1, tramaRespon, AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;

    }

}
