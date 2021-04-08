package com.example.experiment_automata;

import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.Reply;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReplyTest {
    private final static String description = "Example Reply";
    private final static UUID owner = UUID.randomUUID();
    private final static UUID questionID = UUID.randomUUID();
    private final static UUID replyID = UUID.randomUUID();

    @Test
    public void testReply() {
        Reply reply = new Reply(description, questionID, owner, replyID);
        assertNotNull(reply);
        assertEquals(description, reply.getReply());
        assertEquals(owner, reply.getUser());
        assertEquals(replyID, reply.getReplyId());
    }

    @Test
    public void testGetReply() {
        Reply reply = new Reply(description, questionID, owner, replyID);
        assertEquals(description, reply.getReply());
        assertNotEquals("Random String", reply.getReply());
    }

    @Test
    public void testGetReplyId () {
        Reply reply = new Reply(description, questionID, owner, replyID);
        assertEquals(replyID, reply.getReplyId());
        assertNotEquals(UUID.randomUUID(), reply.getReplyId());
    }

    @Test
    public void testGetUser() {
        Reply reply = new Reply(description, questionID, owner, replyID);
        assertEquals(owner, reply.getUser());
        assertNotEquals(UUID.randomUUID(), reply.getUser());
    }

    @Test
    public void testToString() {
        Reply reply = new Reply(description, questionID, owner, replyID);
        assertEquals(description, reply.toString());
        assertNotEquals("Random String", reply.toString());
    }

    @Test
    public void testCompareTo() {
        Reply replyOne = new Reply(description, questionID, owner, replyID);
        Reply replyTwo = new Reply(description, questionID, owner, replyID);
        assertTrue("Error, not equal", replyOne.compareTo(replyTwo) == 0);
        Reply replyThree = new Reply("Z", questionID, owner, replyID);
        assertTrue("Error, not positive", replyOne.compareTo(replyThree) > 0);
        Reply replyFour = new Reply("A", questionID, owner, replyID);
        assertTrue("Error, not negative", replyOne.compareTo(replyFour) < 0);
    }
}
