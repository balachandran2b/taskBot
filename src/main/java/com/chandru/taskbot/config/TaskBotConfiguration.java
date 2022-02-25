package com.chandru.taskbot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskBotConfiguration {

    @Value("${taskBotProps.taskBotToken}")
    String taskBotToken;

    @Bean
    public TelegramBot getTaskBot(){
        return new TelegramBot(taskBotToken);
    }

}
