package com.chandru.taskbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
            try{
                handleTask(messageText, chatId, replyToMessageId);
            }catch(Exception e){
                System.out.println("Error while executing task: " + e.getStackTrace());
            }

        });
    }

    private void handleTask(String messageText, long chatId, int replyToMessageId) throws UnknownHostException {
        if (StringUtils.isEmpty(messageText)) {
            return;
        }
        String[] parsedMessage = messageText.split(" ");
        String replyText = "";
        if (parsedMessage.length == 0) {
            return;
        }
        String taskKey = parsedMessage[0];
        String restOfMessage = Arrays.stream(parsedMessage).skip(1).reduce("", (fm, sm) -> fm + " " + sm);
        if (parsedMessage.length == 1 && taskKey.matches(PHONE_NO_REGEX)) {
            replyText = getWhatsAppMeLink(parsedMessage[0]);
        } else if (parsedMessage[0].equalsIgnoreCase("reply")) {
            replyText = "replied to message: " + restOfMessage;
        }else if(parsedMessage[0].equalsIgnoreCase("test")){
            replyText = "This bot message is from the IP: " + InetAddress.getLocalHost();
        }else{
            replyText = "UNSUPPORTED TASK IN CURRENT IMPLEMENTATION";
        }
        this.sendMessage(replyText, chatId, replyToMessageId, null);
    }

    private String getWhatsAppMeLink(String messageText) {
        return String.format(whatsAppMeLinkPlaceholder, messageText);
    }

    public void sendMessage(String messageText, long chatId, int replyMessageId, ParseMode parseMode){
        SendMessage sendMessageRequest = new SendMessage(chatId, messageText);
        if(replyMessageId != -1){
            sendMessageRequest.replyToMessageId(replyMessageId);
        }
        if(Objects.nonNull(parseMode)){
            sendMessageRequest.parseMode(parseMode);
        }
        taskBot.execute(sendMessageRequest);
    }
}
