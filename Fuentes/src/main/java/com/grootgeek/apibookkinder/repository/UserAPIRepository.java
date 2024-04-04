package com.grootgeek.apibookkinder.repository;

import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserAPIRepository extends JpaRepository<UserAPIEntity, Integer> {
	Optional<UserAPIEntity> findByUserName(String userName);

}
