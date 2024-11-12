package com.springboot.project.citycab.utils;

import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final UserService userService;

    public boolean hasActiveRole(String role) {
        User user = userService.getCurrentUser();

        return user.getActiveRole().toString().equals(role);
    }
}
