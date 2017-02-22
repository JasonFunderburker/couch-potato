package com.jasonfunderburker.couchpotato;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Created on 22.02.2017
 *
 * @author JasonFunderburker
 */
@Configuration
public class TaskSchedulerConfig {

    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }
}
