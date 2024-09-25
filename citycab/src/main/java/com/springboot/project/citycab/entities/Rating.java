package com.springboot.project.citycab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @OneToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private Integer driverRating; //rating for the driver
    private Integer riderRating; //rating for the rider
}


//10 ratingsCount -> 4.0
//new rating 4.6
//updated rating
//new rating 44.6/11 -> 4.05