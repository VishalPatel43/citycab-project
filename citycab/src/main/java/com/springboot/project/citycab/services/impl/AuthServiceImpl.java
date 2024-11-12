package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.LoginRequestDTO;
import com.springboot.project.citycab.dto.LoginResponseDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Wallet;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.security.JWTService;
import com.springboot.project.citycab.services.AuthService;
import com.springboot.project.citycab.services.RiderService;
import com.springboot.project.citycab.services.UserService;
import com.springboot.project.citycab.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    // Service
    private final JWTService jwtService;
    private final RiderService riderService;
    private final WalletService walletService;
    // Mapper
    private final ModelMapper modelMapper;
    // Password Encoder
    private final PasswordEncoder passwordEncoder;
    // Authentication Manager
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        // Authenticate User
        User user = authenticateUser(loginRequestDTO);

        Role activeRole = loginRequestDTO.getAcitveRole();
        if (activeRole != null) {
            if (user.getRoles().contains(Role.ADMIN) || (user.getRoles().contains(activeRole))) {
                user.setActiveRole(activeRole);
                user = userService.save(user);
            } else throw new ResourceNotFoundException("User does not have the role " + activeRole);
        }

        log.info("User: {}", user);
        // Generate JWT Token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    private User authenticateUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));
        return (User) authentication.getPrincipal();
    }

    @Override
    @Transactional
    public UserDTO singUp(SignUpDTO signUpDTO) {

        User user = userService.getUserByEmail(signUpDTO.getEmail());
        if (user != null)
            // used for bad request error
            throw new RuntimeConflictException("Cannot signup, User already exists with email " + signUpDTO.getEmail());

        User mappedUser = modelMapper.map(signUpDTO, User.class);
        mappedUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

        // When user signed up it will be a rider, after that onl admin can onboard him as a driver
        mappedUser.setRoles(Set.of(Role.RIDER));

        User savedUser = userService.save(mappedUser);

        // Create User related entities (When we signup it will create so many entities related to user like wallet, rider, etc)
//        Rider rider = riderService.createNewRider(savedUser);
        Rider rider = riderService.createNewRider(savedUser);

        // TODO: Add Wallet related service here
        Wallet wallet = walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(accessToken, refreshToken);
    }
}
