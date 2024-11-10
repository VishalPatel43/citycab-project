package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.project.citycab.serializers.PointSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(indexes = {
        @Index(name = "idx_driver_vehicle_id", columnList = "vehicleId")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private Double avgRating;

    private Boolean available;

//    private Boolean availableAsDriver; // --> If work as driver then can't be available as rider

    private String vehicleId;

//    private String licenseNumber;

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


    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", avgRating=" + avgRating +
                ", available=" + available +
                ", vehicleId='" + vehicleId + '\'' +
                ", currentLocation=" + currentLocation + "\n" +
                ", user=" + user +
                '}';
    }
}
