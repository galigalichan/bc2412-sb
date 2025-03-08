package com.bootcamp.sb.demo_bc_mtr_station.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MtrResponseDto {
    @JsonProperty("sys_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sysTime;

    @JsonProperty("curr_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime currTime;

    // A flexible map to store the dynamically named station keys (e.g., "TKL-TKO")
    @JsonIgnore // Prevent serialization of the raw map (used internally)
    private Map<String, MtrScheduleData> rawData = new HashMap<>();

    @JsonProperty("isdelay")
    private String isDelay;

    private Long status;
    private String message;

    // A structured map that maps enums to data
    @JsonIgnore // Prevents Jackson from serializing it (it's just a computed field)
    private Map<LineCode, Map<StationCode, MtrScheduleData>> structuredData = new HashMap<>();

    @JsonAnySetter
    public void addDynamicKey(String key, MtrScheduleData value) {
        rawData.put(key, value);
        
        // Parsing the key (e.g., "TKL-TKO") into LineCode and StationCode
        String[] parts = key.split("-");
        if (parts.length == 2) {
            try {
                LineCode lineCode = LineCode.valueOf(parts[0]);
                StationCode stationCode = StationCode.valueOf(parts[1]);

                structuredData.computeIfAbsent(lineCode, k -> new HashMap<>()).put(stationCode, value);
            } catch (IllegalArgumentException e) {
                // If the key does not match an enum, it will be ignored
                System.err.println("Invalid line or station code: " + key);
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MtrScheduleData {
        @JsonProperty("curr_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime currTime;

        @JsonProperty("sys_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime sysTime;

        @JsonProperty("UP")
        private List<TrainInfo> up;

        @JsonProperty("DOWN")
        private List<TrainInfo> down;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainInfo {
        private String seq;
        private String dest;
        private String plat;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime time;

        private String ttnt;
        private String valid;
        private String source;
    }
}

