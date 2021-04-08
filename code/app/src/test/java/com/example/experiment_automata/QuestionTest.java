package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.Reply;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestionTest {
    private final static String description = "Example Question";
    private final static UUID owner = UUID.randomUUID();
    private final static UUID experiment = UUID.randomUUID();
    private final static UUID questionID = UUID.randomUUID();

    @Test
    public void testQuestion() {
        Question question = new Question(description, owner, experiment, questionID);
        assertNotNull(question);
        assertEquals(description, question.getQuestion());
        assertEquals(owner, question.getUser());
        assertEquals(experiment, question.getExperimentId());
        assertEquals(questionID, question.getQuestionId());
    }

    @Test
    public void testGetQuestion() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(description, question.getQuestion());
        assertNotEquals("random string", question.getQuestion());
    }

    @Test
    public void testSetQuestion() {
        Question question = new Question(description, owner, experiment, questionID);
        String newQuestion = "New Question";
        assertEquals(description, question.getQuestion());
        assertNotEquals(newQuestion, question.getQuestion());
        question.setQuestion(newQuestion);
        assertNotEquals(description, question.getQuestion());
        assertEquals(newQuestion, question.getQuestion());
    }

    @Test
    public void testGetUser() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(owner, question.getUser());
        assertNotEquals(UUID.randomUUID(), question.getUser());
    }

    @Test
    public void testSetUser() {
        Question question = new Question(description, owner, experiment, questionID);
        UUID newOwner = UUID.randomUUID();
        assertEquals(owner, question.getUser());
        assertNotEquals(newOwner, question.getUser());
        question.setUser(newOwner);
        assertEquals(newOwner, question.getUser());
        assertNotEquals(owner, question.getUser());
    }

    @Test
    public void testGetExperiment() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(experiment, question.getExperimentId());
        assertNotEquals(UUID.randomUUID(), question.getExperimentId());
    }

    @Test
    public void testGetReply() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(null, question.getReply());
        UUID replyId = UUID.randomUUID();
        question.setReply(replyId);
        assertEquals(replyId, question.getReply());
        assertNotEquals(UUID.randomUUID(), question.getReply());
    }

    @Test
    public void testSetReply() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(null, question.getReply());
        UUID replyId = UUID.randomUUID();
        question.setReply(replyId);
        assertEquals(replyId, question.getReply());
        assertNotEquals(UUID.randomUUID(), question.getReply());
        UUID newReplyId = UUID.randomUUID();
        question.setReply(newReplyId);
        assertEquals(newReplyId, question.getReply());
        assertNotEquals(replyId, question.getReply());
    }

    @Test
    public void testGetQuestionId() {
        Question question = new Question(description, owner, experiment, questionID);
        assertEquals(questionID, question.getQuestionId());
        assertNotEquals(UUID.randomUUID(), question.getQuestionId());
    }

    @Test
    public void testCompare() {
        Question questionOne = new Question(description, owner, experiment, questionID);
        Question questionTwo = new Question(description, owner, experiment, questionID);
        assertTrue("Error, not equal", questionOne.compareTo(questionTwo) == 0);
        questionTwo.setQuestion("Z");
        assertTrue("Error, not positive", questionOne.compareTo(questionTwo) > 0);
        questionTwo.setQuestion("A");
        assertTrue("Error, not negative", questionOne.compareTo(questionTwo) < 0);
    }
}
