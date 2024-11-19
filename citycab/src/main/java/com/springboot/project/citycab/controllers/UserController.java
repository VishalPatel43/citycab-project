package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.UserDTO;
import com.springboot.project.citycab.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getMyProfile")
    public ResponseEntity<UserDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @PostMapping("/updateMyProfile/{userId}")
    public ResponseEntity<UserDTO> updateMyProfile(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, userDTO));
    }

    @PostMapping("/updateMyPassword/{userId}")
    public ResponseEntity<UserDTO> updateMyPassword(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, userDTO));
    }

    @PostMapping("/updateMyRoles/{userId}")
    public ResponseEntity<UserDTO> updateMyRoles(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, userDTO));
    }

    @PostMapping("/deleteMyProfile/{userId}")
    public ResponseEntity<UserDTO> deleteMyProfile(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, userDTO));
    }
}
