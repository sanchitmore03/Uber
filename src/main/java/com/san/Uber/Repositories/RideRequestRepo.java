package com.san.Uber.Repositories;

import com.san.Uber.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepo extends JpaRepository<RideRequest,Long> {
}
