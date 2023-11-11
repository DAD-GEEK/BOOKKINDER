package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.UserAppEntity;

import java.util.List;

public interface UserAppServiceInterface {

    UserAppEntity saveNewUser(UserAppEntity newUser);

    UserAppEntity findByEmail(String email);

    Boolean validatePassword(String email, String password);

    UserAppEntity updateUserApp(UserAppEntity updateUserApp);

    Boolean deleteByEmail(String email);

    List<UserAppEntity> findall();
}
