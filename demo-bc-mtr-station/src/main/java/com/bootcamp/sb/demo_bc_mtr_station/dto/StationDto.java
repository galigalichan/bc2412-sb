package com.bootcamp.sb.demo_bc_mtr_station.dto;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.bootcamp.sb.demo_bc_mtr_station.exception.StationCodeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {
    private Long LineId;
    private Long id;

    @JsonDeserialize(using = StationCodeDeserializer.class) // Use custom deserializer
    private StationCode stationCode;

    private String description; // New field for description
    
    public StationDto(StationCode stationCode) {
        this.stationCode = stationCode;
        this.description = stationCode.getDescription(); // Set description from StationCode
    }
}
