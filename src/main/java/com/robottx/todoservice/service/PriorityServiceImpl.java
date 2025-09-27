package com.robottx.todoservice.service;

import com.robottx.todoservice.entity.Priority;
import com.robottx.todoservice.repository.PriorityRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;

    @Override
    public Priority getPriorityByLevel(Integer level) {
        return priorityRepository.findByPriorityLevel(level)
                .orElseThrow(() -> new IllegalArgumentException("Invalid priority level: " + level));
    }

}
