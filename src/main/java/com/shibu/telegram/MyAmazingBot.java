package com.shibu.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAmazingBot extends TelegramLongPollingBot {
    
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss:SSS Z");
    @Override
    public void onUpdateReceived(Update update) {
        try {
            MediaDownloader.writeToJson(update);
            if (update.hasMessage() && update.getMessage().hasPhoto()) {
                System.out.println("Has photo");
                MediaDownloader.downloadPhoto(update);
            }
            if (update.hasMessage() && update.getMessage().hasDocument()) {
                System.out.println("Has document");
                MediaDownloader.downloadDocument(update);
            }

            // We check if the update has a message and the message has text
            if (update.hasMessage() && update.getMessage().hasText()) {
                Date today = new Date();
                String formattedDate = DATE_FORMAT.format(today);
                String printString = formattedDate + " : "+update.getMessage().getText();
                System.out.println(printString);
                SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message = message.setChatId(update.getMessage().getChatId());
                message = message.setText("Yukthi also says ..."+update.getMessage().getText());
                //message = message.setText("podee ...");
                
                //execute(message); // Call method to send the message
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BotToken.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotToken.BOT_TOKEN;
    }
}
