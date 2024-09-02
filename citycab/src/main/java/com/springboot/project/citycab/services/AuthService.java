package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;

public interface AuthService {

    String login(String email, String password);

    UserDTO singUp(SignUpDTO signUpDTO);

    DriverDTO onBoardNewDriver(Long userId);
}
