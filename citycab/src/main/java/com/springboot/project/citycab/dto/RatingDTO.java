package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    private Long ratingId;

    private Long rideId;

    private Integer rating;
}
