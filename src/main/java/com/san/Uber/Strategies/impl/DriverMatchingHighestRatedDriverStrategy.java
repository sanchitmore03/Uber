package com.san.Uber.Strategies.impl;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Strategies.DriverMatchingStrategy;
import com.san.Uber.entities.Driver;

import java.util.List;

public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Driver> findMatchingDriver(RideRequestDto rideRequestDto) {
        return List.of();
    }
}
