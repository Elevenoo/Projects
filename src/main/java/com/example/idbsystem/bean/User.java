package com.example.idbsystem.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;

    public String getUsername(String username) {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
