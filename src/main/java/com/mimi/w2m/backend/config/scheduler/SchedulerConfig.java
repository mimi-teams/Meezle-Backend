package com.mimi.w2m.backend.config.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * SchedulerConfig
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/29
 **/
@Configuration
public class SchedulerConfig implements AsyncConfigurer, SchedulingConfigurer {
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        scheduler.setThreadNamePrefix("MEEZLE_SCHEDULER-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(this.threadPoolTaskScheduler());
    }

    @Override
    public Executor getAsyncExecutor() {
        return this.threadPoolTaskScheduler();
    }
}