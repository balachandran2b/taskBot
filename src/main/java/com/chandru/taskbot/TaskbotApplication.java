package com.chandru.taskbot;

import com.chandru.taskbot.listeners.TaskBotUpdateListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TaskbotApplication {

	public static void main(String[] args) {
		ApplicationContext  context = SpringApplication.run(TaskbotApplication.class, args);
		TelegramBot taskBot = context.getBean(TelegramBot.class);
		taskBot.setUpdatesListener(context.getBean(UpdatesListener.class));
	}

}
