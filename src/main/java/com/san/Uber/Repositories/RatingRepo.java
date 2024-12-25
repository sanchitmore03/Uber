package com.san.Uber.Repositories;

import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Rating;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByRider (Rider rider);
    List<Rating> findByDriver(Driver driver);
    Optional<Rating> findByRide(Ride ride);
}
