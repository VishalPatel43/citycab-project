package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/signup")
    UserDTO singUp(@RequestBody SignUpDTO signUpDTO) {
        return authService.singUp(signUpDTO);
    }
}
