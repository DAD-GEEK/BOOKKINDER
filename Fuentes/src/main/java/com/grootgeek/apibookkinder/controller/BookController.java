package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.RespuestaApiDto;
import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.service.UserAppService;
import com.grootgeek.apibookkinder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping
public class BookController {
    /*
    private final UserAppService usuarioAppService;
    BCryptPasswordEncoder b = new BCryptPasswordEncoder();
    private final Utils utilLogs;

    @Autowired
    public BookController(UserAppService usuarioAppService, Utils utilLogs) {
        this.usuarioAppService = usuarioAppService;
        this.utilLogs = utilLogs;
    }

    @PostMapping("/book/new")
    public RespuestaApiDto<Object> newbook(@RequestBody UserAppEntity newBookReq, HttpServletRequest request) {
        String logbook;
        RespuestaApiDto<Object> response = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Create user APP " + newBookReq.getEmail(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final UserAppEntity validateUser = usuarioAppService.findByEmail(newBookReq.getEmail());
        if (validateUser != null) {
            response.setSuccess(false);
            response.setCodigo("User-01");
            response.setMessage("User already exists");
            logbook = response.getCodigo() + " username: " + newBookReq.getEmail() + " " + response.getMessage();
            utilLogs.logApi(1, logbook, BookController.class.getSimpleName(), request.getRemoteAddr());
        } else {
            if (newBookReq.getRole() == null) {
                newBookReq.setRole("0");
            } else if (newBookReq.getRole().equals("1") && newBookReq.getRating() == null) {
                newBookReq.setRating("0");
            }
            newBookReq.setPassword(b.encode(newBookReq.getPassword()));
            final UserAppEntity createUser = usuarioAppService.saveNewUser(newBookReq);
            if (createUser != null) {
                response.setSuccess(true);
                response.setCodigo("User-01");
                response.setMessage("User created successfully");
                newBookReq.setPassword("[PROTECTED]");
                response.setData(newBookReq);
                logbook = response.getMessage() + ": " + response.getData() + " ";
            } else {
                response.setSuccess(false);
                response.setCodigo("ErrorUser-01");
                response.setMessage("Error creating user");
                logbook = response.getCodigo() + " username: " + newBookReq.getEmail();
                utilLogs.logApiError(logbook);
            }
            utilLogs.logApi(1, logbook, BookController.class.getSimpleName(), request.getRemoteAddr());
        }
        return response;
    }

    @PostMapping("/user/authenticate")
    public RespuestaApiDto<Object> authenticateUserAPP(@RequestBody UserAppEntity authenticationReq, HttpServletRequest request) {
        String logAuth;
        RespuestaApiDto<Object> response = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Authenticating user APP " + authenticationReq.getEmail(), AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        final Boolean validatePassword = usuarioAppService.validatePassword(authenticationReq.getEmail(), authenticationReq.getPassword());
        if (validatePassword) {
            response.setSuccess(true);
            response.setCodigo("User-02");
            response.setMessage("User Authenticate successfully");
            logAuth = response.getMessage() + ": " + authenticationReq.getEmail();
        } else {
            response.setSuccess(false);
            response.setCodigo("ErrorUser-02");
            response.setMessage("Incorrect user or password");
            logAuth = response.getCodigo() + " " + response.getCodigo() + " username: " + authenticationReq.getEmail();
            utilLogs.logApiError(logAuth);
        }
        utilLogs.logApi(1, logAuth, AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }

    @PostMapping("/user/update")
    public RespuestaApiDto<Object> updateUserAPP(@RequestBody UserAppEntity updateUserReq, HttpServletRequest request) {
        String logUpdate;
        RespuestaApiDto<Object> response = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Update user APP " + updateUserReq.getEmail(), AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        if (updateUserReq.getPassword() != null) {
            updateUserReq.setPassword(b.encode(updateUserReq.getPassword()));
        }
        final UserAppEntity updateUser = usuarioAppService.updateUserApp(updateUserReq);
        if (updateUser != null) {
            response.setSuccess(true);
            response.setCodigo("User-03");
            response.setMessage("User update successfully");
            updateUser.setPassword("[PROTECTED]");
            response.setData(updateUser);
            logUpdate = response.getMessage() + ": " + response.getData() + " ";
        } else {
            response.setSuccess(false);
            response.setCodigo("ErrorUser-03");
            response.setMessage("Error updating user");
            logUpdate = response.getCodigo() + " username: " + updateUserReq.getEmail();
            utilLogs.logApiError(logUpdate);
        }
        utilLogs.logApi(1, logUpdate, BookController.class.getSimpleName(), request.getRemoteAddr());
        return response;
    }

    @PostMapping("/user/delete")
    public RespuestaApiDto<Object> deleteUserAPP(@RequestBody UserAppEntity deleteUserReq, HttpServletRequest request) {
        String logDelete;
        RespuestaApiDto<Object> responseDelete = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Delete user APP " + deleteUserReq.getEmail(), AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        final boolean deleteUser = usuarioAppService.deleteByEmail(deleteUserReq.getEmail());
        if (deleteUser) {
            responseDelete.setSuccess(true);
            responseDelete.setCodigo("User-04");
            responseDelete.setMessage("User delete successfully");
            logDelete = responseDelete.getMessage() + ": " + deleteUserReq.getEmail() + " ";
        } else {
            responseDelete.setSuccess(false);
            responseDelete.setCodigo("ErrorUser-04");
            responseDelete.setMessage("Error delete user");
            logDelete = responseDelete.getCodigo() + " username: " + deleteUserReq.getEmail();
            utilLogs.logApiError(logDelete);
        }
        utilLogs.logApi(1, logDelete, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseDelete;
    }

    @PostMapping("/user/read/all")
    public RespuestaApiDto<Object> deleteUserAPP(HttpServletRequest request) {
        String logReadAll;
        RespuestaApiDto<Object> responseReadALL = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Read all users APP " , AutenticationController.class.getSimpleName(), request.getRemoteAddr());
        final List<UserAppEntity> readAllUsers = usuarioAppService.findall();
        if (readAllUsers != null) {
            readAllUsers.forEach(user -> user.setPassword("[PROTECTED]"));
            responseReadALL.setSuccess(true);
            responseReadALL.setCodigo("User-05");
            responseReadALL.setMessage("Users Read successfully");
            responseReadALL.setData(readAllUsers);
            logReadAll = responseReadALL.getMessage() + ": Usuarios encontrados = " + readAllUsers.size() + " ";
        } else {
            responseReadALL.setSuccess(false);
            responseReadALL.setCodigo("ErrorUser-05");
            responseReadALL.setMessage("Error Read users");
            logReadAll = responseReadALL.getCodigo() + " " + responseReadALL.getMessage();
            utilLogs.logApiError(logReadAll);
        }
        utilLogs.logApi(1, logReadAll, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseReadALL;
    }
  */
}
