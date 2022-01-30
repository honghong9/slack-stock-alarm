package kr.slack.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableAsync
public class TaskSchedulerTest {

    /**
     * runs again 1000 ms after the previous task finishes
     */
    @Scheduled(fixedDelay = 1000)
    @Test
    public void scheduleFixedDelayTask() throws InterruptedException {
        log.info("Fixed delay task - {}" + (System.currentTimeMillis() / 1000));
        Thread.sleep(10000);
    }

    /**
     *  runs every milliseconds but the next task won't be executed by the current one finishes
     *  can run in parallel with @Async
     */
    @Async
    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() throws InterruptedException {
        log.info("Fixed rate task - {}", System.currentTimeMillis() / 1000);
        Thread.sleep(5000);
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Fixed rate task with one second initial delay -{}", now);
    }

    /**
     *  Cron Expression
     *  second(0-59), minute(0-59), hour(0-23), date(1-31), month(1-12), day(0-7)
     */
    @Scheduled(cron = "0 15 10 15 * ?")
    public void scheduleTaskUsingCronExpression() {
        long now = System.currentTimeMillis() / 1000;
        log.info("schedule tasks using cron jobs - {}", now);
    }
}