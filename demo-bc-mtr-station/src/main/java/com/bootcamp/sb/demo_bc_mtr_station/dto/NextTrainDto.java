package com.bootcamp.sb.demo_bc_mtr_station.dto;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NextTrainDto {
    @JsonProperty("curr_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime currTime;

    @JsonProperty("sys_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sysTime;

    @JsonProperty("currentStation")
    private StationCode currentStation;

    @JsonProperty("trains")
    private List<TrainInfo> trains;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainInfo {
        @JsonProperty("destination")
        private String destination;

        @JsonProperty("arrivalTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private String arrivalTime;

        @JsonProperty("direction")
        private String direction;

    }

}



