package com.chandru.taskbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class TaskBotServiceImpl {

    @Autowired
    TelegramBot taskBot;

    private static final String PHONE_NO_REGEX ="^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";

    private static final String whatsAppMeLinkPlaceholder = "https://wa.me/+91%s";

    public void handleTasks(List<Update> updates) {
        updates.stream().forEach(update -> {
            Message updateMessage = update.message();
            String messageText = updateMessage.text();
            String messageFrom = updateMessage.chat().username();
            //TODO: only execute for normieName using above messageFrom..
            long chatId = updateMessage.chat().id();
            int replyToMessageId = updateMessage.messageId();
            handleTask(messageText, chatId, replyToMessageId);
        });
    }

    private void handleTask(String messageText, long chatId, int replyToMessageId) {
        if(StringUtils.isEmpty(messageText)){
            return;
        }
        String[] parsedMessage = messageText.split(" ");
        String replyText = "";
        if(parsedMessage.length == 1 && parsedMessage[0].matches(PHONE_NO_REGEX)){
            replyText = getWhatsAppMeLink(parsedMessage[0]);
        }else if(parsedMessage[0].equalsIgnoreCase("reply")){
            replyText = "replied to message: " + parsedMessage[1];
        }else{
            replyText = "UNSUPPORTED TASK IN CURRENT IMPLEMENTATION";
        }
        this.sendMessage(replyText, chatId, replyToMessageId);
    }

    private String getWhatsAppMeLink(String messageText) {
        return String.format(whatsAppMeLinkPlaceholder, messageText);
    }

    public void sendMessage(String messageText, long chatId, int replyMessageId){
        BaseRequest sendMessageRequest = new SendMessage(chatId, messageText);
        if(!StringUtils.isEmpty(replyMessageId)){
            ((AbstractSendRequest)sendMessageRequest).replyToMessageId(replyMessageId);
        }
        taskBot.execute(sendMessageRequest);
    }
}