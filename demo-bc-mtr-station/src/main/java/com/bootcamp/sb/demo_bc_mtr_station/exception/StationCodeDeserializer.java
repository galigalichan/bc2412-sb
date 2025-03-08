package com.bootcamp.sb.demo_bc_mtr_station.exception;

import java.io.IOException;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StationCodeDeserializer extends JsonDeserializer<StationCode> {
    @Override
    public StationCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        try {
            return StationCode.valueOf(value); // Attempt to convert to StationCode enum
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid StationCode: " + value); // Throw an IOException for invalid input
        }
    }    
}
