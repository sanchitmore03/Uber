package com.san.Uber.Repositories;

import com.san.Uber.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
@Repository
public interface DriverRepo extends JpaRepository<Driver,Long> {
}
