package com.springboot.project.citycab.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {

    private Long ratingId;

    //    @NotNull(message = "Rating cannot be null")
//    @Min(value = 1, message = "Rating must be at least 1")
//    @Max(value = 5, message = "Rating must be at most 5")
    private Double driverRating;

    //    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    private LocalDateTime ratingDate;

//    private RideDTO ride;
}
