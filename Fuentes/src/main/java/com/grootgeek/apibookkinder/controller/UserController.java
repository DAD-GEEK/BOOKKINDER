package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.ResponseApiDto;
import com.grootgeek.apibookkinder.dto.UserAppDto;
import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.service.UserAppService;
import com.grootgeek.apibookkinder.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping
public class UserController {
    private final UserAppService usuarioAppService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Logger utilLogs;

    @Value("${user.name}")
    private String userName;

    @Value("${user.password}")
    private String userPassword;

    @Autowired
    public UserController(UserAppService usuarioAppService, Logger utilLogs) {
        this.usuarioAppService = usuarioAppService;
        this.utilLogs = utilLogs;
    }

    @PostMapping("/user/new")
    public ResponseApiDto<Object> newUser(@RequestBody UserAppDto newUserReq, HttpServletRequest request) {
        String log;
        ResponseApiDto<Object> responseNewUser = new ResponseApiDto<>();
        utilLogs.logApi(0, "Create user APP " + newUserReq.getEmail(), UserController.class.getSimpleName(), request.getRemoteAddr());
        final UserAppEntity validateUser = usuarioAppService.findByEmail(newUserReq.getEmail());
        if (validateUser != null) {
            responseNewUser.responseError(false, "User-01", "User already exists");
            log = responseNewUser.getCode() + userName + newUserReq.getEmail() + " " + responseNewUser.getMessage();
            utilLogs.logApi(1, log, UserController.class.getSimpleName(), request.getRemoteAddr());
        } else {
            newUserReq.setRole(newUserReq.getRole() == null ? "0" : newUserReq.getRole());
            newUserReq.setRating("1".equals(newUserReq.getRole()) && newUserReq.getRating() == null ? "0" : newUserReq.getRating());
            newUserReq.setPassword(passwordEncoder.encode(newUserReq.getPassword()));
            final UserAppEntity createUser = usuarioAppService.saveNewUser(newUserReq);
            if (createUser != null) {
                createUser.setPassword(userPassword);
                responseNewUser.responseSuccess(true, "User-01", "User created successfully", createUser);
                log = responseNewUser.getMessage() + ": " + responseNewUser.getData() + " ";
            } else {
                responseNewUser.responseError(false, "ErrorUser-01", "Error creating user");
                log = responseNewUser.getCode() + userName + newUserReq.getEmail();
                utilLogs.logApiError(log);
            }
            utilLogs.logApi(1, log, UserController.class.getSimpleName(), request.getRemoteAddr());
        }
        return responseNewUser;
    }

    @PostMapping("/user/authenticate")
    public ResponseApiDto<Object> authenticateUserAPP(@RequestBody UserAppDto authenticationReq, HttpServletRequest request) {
        String logAuthUserAPP;
        ResponseApiDto<Object> responseAuth = new ResponseApiDto<>();
        utilLogs.logApi(0, "Authenticating user APP " + authenticationReq.getEmail(), UserController.class.getSimpleName(), request.getRemoteAddr());
        final Boolean validatePassword = usuarioAppService.validatePassword(authenticationReq.getEmail(), authenticationReq.getPassword());
        if (Boolean.TRUE.equals(validatePassword)) {
            responseAuth.responseSuccess(true, "User-02", "User Authenticate successfully", null);
            logAuthUserAPP = responseAuth.getMessage() + ": " + authenticationReq.getEmail();
        } else {
            responseAuth.responseError(false, "ErrorUser-02", "Incorrect user or password");
            logAuthUserAPP = responseAuth.getCode() + " " + responseAuth.getCode() + userName + authenticationReq.getEmail();
            utilLogs.logApiError(logAuthUserAPP);
        }
        utilLogs.logApi(1, logAuthUserAPP, AuthenticationController.class.getSimpleName(), request.getRemoteAddr());
        return responseAuth;
    }

    @PostMapping("/user/update")
    public ResponseApiDto<Object> updateUserAPP(@RequestBody UserAppDto updateUserReq, HttpServletRequest request) {
        String logUpdateUserApp;
        ResponseApiDto<Object> responseUpdateUserApp = new ResponseApiDto<>();
        utilLogs.logApi(0, "Update user APP " + updateUserReq.getEmail(), UserController.class.getSimpleName(), request.getRemoteAddr());
        updateUserReq.setPassword((updateUserReq.getPassword() != null ? passwordEncoder.encode(updateUserReq.getPassword()) : null));
        final UserAppEntity updateUser = usuarioAppService.updateUserApp(updateUserReq);
        if (updateUser != null) {
            updateUser.setPassword(userPassword);
            responseUpdateUserApp.responseSuccess(true, "User-03", "User update successfully", updateUser);
            logUpdateUserApp = responseUpdateUserApp.getMessage() + ": " + responseUpdateUserApp.getData() + " ";
        } else {
            responseUpdateUserApp.responseError(false, "ErrorUser-03", "Error updating user");
            logUpdateUserApp = responseUpdateUserApp.getCode() + userName + updateUserReq.getEmail();
            utilLogs.logApiError(logUpdateUserApp);
        }
        utilLogs.logApi(1, logUpdateUserApp, UserController.class.getSimpleName(), request.getRemoteAddr());
        return responseUpdateUserApp;
    }

    @PostMapping("/user/delete")
    public ResponseApiDto<Object> readAllUserAPP(@RequestBody UserAppDto deleteUserReq, HttpServletRequest request) {
        String logDeleteUserApp;
        ResponseApiDto<Object> responseDeleteUserApp = new ResponseApiDto<>();
        utilLogs.logApi(0, "Delete user APP " + deleteUserReq.getEmail(), UserController.class.getSimpleName(), request.getRemoteAddr());
        final boolean deleteUser = usuarioAppService.deleteByEmail(deleteUserReq.getEmail());
        if (deleteUser) {
            responseDeleteUserApp.responseSuccess(true, "User-04", "User delete successfully", null);
            logDeleteUserApp = responseDeleteUserApp.getMessage() + ": " + deleteUserReq.getEmail() + " ";
        } else {
            responseDeleteUserApp.responseError(false, "ErrorUser-04", "Error delete user");
            logDeleteUserApp = responseDeleteUserApp.getCode() + userName + deleteUserReq.getEmail();
            utilLogs.logApiError(logDeleteUserApp);
        }
        utilLogs.logApi(1, logDeleteUserApp, UserController.class.getSimpleName(), request.getRemoteAddr());
        return responseDeleteUserApp;
    }

    @PostMapping("/user/read/all")
    public ResponseApiDto<Object> readAllUserAPP(HttpServletRequest request) {
        String logReadAll;
        ResponseApiDto<Object> responseReadAllUsers = new ResponseApiDto<>();
        utilLogs.logApi(0, "Read all users APP ", UserController.class.getSimpleName(), request.getRemoteAddr());
        final List<UserAppEntity> readAllUsers = usuarioAppService.findall();
        if (readAllUsers != null) {
            readAllUsers.forEach(user -> user.setPassword(userPassword));
            responseReadAllUsers.responseSuccess(true, "User-05", "Users Read successfully", readAllUsers);
            logReadAll = responseReadAllUsers.getMessage() + ": Users found = " + readAllUsers.size() + " ";
        } else {
            responseReadAllUsers.responseError(false, "ErrorUser-05", "Error Read users");
            logReadAll = responseReadAllUsers.getCode() + " " + responseReadAllUsers.getMessage();
            utilLogs.logApiError(logReadAll);
        }
        utilLogs.logApi(1, logReadAll, UserController.class.getSimpleName(), request.getRemoteAddr());
        return responseReadAllUsers;
    }

}
