package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.LoginRequestDTO;
import com.springboot.project.citycab.dto.LoginResponseDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    UserDTO singUp(SignUpDTO signUpDTO);

    LoginResponseDTO refreshToken(String refreshToken);
}
