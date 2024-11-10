package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping(path = "/signup")
    ResponseEntity<UserDTO> singUp(@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(authService.singUp(signUpDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO,
                                           HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);

        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    // write this method in the Admin Controller
    @PostMapping(path = "/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDTO> onBoardNewDriver(@PathVariable Long userId,
                                               @RequestBody OnboardDriverDTO onboardDriverDTO) {
        return new ResponseEntity<>(authService.onboardNewDriver(userId,
                onboardDriverDTO.getVehicleId()), HttpStatus.CREATED);
    }
}
