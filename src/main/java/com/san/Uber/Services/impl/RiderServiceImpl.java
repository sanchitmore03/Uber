package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Repositories.RideRequestRepo;
import com.san.Uber.Repositories.RiderRepo;
import com.san.Uber.Services.DriverService;
import com.san.Uber.Services.RideService;
import com.san.Uber.Services.RiderService;
import com.san.Uber.Strategies.RideStrategyMangaer;
import com.san.Uber.entities.*;
import com.san.Uber.entities.enums.RideRequestStatus;
import com.san.Uber.entities.enums.RideStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {



    private final ModelMapper modelMapper;

    private final RiderRepo riderRepo;

    private final RideStrategyMangaer rideStrategyMangaer;
    private final RideRequestRepo rideRequestRepo;
    private final RideService rideService;
    private final DriverService driverSrvice;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyMangaer.rideFareClaculationStrategy().fareCalculation(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepo.save(rideRequest);

        List<Driver> drivers = rideStrategyMangaer
                .driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

//        TODO : Send notification to all the drivers about this ride request

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider does not own this ride wiht id"+rideId);
        }
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new IllegalStateException("Ride cannot be cancelled. Invalid status: " + ride.getRideStatus());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverSrvice.updateDriverAvailability(ride.getDriver(),true);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RideDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider, RideDto.class);
    }

    @Override
    public Page<RideDto> getAllMyrides(PageRequest pageRequest) {
        Rider rider = getCurrentRider();
        return rideService.getAllRidesOfRider(rider,pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
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

    @Override
    public Rider getCurrentRider() {
//        TODO : implement Spring security

        return riderRepo.findById(1L).orElseThrow(() -> new ResourceNotFoundException(
                "Rider not found with id: "+1
        ));
    }
}
