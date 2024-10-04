package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.services.AdminService;
import com.springboot.project.citycab.services.DriverService;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DriverService driverService;
    private final RiderService riderService;

    @Override
    public Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest) {
        return riderService.findRidersByName(name, pageRequest);
    }

    @Override
    public Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest) {
        return driverService.findDriversByName(name, pageRequest);
    }


}
