package com.example.experiment_automata.QuestionsModel;

import com.example.experiment_automata.UserInformation.User;

import java.util.ArrayList;


/**
 *  The goal of this class is to maintain and hold the question data.
 */

public class Question
{
    private String question;
    private User user;
    private Reply reply;// Might just have one reply... Might need fixing

    public Question(String question, User user, Reply reply)
    {
        this.question = question;
        this.user = user;
        this.reply = reply;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}