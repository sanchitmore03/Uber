package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Rider;
import com.san.Uber.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RiderService {
    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);
    DriverDto rateDriver(Long rideId, Integer rating);
    RideDto getMyProfile();
    List<RideDto> getAllMyrides();
    Rider createNewRider(User user);
}
