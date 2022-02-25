package com.chandru.taskbot.listeners;

import com.chandru.taskbot.service.TaskBotServiceImpl;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskBotUpdateListener implements UpdatesListener {

    @Autowired
    TaskBotServiceImpl taskBotServiceImpl;

    @Override
    public int process(List<Update> updates) {
        taskBotServiceImpl.handleTasks(updates);
        return -1;
    }
}
