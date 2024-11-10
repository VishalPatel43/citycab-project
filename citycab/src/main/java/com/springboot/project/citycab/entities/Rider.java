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

//    private Boolean availableAsRider; // --> If work as rider then can't be available as driver

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
