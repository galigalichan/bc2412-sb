package com.bootcamp.bc_xfin_service.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchRecoveryRequest {
    private List<RecoveryRequest> recoveries;
}
