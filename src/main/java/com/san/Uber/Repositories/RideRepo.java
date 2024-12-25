package com.san.Uber.Repositories;

import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RideRepo extends JpaRepository<Ride,Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);
}
