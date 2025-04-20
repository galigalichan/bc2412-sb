package com.bootcamp.bc_xfin_service.lib;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class IsoWeekGroupingTest {

    public static void main(String[] args) {
        List<Long> exampleTimestamps = List.of(
            toTimestamp("2025-03-31"), // Monday
            toTimestamp("2025-04-01"), // Tuesday
            toTimestamp("2025-04-03"), // Thursday
            toTimestamp("2025-04-05"), // Saturday
            toTimestamp("2025-04-07"), // Next Monday
            toTimestamp("2025-04-10")  // Thursday of next week
        );

        WeekFields weekFields = WeekFields.ISO;

        Map<String, List<LocalDate>> grouped = exampleTimestamps.stream()
            .map(ts -> Instant.ofEpochSecond(ts).atZone(ZoneOffset.UTC).toLocalDate())
            .collect(Collectors.groupingBy(date -> {
                int week = date.get(weekFields.weekOfWeekBasedYear());
                int year = date.get(weekFields.weekBasedYear());
                return year + "-W" + String.format("%02d", week);
            }));

        // Print the grouped result
        grouped.forEach((weekKey, dates) -> {
            System.out.println("Week: " + weekKey);
            dates.forEach(d -> System.out.println("  " + d));
        });
    }

    private static long toTimestamp(String dateString) {
        return LocalDate.parse(dateString)
            .atStartOfDay(ZoneOffset.UTC)
            .toEpochSecond();
    }
}
