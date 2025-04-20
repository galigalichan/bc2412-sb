package com.bootcamp.bc_xfin_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampPatchRequest {
    private Long id;
    private Long newTimestamp;

}
