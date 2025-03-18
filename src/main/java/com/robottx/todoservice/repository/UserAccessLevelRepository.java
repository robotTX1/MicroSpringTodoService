package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.UserAccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevel, Integer> {

    Optional<UserAccessLevel> findByAccessLevel(Integer accessLevel);

}
