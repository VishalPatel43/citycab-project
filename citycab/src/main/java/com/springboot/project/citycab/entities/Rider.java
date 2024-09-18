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

    // Why rating in Rider entity? or consider avg rating given to the rider by the driver
    // Here use the Rating entity to store the rating given by the driver to the rider
    // or When driver gives rating to the rider update (Avg) the rating in the Rider entity
    private Double rating;

//    private Boolean available;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Rider{" +
                "riderId=" + riderId +
                ", rating=" + rating +
//                ", available=" + available +
                ", user=" + user +
                '}';
    }
}
