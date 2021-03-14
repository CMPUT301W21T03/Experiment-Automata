package com.example.experiment_automata.QuestionsModel;

import com.example.experiment_automata.UserInformation.User;

public class Reply
{
    private String reply;
    private User experimenter;

    public Reply(String reply, User experimenter)
    {
        this.reply = reply;
        this.experimenter = experimenter;
    }

    public String getReply() {
        return reply;
    }
}
