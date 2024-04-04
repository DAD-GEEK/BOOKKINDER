package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.repository.UserAPPRepository;
import com.grootgeek.apibookkinder.service.interfaces.UserAppServiceInterface;
import com.grootgeek.apibookkinder.utils.Logger;
import java.util.Collections;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


@Service
public class UserAppService implements UserAppServiceInterface {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAppService.class);
    private final Logger utilLogs;
    private final UserAPPRepository userAPPRepository;

    @Autowired
    public UserAppService(Logger utilLogs, UserAPPRepository userAPPRepository) {
        this.utilLogs = utilLogs;
        this.userAPPRepository = userAPPRepository;
    }
    @Override
    public UserAppEntity saveNewUser(UserAppEntity newUser) {
        try {
            UserAppEntity userCreate = userAPPRepository.save(newUser);
            if (userCreate.getEmail().equals(newUser.getEmail())) {
                return userCreate;
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
            if (userGet.isPresent()) {
                return new UserAppEntity(userGet.get());
            } else {
                utilLogs.logApiError("User not Found" + email);
                return null;
            }
        } catch (Exception e) {
            String message = "Error Find User: " + email + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(message);
            return null;
        }
    }

    @Override
    public Boolean validatePassword(String email, String password) throws UsernameNotFoundException {
        boolean response = false;
        Optional<UserAppEntity> userGet = Optional.ofNullable(findByEmail(email));
        if (userGet.isPresent()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(password, userGet.get().getPassword())) {
                response = true;
            }
        }
        return response;
    }

    @Override // method for update user
    public UserAppEntity updateUserApp(UserAppEntity updateUserApp) {
        UserAppEntity userUpdate = findByEmail(updateUserApp.getEmail());
        updateIfNotNullOrEmpty(userUpdate::setPassword, updateUserApp.getPassword());
        updateIfNotNullOrEmpty(userUpdate::setName, updateUserApp.getName());
        updateIfNotNullOrEmpty(userUpdate::setLastName, updateUserApp.getLastName());
        updateIfNotNullOrEmpty(userUpdate::setPhone, updateUserApp.getPhone());
        updateIfNotNullOrEmpty(userUpdate::setRole, updateUserApp.getRole());
        updateIfNotNullOrEmpty(userUpdate::setRating, updateUserApp.getRating());
        UserAppEntity updater = userAPPRepository.save(userUpdate);
        if (updater.getEmail().equals(userUpdate.getEmail())) {
            return updater;
        } else {
            utilLogs.logApiError("Error Update New User " + updater.getEmail());
            return null;
        }
    }
    //method for valide if the value is not null or empty
    private void updateIfNotNullOrEmpty(Consumer<String> setter, String value) {
        if (Objects.nonNull(value) && !"".equalsIgnoreCase(value)) {
            setter.accept(value);
        }
    }
    @Override
    public List<UserAppEntity> findall() {
        try {
            return userAPPRepository.findAll();
        } catch (Exception e) {
            String message = "Error read ALL Users: " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(message);
            return Collections.emptyList();
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
            logger.info(message);
            return false;
        }
    }



}
