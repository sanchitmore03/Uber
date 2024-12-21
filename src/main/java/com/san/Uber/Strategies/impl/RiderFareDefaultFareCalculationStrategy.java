package com.san.Uber.Strategies.impl;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Strategies.RideFareClaculationStrategy;

public class RiderFareDefaultFareCalculationStrategy implements RideFareClaculationStrategy {
    @Override
    public double fareCalculation(RideRequestDto rideRequestDto) {
        return 0;
    }
}