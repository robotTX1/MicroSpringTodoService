package com.robottx.todoservice.repository;

import com.robottx.todoservice.entity.UserAccessLevel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevel, Integer> {

    Optional<UserAccessLevel> findByAccessLevel(Integer accessLevel);

}
