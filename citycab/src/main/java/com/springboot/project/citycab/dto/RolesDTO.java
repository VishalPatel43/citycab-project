package com.springboot.project.citycab.dto;


import com.springboot.project.citycab.constants.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RolesDTO {

    //    @UserRoleValidation
    private Set<Role> roles;

}
