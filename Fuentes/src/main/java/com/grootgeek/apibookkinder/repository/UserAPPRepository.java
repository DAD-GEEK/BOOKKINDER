package com.grootgeek.apibookkinder.repository;

import com.grootgeek.apibookkinder.entities.UserAppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserAPPRepository extends JpaRepository<UserAppEntity, Integer> {
    Optional<UserAppEntity> findByEmail(String email);
    void deleteByEmail(String email);
    }

