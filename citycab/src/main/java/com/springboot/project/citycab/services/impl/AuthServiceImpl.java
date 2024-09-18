package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.UserRepository;
import com.springboot.project.citycab.services.AuthService;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Repository
    private final UserRepository userRepository;
    // Service
    private final RiderService riderService;
    // Mapper
    private final ModelMapper modelMapper;

    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    @Transactional
    public UserDTO singUp(SignUpDTO signUpDTO) {

        User user = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);
        if (user != null)
            // used for bad request error
            throw new RuntimeConflictException("Cannot signup, User already exists with email " + signUpDTO.getEmail());

        User mappedUser = modelMapper.map(signUpDTO, User.class);

        // When user signed up it will be a rider, after that onl admin can onboard him as a driver
        mappedUser.setRoles(Set.of(Role.RIDER));

        User savedUser = userRepository.save(mappedUser);

        // Create User related entities (When we signup it will create so many entities related to user like wallet, rider, etc)
//        Rider rider = riderService.createNewRider(savedUser);
        Rider rider = riderService.createNewRider(savedUser);

        // TODO: Add Wallet related service here

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public DriverDTO onBoardNewDriver(Long userId) {
        return null;
    }
}
