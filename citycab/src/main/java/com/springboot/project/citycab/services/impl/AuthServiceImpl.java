package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Wallet;
import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.UserRepository;
import com.springboot.project.citycab.security.JWTService;
import com.springboot.project.citycab.services.*;
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
    private final DriverService driverService;
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

        // Generate JWT Token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    private User authenticateUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));

        log.info("Authenticated User: {}", (User) authentication.getPrincipal());
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
    public DriverDTO onboardNewDriver(Long userId, String vehicleId) {
        User user = userService.getUserById(userId);

        if (user.getRoles().contains(Role.DRIVER))
            throw new RuntimeConflictException("User with id: " + userId + " is already a Driver");

        Driver createDriver = Driver.builder()
                .user(user)
                .avgRating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();

        user.getRoles().add(Role.DRIVER);
        userService.save(user);

        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(accessToken, refreshToken);
    }
}
