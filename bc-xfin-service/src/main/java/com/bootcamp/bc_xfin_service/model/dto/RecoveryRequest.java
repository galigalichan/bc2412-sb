package com.bootcamp.bc_xfin_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryRequest {
    private String symbol;
    private long startEpoch;
    private long endEpoch;
}
