package me.dasha.lab7.utility;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;

    public User(String login, String password){
        this.username = login;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "username: " + username;
    }
}
