package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Repositories.RideRequestRepo;
import com.san.Uber.Repositories.RiderRepo;
import com.san.Uber.Services.RiderService;
import com.san.Uber.Strategies.DriverMatchingStrategy;
import com.san.Uber.Strategies.RideFareClaculationStrategy;
import com.san.Uber.entities.RideRequest;
import com.san.Uber.entities.Rider;
import com.san.Uber.entities.User;
import com.san.Uber.entities.enums.RideRequestStatus;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;

    private final RiderRepo riderRepo;

    private final RideFareClaculationStrategy rideFareClaculationStrategy;
    private final RideRequestRepo rideRequestRepo;
    private final DriverMatchingStrategy driverMatchingStrategy;
    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        RideRequest rideRequest = modelMapper.map(rideRequestDto,RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        Double fare = rideFareClaculationStrategy.fareCalculation(rideRequest);
        rideRequest.setFare(fare);
       RideRequest savedRideRequest =  rideRequestRepo.save(rideRequest);
        driverMatchingStrategy.findMatchingDriver(rideRequest);
        return modelMapper.map(savedRideRequest,RideRequestDto.class);

    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RideDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyrides() {
        return List.of();
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepo.save(rider);
    }
}
