package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.repository.UserAPIRepository;
import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import com.grootgeek.apibookkinder.utils.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioDetailsService.class);

    private final Logger utilLogs;
    @Value("${application.name}")
    private String applicationName;

    private final UserAPIRepository userRepo;

    @Autowired
    public UsuarioDetailsService(Logger utilLogs, UserAPIRepository userRepo) {
        this.utilLogs = utilLogs;
        this.userRepo = userRepo;
    }

    @Override // Validate user by username
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<UserAPIEntity> userGet = userRepo.findByUserName(username);
            if (userGet.isPresent() && username.equals(userGet.get().getUserName())) {

                if (userGet.get().getAppName().equals(applicationName)) {
                    return new org.springframework.security.core.userdetails.User(userGet.get().getUserName(), userGet.get().getPassword(), new ArrayList<>());
                } else {
                    utilLogs.logApiError("Application name incorrect");
                }
            }
        } catch (Exception e) {
            String message = "User not found with username: " + username + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
        }
        return null;
    }

    // validate user password
    public Boolean validatePassword(String username, String password) throws UsernameNotFoundException {
        Optional<UserAPIEntity> userGet = userRepo.findByUserName(username);
        boolean response = false;
        if (userGet.isPresent()) {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            response = b.matches(password, userGet.get().getPassword());
        }
        return response;
    }

}
