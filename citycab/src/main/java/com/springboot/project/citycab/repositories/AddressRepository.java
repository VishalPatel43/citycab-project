package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource(exported = false)
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}

