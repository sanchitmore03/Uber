package com.san.Uber.Repositories;

import com.san.Uber.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface RiderRepo extends JpaRepository<Rider,Long> {
}
