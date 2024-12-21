package com.san.Uber.Services.impl;

import com.san.Uber.Services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
    @Override
    public double calculateDistance(org.locationtech.jts.geom.Point src, Point dist) {
        return 0;
    }
}
