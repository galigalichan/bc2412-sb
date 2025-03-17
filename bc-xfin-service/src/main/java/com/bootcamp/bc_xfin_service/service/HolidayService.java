package com.bootcamp.bc_xfin_service.service;

import java.time.LocalDate;

public interface HolidayService {
    boolean isHoliday(LocalDate date);
    
}
