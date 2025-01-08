package com.san.Uber.Dto;

import lombok.Data;

@Data
public class RideRequestDto {
    private LocationDto pickupLocation;
    private LocationDto dropOffLocation;
    private String paymentMethod;
}

@Data
class LocationDto {
    private double[] coordinates;  // [longitude, latitude]
}
