package com.san.Uber.Services;

import org.locationtech.jts.geom.Point;

public interface DistanceService {
    double calculateDistance(org.locationtech.jts.geom.Point src, Point dist);
}
