package com.bootcamp.sb.demo_bc_mtr_station.exception;

import java.io.IOException;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LineCodeDeserializer extends JsonDeserializer<LineCode> {
    @Override
    public LineCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        try {
            return LineCode.valueOf(value); // Attempt to convert to LineCode enum
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid LineCode: " + value); // Throw an IOException for invalid input
        }
    }
}