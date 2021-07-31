package com.izhar.fetawa;

public class Answer {
    String id, question, answer;

    public Answer(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public Answer(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public Answer(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
