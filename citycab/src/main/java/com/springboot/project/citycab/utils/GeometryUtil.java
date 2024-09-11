package com.springboot.project.citycab.utils;

import com.springboot.project.citycab.dto.PointDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    public static Point createPoint(PointDTO pointDTO) {
        // 4326 is the SRID (Spatial Reference Identifier) for WGS 84 --> Earth
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
//        Coordinate coordinate = new Coordinate(pointDTO.getCoordinates()[0],
//                pointDTO.getCoordinates()[1]
//        );
        double[] coordinates = pointDTO.getCoordinates();
        Coordinate coordinate = new Coordinate(coordinates[0], coordinates[1]);

        return geometryFactory.createPoint(coordinate);
    }
}
