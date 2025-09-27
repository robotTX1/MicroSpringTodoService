package com.robottx.todoservice.service;

import com.robottx.todoservice.entity.UserAccessLevel;
import com.robottx.todoservice.repository.UserAccessLevelRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccessLevelServiceImpl implements UserAccessLevelService {

    private final UserAccessLevelRepository userAccessLevelRepository;

    @Override
    public UserAccessLevel findByLevel(Integer level) {
        return userAccessLevelRepository.findByAccessLevel(level)
                .orElseThrow(() -> new IllegalArgumentException("Invalid access level: " + level));
    }

}
