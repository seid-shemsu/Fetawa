package com.izhar.fetawa;

public class Question {
    String question;
    String username;

    public Question(String question, String username, String status) {
        this.question = question;
        this.username = username;
        this.status = status;
    }

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  Question(){}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
