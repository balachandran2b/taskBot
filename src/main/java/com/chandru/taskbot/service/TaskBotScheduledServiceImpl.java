package com.chandru.taskbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TaskBotScheduledServiceImpl {

    @Autowired
    private TelegramBot taskBot;

    @Value("${taskbotProps.normieNameChatId:1075392617}")
    private long normieNameChatId;

    @Autowired
    private TaskBotServiceImpl taskBotService;

    private List<String> quotesListFromFile = new ArrayList<>();

    private final int TOTAL_NO_OF_QUOTES = 1665;

    private int previousDayQuoteId = 0;

    @Scheduled(cron = "0 30 1 * * *")
    private void sendMorningAffirmations(){
        String greetingsText = "Good Morning Chandru, Wake up and shine!!! \n";
        String quoteForTheDay = getQuote();
        //TODO: issue with parse mode so currently using unformatted message.
        String messageText = greetingsText + quoteForTheDay; //getHTMLFormattedText(greetingsText, "header") + quoteForTheDay;
        taskBotService.sendMessage(messageText, normieNameChatId, -1, null);
    }

    private String getQuote() {
        int randomInteger = -1;
        do{
            randomInteger = ThreadLocalRandom.current().nextInt(1, TOTAL_NO_OF_QUOTES);
        }while(randomInteger == this.previousDayQuoteId);
        String quoteLine = quotesListFromFile.get(randomInteger);
        String author = quoteLine.split(",")[0];
        String quote = Arrays.stream(quoteLine.split(",")).skip(1).reduce("", (fm, sm) -> fm + "," + sm);
        if(StringUtils.isEmpty(author)){
            author = "Anonymous";
        }
        String quoteText = String.format("%1s \n\n ~ %2s", quote, author);
                //getHTMLFormattedText(quote, "quote"),
                //getHTMLFormattedText(author, "author"));
        this.previousDayQuoteId = randomInteger;
        return quoteText;
    }

    public String getHTMLFormattedText(String text, String formatType){
        switch (formatType){
            case "header":
                return String.format("<h2 style=\"color: #5e9ca0;\">%s</h2>", text);
            case "quote":
                return String.format("<pre><strong>%s</strong></pre>", text);
            case "author":
                return String.format("<em>%s</em>", text);
            default:
                return text;
        }
    }

    //INIT BLOCK to initialize the quotes List;
    {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("datasets/quotes_main.csv");
        try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            String line;
            while((line = bufferedReader.readLine()) != null){
                quotesListFromFile.add(line);
            }
        }catch(Exception exception){
            System.out.println("Error reading from file" + exception.getMessage());
        }
    }
}
