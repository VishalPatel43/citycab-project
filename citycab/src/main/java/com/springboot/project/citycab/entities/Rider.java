package com.springboot.project.citycab.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    private Double rating;

    private Boolean available;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
