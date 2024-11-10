package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

//        private Long userId;
        private String accessToken;
        private String refreshToken; // we can remove it and just store the refresh token in the cookie

}
