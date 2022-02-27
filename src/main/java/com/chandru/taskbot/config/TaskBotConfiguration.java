package com.chandru.taskbot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TaskBotConfiguration {

    @Value("${taskBotProps.taskBotToken}")
    String taskBotToken;

    @Bean
    public TelegramBot getTaskBot(){
        return new TelegramBot(taskBotToken);
    }

}
