package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.OnboardDriverDTO;
import com.springboot.project.citycab.dto.SignUpDTO;
import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/signup")
    ResponseEntity<UserDTO> singUp(@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(authService.singUp(signUpDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDTO> onBoardNewDriver(@PathVariable Long userId,
                                               @RequestBody OnboardDriverDTO onboardDriverDTO) {
        return new ResponseEntity<>(authService.onboardNewDriver(userId,
                onboardDriverDTO.getVehicleId()), HttpStatus.CREATED);
    }
}
