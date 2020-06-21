package com.shibu.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    static {
        ApiContextInitializer.init();
    }
    public static void main(String[] args) {

        //ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new MyAmazingBot());
        //} catch (TelegramApiException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
