package com.bootcamp.sb.demo_bc_mtr_station.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class LineSignalDto {
    @JsonProperty("line")
    private LineCode line;
   
    @JsonProperty("signal")
    private String signal;

    @JsonProperty("delayStation")
    private List<StationCode> stations;

    @JsonProperty("curr_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime currTime;

    @JsonProperty("sys_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sysTime;

}
