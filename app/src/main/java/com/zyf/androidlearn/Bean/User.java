package com.zyf.androidlearn.Bean;

public class User {

    private String name;
    private String pwd;
    private String cardId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public User() {
    }

    public User(String name, String pwd1, String cardId) {
        this.name = name;
        this.pwd = pwd1;
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



}
