package com.san.Uber.Strategies.impl;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Strategies.RideFareClaculationStrategy;
import com.san.Uber.entities.RideRequest;

public class RideFareSurgePricingFareCalculationStrategy implements RideFareClaculationStrategy {

    @Override
    public double fareCalculation(RideRequest rideRequest) {
        return 0;
    }
}
