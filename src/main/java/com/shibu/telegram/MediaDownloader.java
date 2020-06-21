package com.shibu.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.*;
import java.net.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import com.cedarsoftware.util.io.JsonWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MediaDownloader {
    public static String MESSAGES_FOLDER = "messages_out";
    public static String MEDIA_FOLDER    = "media_out";
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss:SSS Z");
    public static SimpleDateFormat MessageFileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
	private static final JSONParser parser = new JSONParser();
    
    /************************************************************************************************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
    public static void writeToJson(Update update) {
        try {
            Date today = new Date();
            java.io.File messagesFolder = new java.io.File(MESSAGES_FOLDER);
            messagesFolder.mkdirs();
            String formattedDate = DATE_FORMAT.format(today);
            String fileName = MessageFileNameFormat.format(today)+".json.txt";
            FileOutputStream fos = new FileOutputStream(new java.io.File(messagesFolder.getName()+"/"+fileName), true);

            fos.write(("======================================\n").getBytes());
            String json1 = JsonWriter.objectToJson(update.getMessage());
            String json2 = JsonWriter.formatJson(json1);
            fos.write((json2+"\n").getBytes());
            //fos.write(("toString="+update.getMessage().toString()+"\n").getBytes());
            fos.write(("======================================\n").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /************************************************************************************************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
    public static void downloadPhoto(Update update) {
        try {
            Date today = new Date();
            java.io.File mediaFolder = new java.io.File(MEDIA_FOLDER);
            mediaFolder.mkdirs();

            System.out.println("Has photo");
            List<PhotoSize> photoList = update.getMessage().getPhoto();
            System.out.println(photoList.toString());
            int fileSize=0;
            String fileId = photoList.get(0).getFileId();
            String fileUniqueId = photoList.get(0).getFileUniqueId();
            for(PhotoSize photo: photoList ) { // in this loop, get fileId of the lasrgest Image
                if(photo.getFileSize() > fileSize) {
                    fileSize = photo.getFileSize();
                    fileId = photo.getFileId();
                    fileUniqueId = photo.getFileUniqueId();
                }
            }
            
            String url = "https://api.telegram.org/bot"+BotToken.BOT_TOKEN+"/getFile?file_id="+fileId;
            String jsonString = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
            System.out.println(jsonString);
            JSONObject resp = (JSONObject) parser.parse(jsonString);
            System.out.println(resp);
            String json3 = JsonWriter.objectToJson(resp);
            String json4 = JsonWriter.formatJson(json3);
            System.out.println("resp="+json4);


            String filePath = (String)((JSONObject)resp.get("result")).get("file_path");
            System.out.println("filePath="+filePath);
            String localFilePath = filePath.replaceAll("/","__");
            Supplier<String> botTokenSupplier = () -> new String(BotToken.BOT_TOKEN);
            TelegramFileDownloader dl =  new TelegramFileDownloader(botTokenSupplier);
            dl.downloadFile(filePath, new java.io.File(MEDIA_FOLDER+"/"+MessageFileNameFormat.format(today)+"__"+fileUniqueId+"_-_"+localFilePath));
            
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /************************************************************************************************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
    public static void downloadDocument(Update update) {
        try {
            Date today = new Date();
            java.io.File mediaFolder = new java.io.File(MEDIA_FOLDER);
            mediaFolder.mkdirs();

            System.out.println("Has document");
            Document doc = update.getMessage().getDocument();
            System.out.println(doc.toString());
            int fileSize=0;
            String fileId = doc.getFileId();
            String fileUniqueId = doc.getFileUniqueId();
            String fileName = doc.getFileName();
            
            String url = "https://api.telegram.org/bot"+BotToken.BOT_TOKEN+"/getFile?file_id="+fileId;
            String jsonString = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
            System.out.println(jsonString);
            JSONObject resp = (JSONObject) parser.parse(jsonString);
            System.out.println(resp);
            String json3 = JsonWriter.objectToJson(resp);
            String json4 = JsonWriter.formatJson(json3);
            System.out.println("resp="+json4);


            String filePath = (String)((JSONObject)resp.get("result")).get("file_path");
            System.out.println("filePath="+filePath);
            Supplier<String> botTokenSupplier = () -> new String(BotToken.BOT_TOKEN);
            TelegramFileDownloader dl =  new TelegramFileDownloader(botTokenSupplier);
            dl.downloadFile(filePath, new java.io.File(MEDIA_FOLDER+"/"+fileUniqueId+"_-_"+fileName));
            
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /************************************************************************************************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
}
