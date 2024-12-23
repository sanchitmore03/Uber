package com.san.Uber.Strategies.impl;

import com.san.Uber.Repositories.DriverRepo;
import com.san.Uber.Strategies.DriverMatchingStrategy;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.RideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepo driverRepo;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepo.findNearByTopRatedDriveres(rideRequest.getPickupLocation());
    }
}
