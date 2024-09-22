package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.project.citycab.serializers.PointSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private Double rating;

    private Boolean available;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Vehicle information
    // One to One mapping with Vehicle Entity
    // Vehicle Type, Vehicle Number, Vehicle Model, Vehicle Color, etc.

    // Custom getter to serialize the Point as PointDTO
//    public PointDTO getCurrentLocation() {
//        if (currentLocation != null) {
//            return new PointDTO(
//                    new double[]{
//                            currentLocation.getX(),
//                            currentLocation.getY()
//                    }
//            );
//        }
//        return null;
//    }
}
