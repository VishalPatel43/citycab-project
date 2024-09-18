package com.springboot.project.citycab.configs;

import com.springboot.project.citycab.dto.PointDTO;
import com.springboot.project.citycab.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    // Not able to Convert PointDto to Point
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // context is there is some information about the conversion
        // PointDTO to Point

        mapper.typeMap(PointDTO.class, Point.class).setConverter(context -> {
            PointDTO pointDTO = context.getSource();
            return GeometryUtil.createPoint(pointDTO);
        });

        // Point to PointDTO
        mapper.typeMap(Point.class, PointDTO.class).setConverter(context -> {
            Point point = context.getSource();

            double[] coordinates = {
                    point.getX(),
                    point.getY()
            };
            return new PointDTO(coordinates);
        });

        // If there is no Point or PointDTO then direct return the mapper
        // If there is Point or PointDTO then return the mapper with the conversion
        return mapper;
    }
}
