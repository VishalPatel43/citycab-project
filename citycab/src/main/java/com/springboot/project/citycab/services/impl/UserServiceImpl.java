package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.RolesDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.repositories.UserRepository;
import com.springboot.project.citycab.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
                .orElse(null); // it will help in the OAuth2 implementation
    }

    @Transactional
    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public User updateUserProfile(Long userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public User updateRoles(Long userId, RolesDTO rolesDTO) {
        return null;
    }
}
