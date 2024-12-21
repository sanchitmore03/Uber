package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Services.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DriverServiceIml implements DriverService {
    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto acceptRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto startRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto endRide(Long rideId) {
        return null;
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyrides() {
        return List.of();
    }
}
