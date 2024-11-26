package com.springboot.project.citycab.services;

import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.RolesDTO;
import com.springboot.project.citycab.dto.UpdatePasswordDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    User getCurrentUser();

    UserDTO getUserProfile();

    User getUserById(Long userId);

    UserDTO findUserById(Long userId);

    Page<UserDTO> getAllUsers(PageRequest pageRequest);

    UserDTO saveUser(Long userId, UserDTO userDTO);

    User getUserByEmail(String email);

    User getUserByMobileNumber(String mobileNumber);

    User saveUser(User newUser);

    void deleteUser(Long userId);

    User validateUserForRole(Long userId, Role role, String roleName);

    UserDTO updateUserProfile(Long userId, UserDTO userDTO);

    UserDTO updateRoles(Long userId, RolesDTO rolesDTO);

    UserDTO updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO);

//    User resetPassword(Long userId);
}
