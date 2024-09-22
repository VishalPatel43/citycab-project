package com.springboot.project.citycab.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class PointSerializer2 extends JsonSerializer<Point> {

    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        if (point != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("coordinates");
            jsonGenerator.writeArray(new double[]{point.getX(), point.getY()}, 0, 2);
            jsonGenerator.writeFieldName("srid");
            jsonGenerator.writeNumber(point.getSRID());
            jsonGenerator.writeFieldName("type");
            jsonGenerator.writeString("Point");
            jsonGenerator.writeEndObject();
        }
    }
}
