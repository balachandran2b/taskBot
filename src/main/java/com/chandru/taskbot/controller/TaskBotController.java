package com.chandru.taskbot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskBotController {

    @Autowired
    TelegramBot taskBot;

    @Autowired
    private UpdatesListener taskBotUpdateListener;

    @GetMapping("/taskbot/stop")
    public ResponseEntity<String> stopTaskBot(@RequestParam String id){
        if(StringUtils.isEmpty(id) || !id.equalsIgnoreCase("normieName")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Authorized");
        }
        taskBot.removeGetUpdatesListener();
        return ResponseEntity.ok("TaskBot stopped successfully");
    }

    @GetMapping("/taskbot/start")
    public ResponseEntity<String> startTaskBot(@RequestParam String id){
        if(StringUtils.isEmpty(id) || !id.equalsIgnoreCase("normieName")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Authorized");
        }
        taskBot.setUpdatesListener(taskBotUpdateListener);
        return ResponseEntity.ok("TaskBot started successfully");
    }
}
