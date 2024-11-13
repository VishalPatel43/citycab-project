package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.Gender;
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
    private String mobileNumber;
    private Gender gender;

//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate birthdate;
    private String birthdate;
    private Set<Role> roles;
}
