package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    public UserDTO singUp(SignUpDTO signUpDTO) {
        return null;
    }

    @Override
    public DriverDTO onBoardNewDriver(Long userId) {
        return null;
    }
}
