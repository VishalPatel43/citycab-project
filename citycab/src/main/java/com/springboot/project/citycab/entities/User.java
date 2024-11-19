package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.project.citycab.constants.enums.Gender;
import com.springboot.project.citycab.constants.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_user", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true, nullable = false)
    private String mobileNumber; // add prefix +91-1234567890 or add with country code

    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate birthdate;
    private String birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Don't add in Database
//    @Transient
    @Enumerated(EnumType.STRING)
    private Role activeRole;

    // Create table for roles only
    // Change Column name to user_roles
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role") // Specifies the column name for the roles
    private Set<Role> roles;

    // See the Mapping carefully
//    @OneToOne(mappedBy = "user")
//    private Rider rider;

//    @OneToOne(mappedBy = "user")
//    private Driver driver;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", activeRole='" + activeRole + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    // getPassword already there
}
