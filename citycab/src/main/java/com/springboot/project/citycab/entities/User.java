package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    // Create table for roles only
    // Change Column name to user_roles
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "roles") // Specifies the column name for the roles
    private Set<Role> roles;

//    MobileNumber, Birthdate, Gender, Address, Profile Picture, etc.

    // See the Mapping carefully
//    @OneToOne(mappedBy = "user")
//    private Rider rider;

//    @OneToOne(mappedBy = "user")
//    private Driver driver;
}
