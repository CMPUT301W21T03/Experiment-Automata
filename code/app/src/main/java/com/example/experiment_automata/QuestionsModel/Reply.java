package com.example.experiment_automata.QuestionsModel;

import com.example.experiment_automata.UserInformation.User;

import java.io.Serializable;

/**
 * Role/Pattern:
 *
 *       The goal of this class is to maintain a questions replies
 *
 * Known Issue:
 *
 *      1. None
 */
public class Reply implements Serializable
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
