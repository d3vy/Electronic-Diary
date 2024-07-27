package com.iliadevy.electronicDiary.services;

import com.iliadevy.electronicDiary.models.Role;
import com.iliadevy.electronicDiary.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER").get();
    }




}
