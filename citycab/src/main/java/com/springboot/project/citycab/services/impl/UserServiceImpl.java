package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.RolesDTO;
import com.springboot.project.citycab.dto.UpdatePasswordDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.UserRepository;
import com.springboot.project.citycab.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User not found with email: " + username));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user))
            throw new UsernameNotFoundException("No authenticated user found");
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Current User: {}", user);
        return user;
    }

    @Override
    public UserDTO getUserProfile() {
        User currentUser = getCurrentUser();
        return modelMapper.map(currentUser, UserDTO.class);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    @Override
    public UserDTO findUserById(Long userId) {
        User user = getUserById(userId);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Transactional
    @Override
    public UserDTO saveUser(Long userId, UserDTO userDTO) {
        getUserById(userId);
        User user = modelMapper.map(userDTO, User.class);
        user.setUserId(userId);
        return modelMapper.map(saveUser(user), UserDTO.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
                .orElse(null); // Supports OAuth2 use cases

    }

    @Override
    public User getUserByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElse(null);
    }

    @Transactional
    @Override
    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        validateUserAccess(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User validateUserForRole(Long userId, Role role, String roleName) {
        User user = getUserById(userId);
        if (user.getRoles().contains(role))
            throw new RuntimeConflictException("User with id: " + userId + " is already a " + roleName);
        return user;
    }

    @Override
    public UserDTO updateUserProfile(Long userId, UserDTO userDTO) {
        User user = validateUserAccess(userId);

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setGender(userDTO.getGender());
        user.setBirthdate(userDTO.getBirthdate());

        // we don't update the roles here, we have a separate method for updating roles
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Transactional
    @Override
    public UserDTO updateRoles(Long userId, RolesDTO rolesDTO) {
        User currentUser = getCurrentUser();
        User user = getUserById(userId);

        if (!currentUser.getRoles().contains(Role.ADMIN))
            throw new AccessDeniedException("You are not authorized to update roles, Only Admin can update roles");

        user.setRoles(rolesDTO.getRoles());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {

        User user = validateUserAccess(userId);

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword()))
            throw new BadCredentialsException("Current password is incorrect");

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);

    }

    private User validateUserAccess(Long userId) {
        User user = getUserById(userId);
        User currentUser = getCurrentUser();

        if (!currentUser.getUserId().equals(userId) && !currentUser.getRoles().contains(Role.ADMIN))
            throw new AccessDeniedException("You are not authorized to access this user.");

        return user;
    }
}

