package com.bootcamp.sb.demo_sb_bccalculator.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.sb.demo_sb_bccalculator.dto.CalRespDTO;
import com.bootcamp.sb.demo_sb_bccalculator.model.Operation;

@Component
public class DTOMapper {
    public CalRespDTO map(Double x, Double y, Operation operation, Double result) {
        return CalRespDTO.builder()
            .x(String.valueOf(x))
            .y(String.valueOf(y))
            .operation(operation.name().toLowerCase())
            .result(String.valueOf(result))
            .build();
    }
}
