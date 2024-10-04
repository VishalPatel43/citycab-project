package com.springboot.project.citycab.repositories;

import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
//@RepositoryRestResource(exported = false)
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}
