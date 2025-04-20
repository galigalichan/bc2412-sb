package com.bootcamp.bc_xfin_service.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ValidationScheduler {
    private final DataValidatorRunner dataValidatorRunner;

    public ValidationScheduler(DataValidatorRunner dataValidatorRunner) {
        this.dataValidatorRunner = dataValidatorRunner;
    }

    @Scheduled(cron = "0 0 3 * * TUE-SAT", zone = "Asia/Hong_Kong")
    public void runDailyValidation() {
        dataValidatorRunner.runValidation();
    }
}
