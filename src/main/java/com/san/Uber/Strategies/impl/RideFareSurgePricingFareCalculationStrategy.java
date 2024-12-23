package com.san.Uber.Strategies.impl;

import com.san.Uber.Services.DistanceService;
import com.san.Uber.Strategies.RideFareClaculationStrategy;
import com.san.Uber.entities.RideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareClaculationStrategy {

    private final DistanceService distanceService;
    private static final double SERGE_FACTOR = 2;
    @Override
    public double fareCalculation(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());
        return distance*RIDE_FARE_MULTIPLIER*SERGE_FACTOR;
    }
}
