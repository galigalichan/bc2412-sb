package com.bootcamp.bc_xfin_service.service.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.service.HolidayService;

@Service
public class HolidayServiceImpl implements HolidayService {
    private Set<LocalDate> holidays = new HashSet<>();

    public HolidayServiceImpl() {
        holidays.add(LocalDate.of(2025, 1, 1)); // New Year's Day
        holidays.add(LocalDate.of(2025, 1, 29)); // Lunar New Year’s Day
        holidays.add(LocalDate.of(2025, 1, 30)); // The second day of Lunar New Year
        holidays.add(LocalDate.of(2025, 1, 31)); // The third day of Lunar New Year
        holidays.add(LocalDate.of(2025, 4, 4)); // Ching Ming Festival
        holidays.add(LocalDate.of(2025, 4, 18)); // Good Friday
        holidays.add(LocalDate.of(2025, 4, 19)); // The day following Good Friday
        holidays.add(LocalDate.of(2025, 4, 21)); // Easter Monday
        holidays.add(LocalDate.of(2025, 5, 1)); // Labour Day
        holidays.add(LocalDate.of(2025, 5, 5)); // The Birthday of the Buddha
        holidays.add(LocalDate.of(2025, 5, 31)); // Tuen Ng Festival
        holidays.add(LocalDate.of(2025, 7, 1)); // Hong Kong Special Administrative Region Establishment Day
        holidays.add(LocalDate.of(2025, 10, 1)); // National Day
        holidays.add(LocalDate.of(2025, 10, 7)); // The day following the Chinese Mid-Autumn Festival
        holidays.add(LocalDate.of(2025, 10, 29)); // Chung Yeung Festival
        holidays.add(LocalDate.of(2025, 12, 25)); // Christmas Day
        holidays.add(LocalDate.of(2025, 12, 26)); // The first weekday after Christmas Day
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }    
}
