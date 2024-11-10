package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.RolesDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.User;

public interface UserService {

    User getCurrentUser();

    UserDTO getUserProfile();

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User save(User newUser);

    User updateUserProfile(Long userId, UserDTO userDTO);

    User updateRoles(Long userId, RolesDTO rolesDTO);
}
