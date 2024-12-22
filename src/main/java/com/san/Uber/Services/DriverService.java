package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Dto.SignupDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DriverService {
    RideDto cancelRide(Long rideId);
    RideDto acceptRide(Long rideId);
    RideDto startRide(Long rideId);
    RideDto endRide(Long rideId);
    RiderDto rateRider(Long rideId,Integer rating);
    DriverDto getMyProfile();
    List<RideDto> getAllMyrides();


}
