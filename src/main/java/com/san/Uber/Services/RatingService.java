package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Rating;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Rider;

import java.util.Optional;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);


}
