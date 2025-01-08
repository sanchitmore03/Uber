package com.san.Uber.Repositories;

import com.san.Uber.entities.RideRequest;
import com.san.Uber.entities.enums.RideRequestStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideRequestRepo extends JpaRepository<RideRequest,Long> {
    Optional<Object> findByStatus(RideRequestStatus rideRequestStatus, PageRequest pageRequest);
}
