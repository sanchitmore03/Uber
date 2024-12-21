package com.san.Uber.Services;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.awt.*;
@Service
public interface DistanceService {
    double calculateDistance(org.locationtech.jts.geom.Point src, Point dist);
}
