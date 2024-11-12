package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email of the employee cannot be blank")
    @Email(message = "Email should be a valid email")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

    private Role acitveRole;
}
