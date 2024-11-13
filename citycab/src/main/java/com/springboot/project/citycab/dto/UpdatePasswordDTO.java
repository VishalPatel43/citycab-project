package com.springboot.project.citycab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String newPassword;

    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;

}

