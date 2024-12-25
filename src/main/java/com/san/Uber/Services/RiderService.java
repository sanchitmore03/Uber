package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.entities.Rider;
import com.san.Uber.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {
    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);
    DriverDto rateDriver(Long rideId, Integer rating);
    RiderDto getMyProfile();
    Rider createNewRider(User user);

    Rider getCurrentRider();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);
}
