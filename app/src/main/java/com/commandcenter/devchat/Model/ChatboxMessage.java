package com.commandcenter.devchat.Model;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Command Center on 11/8/2017.
 */

public class ChatboxMessage {

    private String User;
    private String ChatMessage;
    private String Date;
    private String time;


    public ChatboxMessage() {
    }

    public ChatboxMessage(String user, String chatMessage, String date, String time) {
        User = user;
        ChatMessage = chatMessage;
        Date = date;
        this.time = time;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getChatMessage() {
        return ChatMessage;
    }

    public void setChatMessage(String chatMessage) {
        ChatMessage = chatMessage;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
