package com.bootcamp.bc_xfin_service.lib;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.service.HolidayService;

@Service
public class CacheCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(CacheCleanupService.class);

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private HolidayService holidayService;

    @Scheduled(cron = "0 55 8 * * MON-FRI") // Every weekday at 08:55 AM
    public void clearCache() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Hong_Kong"));

        // Check if today is a public holiday
        if (holidayService.isHoliday(today)) {
            logger.info("Stock market is closed today (public holiday). Skipping cache cleanup.");
            return; // Skip this execution
        }

        redisManager.delete("STOCK-LIST");

        List<String> stocks = List.of("0388.HK", "0700.HK", "0005.HK", "0939.HK", "1299.HK", "1398.HK", "9988.HK", "3690.HK", "1810.HK", "0941.HK");

        for (String stock : stocks) {
            redisManager.delete("SYSDATE-" + stock);
            redisManager.delete("5MIN-" + stock);
            logger.info("Cleared system date and 5-min Redis data for: {}", stock);
        }
        logger.info("Scheduled cache cleanup executed at 08:55 AM.");
    }
}
