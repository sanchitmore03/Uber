package com.san.Uber.Services.impl;


import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Exceptions.RuntimeConflictException;
import com.san.Uber.Repositories.DriverRepo;
import com.san.Uber.Repositories.RatingRepo;
import com.san.Uber.Repositories.RideRepo;
import com.san.Uber.Repositories.RiderRepo;
import com.san.Uber.Services.RatingService;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Rating;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Rider;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;




@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepo ratingRepository;
    private final DriverRepo  driverRepository;
    private final RiderRepo  riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: "+ride.getId()));

        if(ratingObj.getDriverRating() != null)
            throw new RuntimeConflictException("Driver has already been rated, cannot rate again");

        ratingObj.setDriverRating(rating);

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);
        driver.setRating(newRating);

        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: "+ride.getId()));
        if(ratingObj.getRiderRating() != null)
            throw new RuntimeConflictException("Rider has already been rated, cannot rate again");

        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getRiderRating)
                .average().orElse(0.0);
        rider.setRating(newRating);

        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
