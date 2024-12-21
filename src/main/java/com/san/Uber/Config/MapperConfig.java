package com.san.Uber.Config;

import com.san.Uber.Dto.PointDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Utils.GeometryUtil;
import com.san.Uber.entities.RideRequest;
import org.geolatte.geom.M;
import org.hibernate.query.results.complete.ModelPartReference;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

import java.awt.*;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper(ConfigurationPropertiesAutoConfiguration configurationPropertiesAutoConfiguration){
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(PointDto.class, Point.class).setConverter(context ->
        {
            PointDto pointDto =  context.getSource();
            return GeometryUtil.createPoint(pointDto);
        });

        mapper.typeMap(Point.class, PointDto.class).setConverter(context -> {
            Point point = context.getSource();
            double coordinates[] = {
                    point.getX(),
                    point.getY()
            };
            return new PointDto(coordinates);
        });


        return mapper;
    }
}
