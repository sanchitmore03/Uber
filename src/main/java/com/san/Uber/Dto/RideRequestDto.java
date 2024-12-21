package com.san.Uber.Dto;

import com.san.Uber.entities.Rider;
import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long id;


    private PointDto pickupLocation;

    private PointDto dropOffLocation;

    private LocalDateTime requestedTime;


    private RiderDto rider;


    private PayementMethod payementMethod;


    private RideRequestStatus rideRequestStatus;
}
