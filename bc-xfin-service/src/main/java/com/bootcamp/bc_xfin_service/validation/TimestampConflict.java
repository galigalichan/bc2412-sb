package com.bootcamp.bc_xfin_service.validation;

import java.time.LocalDate;
import java.util.Set;

public record TimestampConflict(LocalDate date, Set<Long> dbTimestamps, Set<Long> yahooTimestamps) {
    
}
