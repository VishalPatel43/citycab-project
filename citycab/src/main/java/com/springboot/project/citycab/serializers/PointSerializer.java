package com.springboot.project.citycab.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.springboot.project.citycab.dto.PointDTO;
import org.locationtech.jts.geom.Point;


import java.io.IOException;

public class PointSerializer extends StdSerializer<Point> {

    public PointSerializer() {
        this(null);
    }

    public PointSerializer(Class<Point> t) {
        super(t);
    }

    @Override
    public void serialize(Point value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            PointDTO pointDTO = new PointDTO(new double[]{value.getX(), value.getY()});
            gen.writeObject(pointDTO); // Write PointDTO to JSON
        }
    }
}
