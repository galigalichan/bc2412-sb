package com.bootcamp.sb.demo_sb_customer.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleConfig {
    @Scheduled (cron = "0 40 17 * * MON")
    public void testCronJob() {
        System.out.println("Test Cron Job");
    }
    // @Scheduled(fixedDelay = 4000) // wait 9000 ms
    // public void sayHello() throws Exception{
    //     System.out.println(System.currentTimeMillis());
    //     Thread.sleep(5000); // Finish the task before running the process (consuming time: 9000 ms)
    //     System.out.println("Hello world!");
    // }

    // @Scheduled(fixedDelay = 2000) // 2000 ms
    // public void sayGoodBye() {
    //     System.out.println("Goodbye!");
    // }

    // @Scheduled(fixedRate = 4000) // 5000 ms
    // public void sayGoodBye() throws Exception{
    //     System.out.println(System.currentTimeMillis());
    //     Thread.sleep(5000); // task is longer than the process, so it's chasing after the task (consuming time: 5000 ms)
    //     System.out.println("ABCD!");
    // }
}
