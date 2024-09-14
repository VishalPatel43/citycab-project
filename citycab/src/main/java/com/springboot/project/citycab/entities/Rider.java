package com.springboot.project.citycab.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    // Why rating in Rider entity?
    private Double rating;

    private Boolean available;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
