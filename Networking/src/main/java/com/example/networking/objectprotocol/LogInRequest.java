package com.example.networking.objectprotocol;

public class LogInRequest implements Request{
    private String username;
    private String password;

    public LogInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
