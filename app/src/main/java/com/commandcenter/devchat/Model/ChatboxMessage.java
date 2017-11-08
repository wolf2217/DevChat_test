package com.commandcenter.devchat.Model;

import java.sql.Time;

/**
 * Created by Command Center on 11/8/2017.
 */

public class ChatboxMessage {

    private String User;
    private String ChatMessage;
    private Time Timestamp;

    public ChatboxMessage(String user, String chatMessage, Time timestamp) {
        User = user;
        ChatMessage = chatMessage;
        Timestamp = timestamp;
    }

    public ChatboxMessage() {
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

    public Time getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Time timestamp) {
        Timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatboxMessage{" +
                "User='" + User + '\'' +
                ", ChatMessage='" + ChatMessage + '\'' +
                ", Timestamp=" + Timestamp +
                '}';
    }
}
