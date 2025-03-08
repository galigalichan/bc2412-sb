package com.bootcamp.sb.demo_bc_mtr_station.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_bc_mtr_station.codewave.ApiResp;
import com.bootcamp.sb.demo_bc_mtr_station.codewave.SysCode;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineDto;
import com.bootcamp.sb.demo_bc_mtr_station.dto.LineSignalDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.LineEntity;
import com.bootcamp.sb.demo_bc_mtr_station.entity.StationEntity;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.bootcamp.sb.demo_bc_mtr_station.exception.InvalidInputException;
import com.bootcamp.sb.demo_bc_mtr_station.exception.NullInputException;
import com.bootcamp.sb.demo_bc_mtr_station.repository.LineRepository;
import com.bootcamp.sb.demo_bc_mtr_station.repository.StationRepository;
import com.bootcamp.sb.demo_bc_mtr_station.service.LineService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LineServiceImpl implements LineService {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ApiResp<LineDto> addLine(LineDto lineDto) {
        LineEntity line = new LineEntity();
        
        // Validate LineCode
        if (lineDto.getLineCode() == null) {
            throw new NullInputException(SysCode.INVALID_INPUT); // Handle null case
        }

        try {
            line.setLineCode(lineDto.getLineCode()); // This will work if lineCode is valid
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT); // Catch invalid enum value
        }

        line.setDescription(lineDto.getLineCode().getDescription());
        lineRepository.save(line);

        LineDto responseDto = new LineDto(line.getId(), line.getLineCode(), line.getLineCode().getDescription());

        return ApiResp.<LineDto>builder().syscode(SysCode.LINE_ADD_SUCCESS).data(responseDto).build();
    }

   @Override
    public LineSignalDto getLineSignal(String line) {
        LineCode validLine;

        try {
            validLine = LineCode.valueOf(line);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(SysCode.INVALID_INPUT);
        }

        List<StationEntity> stations = stationRepository.findAll();
        List<StationCode> filteredStations = stations.stream().filter(station -> station.getLine().getLineCode().equals(validLine)).map(station -> station.getStationCode()).collect(Collectors.toList());
        
        Integer delayStationCount = 0;
        List<StationCode> delayStations = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime systemTime = LocalDateTime.now();

        for(StationCode station : filteredStations) {
            String url = "https://rt.data.gov.hk/v1/transport/mtr/getSchedule.php?line=" + validLine + "&sta=" + station;

            String response = restTemplate.getForObject(url, String.class);

            try {
                JsonNode root = objectMapper.readTree(response);

                String status = root.path("isdelay").asText();
                    if (status.equals("Y")) {
                        delayStationCount++;
                        delayStations.add(station);
                    }

                LocalDateTime currTime = LocalDateTime.parse(root.path("curr_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                currentTime = currTime;

                LocalDateTime sysTime = LocalDateTime.parse(root.path("sys_time").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                systemTime = sysTime;

            } catch (Exception e) {
                e.printStackTrace();
                return new LineSignalDto(null, null, List.of(), null, null);
            }

        }

        String signal;
        if (delayStationCount == 0) {
            signal = "GREEN";
        } else if (delayStationCount == 1) {
            signal = "YELLOW";
        } else {
            signal = "RED";
        }

        return new LineSignalDto(validLine, signal, delayStations, currentTime, systemTime);

    }

    @Override
    public List<LineSignalDto> getAllLineSignals() {
        List<LineEntity> lines = lineRepository.findAll();
        List<LineCode> lineCodes = lines.stream().map(line -> line.getLineCode()).collect(Collectors.toList());

        List<LineSignalDto> responseDto = new ArrayList<>();

        for(LineCode linecode : lineCodes) {
            String line = String.valueOf(linecode);
            LineSignalDto lineSignalDto = getLineSignal(line);
            responseDto.add(lineSignalDto);
        }

        return responseDto;

    }

}
