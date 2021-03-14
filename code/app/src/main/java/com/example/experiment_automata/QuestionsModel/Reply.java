package com.example.experiment_automata.QuestionsModel;

import com.example.experiment_automata.UserInformation.User;

import java.io.Serializable;
import java.util.UUID;

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
    private UUID replyId;

    public Reply(String reply, User experimenter)
    {
        this.reply = reply;
        this.experimenter = experimenter;
        this.replyId = UUID.randomUUID();
    }

    public String getReply() {
        return reply;
    }

    public UUID getReplyId() {
        return replyId;
    }
}
