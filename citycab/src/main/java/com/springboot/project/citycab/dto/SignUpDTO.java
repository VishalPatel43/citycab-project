package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private LocalDate birthdate;
    private Gender gender;

}
