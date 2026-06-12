package com.ex01.basic.dto;

public class BasicDTO {
    private String username;
    private String password;
    private int number;
    public BasicDTO(String username, String password, int number){
        this.username = username;
        this.password = password;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
