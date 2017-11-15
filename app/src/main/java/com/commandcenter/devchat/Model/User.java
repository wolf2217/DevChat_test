package com.commandcenter.devchat.Model;

/**
 * Created by Command Center on 11/9/2017.
 */

public class User {

    private String Username;
    private String Rank;
    private String Status;
    private String chatStatus;
    private String userID;

    public User() {
    }

    public User(String username, String rank, String status, String chatStatus, String userID) {
        Username = username;
        Rank = rank;
        Status = status;
        this.chatStatus = chatStatus;
        this.userID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
