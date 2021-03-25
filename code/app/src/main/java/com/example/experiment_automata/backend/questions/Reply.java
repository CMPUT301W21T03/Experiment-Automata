package com.example.experiment_automata.backend.questions;

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
public class Reply implements Serializable {
    private String reply;
    private UUID experimenter;
    private UUID replyId;

    public Reply(String reply, UUID experimenter) {
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

    public UUID getUser() {
        return experimenter;
    }

    /**
     * giving us more control as to how the replies are displayed
     * @return
     *  text of the reply
     */
    @Override
    public String toString() {
        return reply;
    }
}
