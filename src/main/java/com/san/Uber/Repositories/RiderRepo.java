package com.san.Uber.Repositories;

import com.san.Uber.entities.Rider;
import com.san.Uber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepo extends JpaRepository<Rider,Long> {
    Optional<Rider> findByUser(User user);
}
