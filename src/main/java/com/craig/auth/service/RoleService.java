package com.craig.auth.service;

import com.craig.auth.domain.Role;
import com.craig.auth.domain.User;
import com.craig.auth.dto.RoleDto;
import com.craig.auth.dto.UserDto;
import com.craig.auth.repository.RoleRepository;
import com.craig.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public RoleDto createRole(RoleDto role){
        Role roleEntity = new Role();
        roleEntity.setName(role.getName());

        try {
            roleRepository.insert(roleEntity);
            return role;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteRole(RoleDto role){
        Role roleEntity = new Role();
        roleEntity.setName(role.getName());
        return roleRepository.deleteRole(roleEntity);
    }

    public boolean addRoleToUser(RoleDto role, UserDto user){
       User userEntity = userRepository.getUser(user.getName());
       if(userEntity == null){
           return false;
       }
       Role existRole = roleRepository.get(role.getName());
       if(existRole == null){
           return false;
       }
       if(userEntity.getRoles() == null || userEntity.getRoles().isEmpty()){
           userEntity.setRoles(new ArrayList<>());
       }
       if(!userEntity.getRoles().stream().anyMatch(r->r.getName().equals(role.getName()))){
           userEntity.getRoles().add(existRole); //as it's a reference type, no need to save
       }
       return true;
    }

    public boolean checkRole(RoleDto role, UserDto user){
        User userEntity = userRepository.getUser(user.getName());
        if(userEntity == null){
            return false;
        }
        Role existRole = roleRepository.get(role.getName());
        if(existRole == null){
            return false;
        }
        if(userEntity.getRoles() == null || userEntity.getRoles().isEmpty()){
            return false;
        }
        return userEntity.getRoles().stream().anyMatch(r->r.getName().equals(role.getName()));
    }

    public List<RoleDto> list(){
        List<Role> roles = roleRepository.list();
        if(CollectionUtils.isEmpty(roles)){
            return Collections.emptyList();
        }
        return roles.stream().map(c->{
            RoleDto dto = new RoleDto();
            dto.setName(c.getName());
            return  dto;
        }).collect(Collectors.toList());
    }
}
