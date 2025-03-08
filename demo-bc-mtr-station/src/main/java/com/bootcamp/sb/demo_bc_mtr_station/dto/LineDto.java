package com.bootcamp.sb.demo_bc_mtr_station.dto;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.exception.LineCodeDeserializer;
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
public class LineDto {
    private Long id;

    @JsonDeserialize(using = LineCodeDeserializer.class) // Use custom deserializer
    private LineCode lineCode;

    private String description; // New field for description

    public LineDto(LineCode lineCode) {
        this.lineCode = lineCode;
        this.description = lineCode.getDescription(); // Set description from LineCode
    }

}
