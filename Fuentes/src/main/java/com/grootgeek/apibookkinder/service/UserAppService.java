package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.repository.UserAPPRepository;
import com.grootgeek.apibookkinder.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserAppService implements UserAppServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private final Utils utilLogs;
    private final UserAPPRepository userAPPRepository;

    @Autowired
    public UserAppService(Utils utilLogs, UserAPPRepository userAPPRepository) {
        this.utilLogs = utilLogs;
        this.userAPPRepository = userAPPRepository;
    }
    @Override
    public UserAppEntity saveNewUser(UserAppEntity newUser) {
        try {
            UserAppEntity newuser = userAPPRepository.save(newUser);
            if (newuser.getEmail().equals(newUser.getEmail())) {
                return newuser;
            } else {
                utilLogs.logApiError("Error Save New User " + newUser.getEmail());
                return null;
            }

        } catch (Exception e) {
            String message = "Error Save New User " + newUser.getEmail() + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }

    }
    @Override
    public UserAppEntity findByEmail(String email) {
        try {
            Optional<UserAppEntity> userGet = userAPPRepository.findByEmail(email);
            if (userGet.isPresent() && email.equals(userGet.get().getEmail())) {
                return new UserAppEntity(userGet.get().getEmail(), userGet.get().getPassword(), userGet.get().getName(), userGet.get().getLastName(), userGet.get().getPhone(), userGet.get().getRole(), userGet.get().getRating());
            } else {
                utilLogs.logApiError("User not Found" + email);
                return null;
            }
        } catch (Exception e) {
            String message = "Error Find User: " + email + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }

    @Override
    public Boolean validatePassword(String email, String password) throws UsernameNotFoundException {
        boolean respuesta = false;
        Optional<UserAppEntity> userGet = Optional.ofNullable(findByEmail(email));
        if (userGet.isPresent()) {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            if (b.matches(password, userGet.get().getPassword())) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    @Override
    public UserAppEntity updateUserApp(UserAppEntity updateUserApp) {
        UserAppEntity userUpdate = findByEmail(updateUserApp.getEmail());
        if (Objects.nonNull(updateUserApp.getPassword()) && !"".equalsIgnoreCase(updateUserApp.getPassword())) {
            userUpdate.setPassword(updateUserApp.getPassword());
        }
        if (Objects.nonNull(updateUserApp.getName()) && !"".equalsIgnoreCase(updateUserApp.getName())) {
            userUpdate.setName(updateUserApp.getName());
        }
        if (Objects.nonNull(updateUserApp.getLastName()) && !"".equalsIgnoreCase(updateUserApp.getLastName())) {
            userUpdate.setLastName(updateUserApp.getLastName());
        }
        if (Objects.nonNull(updateUserApp.getPhone()) && !"".equalsIgnoreCase(updateUserApp.getPhone())) {
            userUpdate.setPhone(updateUserApp.getPhone());
        }
        if (Objects.nonNull(updateUserApp.getRole()) && !"".equalsIgnoreCase(updateUserApp.getRole())) {
            userUpdate.setRole(updateUserApp.getRole());
        }
        if (Objects.nonNull(updateUserApp.getRating()) && !"".equalsIgnoreCase(updateUserApp.getRating())) {
            userUpdate.setRating(updateUserApp.getRating());
        }
        UserAppEntity updater = userAPPRepository.save(userUpdate);
        if (updater.getEmail().equals(userUpdate.getEmail())) {
            return updater;
        } else {
            utilLogs.logApiError("Error Update New User " + updater.getEmail());
            return null;
        }
    }

    @Override
    public List<UserAppEntity> findall() {
        try {
            List<UserAppEntity> usersAPP = userAPPRepository.findAll();
            return usersAPP;
        } catch (Exception e) {
            String message = "Error read ALL Users: " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }
    @Override
    public Boolean deleteByEmail(String email) {
        try {
            userAPPRepository.deleteByEmail(email);
            return true;
        } catch (Exception e) {
            String message = "Error delete User: " + email + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return false;
        }
    }



}
