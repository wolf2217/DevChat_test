package com.commandcenter.devchat.Model;

/**
 * Created by Command Center on 11/9/2017.
 */

public class User {

    private String Username;
    private String Rank;

    public User() {
    }

    public User(String username, String rank) {
        Username = username;
        Rank = rank;
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
}
