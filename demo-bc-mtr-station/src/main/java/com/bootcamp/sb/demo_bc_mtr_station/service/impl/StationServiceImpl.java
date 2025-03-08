package com.bootcamp.sb.demo_bc_mtr_station.service.impl;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.codewave.SysCode;
import com.bootcamp.sb.demo_bc_mtr_station.dto.NextTrainDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.StationDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.LineEntity;
import com.bootcamp.sb.demo_bc_mtr_station.entity.StationEntity;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.bootcamp.sb.demo_bc_mtr_station.exception.InvalidInputException;
import com.bootcamp.sb.demo_bc_mtr_station.repository.LineRepository;
import com.bootcamp.sb.demo_bc_mtr_station.repository.StationRepository;
import com.bootcamp.sb.demo_bc_mtr_station.service.StationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ApiResp<StationDto> addStation(String id, StationDto stationDto) {
        Long lineId;

        try {
            lineId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        // Retrieve the LineEntity by lineId
        LineEntity line = lineRepository.findById(lineId)
            .orElseThrow(() -> new InvalidInputException(SysCode.LINE_NOT_FOUND)); // Handle line not found

        StationEntity station = new StationEntity();
        station.setLine(line);
        station.setStationCode(stationDto.getStationCode());
        station.setDescription(stationDto.getStationCode().getDescription());

        stationRepository.save(station);

        // Create a StationDto with lineId
        StationDto responseDto = new StationDto(line.getId(), station.getId(), station.getStationCode(), station.getStationCode().getDescription());

        return ApiResp.<StationDto>builder().syscode(SysCode.STATION_ADD_SUCCESS).data(responseDto).build();

    }    

    @Override
    public ApiResp<StationDto> getStationById(String id) {
        Long stationId;

        try {
            stationId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        StationEntity station = stationRepository.findById(stationId)
            .orElseThrow(() -> new InvalidInputException(SysCode.STATION_NOT_FOUND));

        StationDto responseDto = new StationDto(station.getLineId(), station.getId(), station.getStationCode(), station.getStationCode().getDescription());

        return ApiResp.<StationDto>builder().syscode(SysCode.SUCCESS).data(responseDto).build();

    }

    @Override
    public ApiResp<List<StationDto>> getStationByCode(String stationCode) {
        StationCode validStationCode;

        try {
            validStationCode = StationCode.valueOf(stationCode);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        List<StationEntity> stations = stationRepository.findAll();

        List<StationDto> targetStations = stations.stream().filter(e -> e.getStationCode().equals(validStationCode)).map(station -> new StationDto(station.getLineId(), station.getId(), station.getStationCode(), station.getStationCode().getDescription())).collect(Collectors.toList());

        if (targetStations.isEmpty()) {
            throw new InvalidInputException(SysCode.STATION_NOT_FOUND); // Handle not found scenario
        }

        return ApiResp.<List<StationDto>>builder().syscode(SysCode.SUCCESS).data(targetStations).build();

    }

    @Override
    // Method to get all stations grouped by LineCode
    public ApiResp<Map<LineCode, List<StationDto>>> getAllStations() {
        Map<LineCode, List<StationDto>> stationMap = new HashMap<>();
        
        List<StationEntity> stations = stationRepository.findAll();

        // Loop through each station, extract LineCode, and use computeIfAbsent() to create a new list if the key doesn't exist.
        for (StationEntity station : stations) {
            LineCode lineCode = station.getLine() != null ? station.getLine().getLineCode() : null; 
            
            // Add each station to the corresponding list in the map.
            if (lineCode != null) {
                // Convert StationEntity to StationDto
                StationDto stationDto = new StationDto(station.getLineId(), station.getId(), station.getStationCode(), station.getStationCode().getDescription());
                // Explicitly specify the type parameter for ArrayList
                stationMap.computeIfAbsent(lineCode, k -> new ArrayList<StationDto>()).add(stationDto);
            }
        }
    
        return ApiResp.<Map<LineCode, List<StationDto>>>builder()
            .syscode(SysCode.SUCCESS)
            .data(stationMap)
            .build();
    }

    @Override
    // Method to get stations by LineCode
    public ApiResp<List<StationDto>> getStationsByLine(String lineCode) {
        LineCode validLineCode;

        try {
            validLineCode = LineCode.valueOf(lineCode);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        List<StationEntity> stations = stationRepository.findAll();

        // Filter the list of stations based on the provided LineCode
        List<StationDto> filteredStations = stations.stream()
                .filter(station -> station.getLine() != null && station.getLine().getLineCode().equals(validLineCode))
                .map(station -> new StationDto(station.getLineId(), station.getId(), station.getStationCode(), station.getStationCode().getDescription()))
                .collect(Collectors.toList());

        // Throw an exception if no stations are found for the provided lineCode
        if (filteredStations.isEmpty()) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
    }

        // Return the filtered list wrapped in an ApiResp
        return ApiResp.<List<StationDto>>builder()
                .syscode(SysCode.SUCCESS)
                .data(filteredStations)
                .build();
    }


    // private String makeApiCall(String urlString) throws Exception {
    //     StringBuilder result = new StringBuilder();
    //     URL url = new URL(urlString);
    //     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //     conn.setRequestMethod("GET");
    //     conn.setRequestProperty("Accept", "application/json");

    //     // Check for HTTP response code
    //     int responseCode = conn.getResponseCode();
    //     if (responseCode == HttpURLConnection.HTTP_OK) { // 200
    //         try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 result.append(line);
    //             }
    //         }
    //     } else {
    //         throw new Exception("Failed to get response from API: " + responseCode);
    //     }

    //     return result.toString();
    // }

   @Override
    public NextTrainDto getNextTrain(String line, String station) {
        LineCode validLine;

        try {
            validLine = LineCode.valueOf(line);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        StationCode validStation;

        try {
            validStation = StationCode.valueOf(station);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }
        
        String url = "https://rt.data.gov.hk/v1/transport/mtr/getSchedule.php?line=" + validLine + "&sta=" + validStation;

        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode stationData = root.path("data").path(validLine + "-" + validStation);
    
            if (stationData.isMissingNode()) {
                return new NextTrainDto(null, null, validStation, List.of());
            }
    
            LocalDateTime currTime = LocalDateTime.parse(root.path("curr_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime sysTime = LocalDateTime.parse(root.path("sys_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
            List<NextTrainDto.TrainInfo> trains = new ArrayList<>();
            processTrains(stationData.path("UP"), trains, "UP");
            processTrains(stationData.path("DOWN"), trains, "DOWN");
    
            return new NextTrainDto(currTime, sysTime, validStation, trains);
    
        } catch (Exception e) {
            e.printStackTrace();
            return new NextTrainDto(null, null, validStation, List.of());
        }
    }

    private void processTrains(JsonNode trainArray, List<NextTrainDto.TrainInfo> trains, String direction) {
        Set<String> seenDestinations = new HashSet<>(); // Track first occurrence for each destination
        for (JsonNode train : trainArray) {
            String destination = train.path("dest").asText();
            if (!seenDestinations.contains(destination)) { // Pick the first occurrence only
                seenDestinations.add(destination);
                trains.add(new NextTrainDto.TrainInfo(
                    destination,
                    train.path("time").asText(),
                    direction
                ));
            }
        }
    }

    // public NextTrainDto getNextTrain(String line, String station) {
    //     String url = "https://rt.data.gov.hk/v1/transport/mtr/getSchedule.php?line=" + line + "&sta=" + station;
        
    //     try {
    //         // Fetch JSON response as a String
    //         String response = restTemplate.getForObject(url, String.class);
            
    //         // Parse JSON
    //         JsonNode root = objectMapper.readTree(response);
    
    //         // Extract root-level times
    //         LocalDateTime currTime = LocalDateTime.parse(root.path("curr_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    //         LocalDateTime sysTime = LocalDateTime.parse(root.path("sys_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
    //         JsonNode stationData = root.path("data").path(line + "-" + station);
    //         if (stationData.isMissingNode()) {
    //             return new NextTrainDto(currTime, sysTime, StationCode.valueOf(station).name(), new HashMap<>());
    //         }
    
    //         Map<String, NextTrainDto.TrainInfo> nextTrains = new HashMap<>();
    //         processTrains(stationData.path("UP"), nextTrains);
    //         processTrains(stationData.path("DOWN"), nextTrains);
    
    //         return new NextTrainDto(currTime, sysTime, StationCode.valueOf(station).name(), nextTrains);
    
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new NextTrainDto(null, null, StationCode.valueOf(station).name(), new HashMap<>());
    //     }
    // }

    // private void processTrains(JsonNode trainArray, Map<String, NextTrainDto.TrainInfo> nextTrains) {
    //     for (JsonNode train : trainArray) {
    //         String destination = train.path("dest").asText();
    //         if (!nextTrains.containsKey(destination)) { // Pick the first occurrence only
    //             nextTrains.put(destination, new NextTrainDto.TrainInfo(
    //                 destination,
    //                 train.path("time").asText(),
    //                 train.path("plat").asText()
    //             ));
    //         }
    //     }
    // }
    
}
