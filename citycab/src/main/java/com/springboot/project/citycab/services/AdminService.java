package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AdminService {

//    Page<DriverDTO> getAllDrivers(PageRequest pageRequest);
//    Page<RiderDTO> getAllRiders(PageRequest pageRequest);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);
    Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest);
}
