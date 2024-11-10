package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    UserDTO singUp(SignUpDTO signUpDTO);

    DriverDTO onboardNewDriver(Long userId, String vehicleId);
}
