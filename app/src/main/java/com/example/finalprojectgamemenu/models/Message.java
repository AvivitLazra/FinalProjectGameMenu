package com.example.finalprojectgamemenu.models;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Message {
    String userName;
    String messageContent;
    String timestamp;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public Message(){
        //Required empty constructor for Firebase..
        userName = "NULL";
        messageContent= "NULL";
        timestamp = "NULL";
    }
    public Message(String userName, String messageContent) {
        this.userName = userName;
        this.messageContent = messageContent;
        this.timestamp = formatter.format(Calendar.getInstance().getTime());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
