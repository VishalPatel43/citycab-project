package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String name;
    private String email;
    private Set<Role> roles;
}
