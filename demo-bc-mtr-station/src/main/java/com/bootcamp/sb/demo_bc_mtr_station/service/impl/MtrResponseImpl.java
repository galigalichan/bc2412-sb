package com.bootcamp.sb.demo_bc_mtr_station.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Service;

import com.bootcamp.sb.demo_bc_mtr_station.dto.MtrResponseDto;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;
import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MtrResponseImpl {
    
    private static final String API_URL = "https://rt.data.gov.hk/v1/transport/mtr/getSchedule.php?line=%s&sta=%s";

    private String makeApiCall(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        // Check for HTTP response code
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
        } else {
            throw new Exception("Failed to get response from API: " + responseCode);
        }

        return result.toString();
    }

    // public MtrScheduleData getNextTrain(LineCode lineCode, StationCode stationCode) {
    //     String url = String.format(API_URL, lineCode, stationCode);
        
    //     try {
    //         // Make API call
    //         String response = makeApiCall(url);
    //         System.out.println("Received JSON: " + response); // Debugging
    
    //         // Parse response
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         MtrResponseDto dto = objectMapper.readValue(response, MtrResponseDto.class);
    
    //         // Check if structuredData is null
    //         if (dto.getStructuredData() == null) {
    //             throw new RuntimeException("API response did not contain structured data.");
    //         }
    
    //         // First, get the map for the given lineCode
    //         Map<StationCode, MtrScheduleData> stationMap = dto.getStructuredData().get(lineCode);
    //         if (stationMap == null) {
    //             throw new RuntimeException("No station data found for line: " + lineCode);
    //         }
    
    //         // Then, get the schedule data for the given stationCode
    //         MtrScheduleData scheduleData = stationMap.get(stationCode);
    //         if (scheduleData == null) {
    //             throw new RuntimeException("No schedule data found for station: " + stationCode + " on line: " + lineCode);
    //         }
    
    //         return scheduleData;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    public MtrResponseDto getTrainSchedule(LineCode lineCode, StationCode stationCode) {
        String url = String.format(API_URL, lineCode, stationCode);
        
        try {
            // Make API call
            String response = makeApiCall(url);
            ObjectMapper objectMapper = new ObjectMapper();
            MtrResponseDto dto = objectMapper.readValue(response, MtrResponseDto.class);
    
            // Return the full DTO (avoid extracting just MtrScheduleData)
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }   

}

