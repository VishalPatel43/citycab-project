package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long addressId;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

}
