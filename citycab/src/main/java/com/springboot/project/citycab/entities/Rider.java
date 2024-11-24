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

    private Double avgGivenRating;

    private Boolean available;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Rider{" +
                "riderId=" + riderId + "\n" +
//                ", available=" + available +
                ", user=" + user +
                '}';
    }
}
