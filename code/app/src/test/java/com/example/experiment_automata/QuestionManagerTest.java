package com.example.experiment_automata;

import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.QuestionManager;
import com.example.experiment_automata.backend.questions.Reply;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class QuestionManagerTest {
    private QuestionManager questionManager;
    private ArrayList<Question> questions;
    private ArrayList<Reply> replies;
    private String question;
    private UUID userId;
    private UUID experimentId;
    private UUID questionId;
    private String reply;
    private UUID replyId;

    @BeforeEach
    public void runningSetup() {
        QuestionManager.enableTestMode();
        QuestionManager.resetInstance();
        questionManager = QuestionManager.getInstance();
        questions = new ArrayList<>();
        replies = new ArrayList<>();
        question = "Question";
        userId = UUID.randomUUID();
        experimentId = UUID.randomUUID();
        questionId = UUID.randomUUID();
        Question q = new Question(question, userId, experimentId, questionId);
        questions.add(q);
        questionManager.addQuestion(experimentId, q);
        reply = "Reply";
        replyId = UUID.randomUUID();
        Reply r = new Reply(reply, questionId, userId, replyId);
        replies.add(r);
        questionManager.addReply(questionId, r);
    }

    @Test
    public void testAddQuestion() {
        assertEquals(questions.get(0), questionManager.getQuestion(questionId));
        Question newQuestion = new Question(question, userId, experimentId, UUID.randomUUID());
        try {
            questionManager.getQuestion(newQuestion.getQuestionId());
            fail("Should not have found the question");
        } catch (IllegalArgumentException ignored) {}
        questionManager.addQuestion(experimentId, newQuestion);
        assertEquals(newQuestion, questionManager.getQuestion(newQuestion.getQuestionId()));
    }

    @Test
    public void testAddReply() {
        assertEquals(replies.get(0), questionManager.getQuestionReply(questionId).get(0));
        Reply newReply = new Reply("New reply", questionId, userId, UUID.randomUUID());
        assertEquals(1, questionManager.getQuestionReply(questionId).size());
        questionManager.addReply(questionId, newReply);
        assertEquals(2, questionManager.getQuestionReply(questionId).size());
        Reply duplicateReply = new Reply(reply, questionId, userId, replyId);
        questionManager.addReply(questionId, duplicateReply);
        assertEquals(2, questionManager.getQuestionReply(questionId).size());
    }

    @Test
    public void testGetQuestion() {
        assertEquals(questions.get(0), questionManager.getQuestion(questionId));
        assertThrows(IllegalArgumentException.class, () -> questionManager.getQuestion(UUID.randomUUID()));
    }

    @Test
    public void testGetTotalQuestions() {
        assertEquals(1, questionManager.getTotalQuestions(experimentId));
        Question newQuestion = new Question(question, userId, experimentId, UUID.randomUUID());
        assertEquals(1, questionManager.getTotalQuestions(experimentId));
        questionManager.addQuestion(newQuestion.getExperimentId(), newQuestion);
        assertEquals(2, questionManager.getTotalQuestions(experimentId));
        assertEquals(-1, questionManager.getTotalQuestions(UUID.randomUUID()));
    }

    @Test
    public void testGetExperimentQuestions() {
        assertEquals(1, questionManager.getExperimentQuestions(experimentId).size());
        assertEquals(questions.get(0), questionManager.getExperimentQuestions(experimentId).get(0));
        Question newQuestion = new Question(question, userId, experimentId, UUID.randomUUID());
        assertEquals(1, questionManager.getExperimentQuestions(experimentId).size());
        questionManager.addQuestion(newQuestion.getExperimentId(), newQuestion);
        assertEquals(2, questionManager.getExperimentQuestions(experimentId).size());
        assertTrue(questionManager.getExperimentQuestions(experimentId).contains(newQuestion), "Error, does not contain newQuestion");
        assertThrows(IllegalArgumentException.class, () -> questionManager.getExperimentQuestions(UUID.randomUUID()));
    }

    @Test
    public void testGetQuestionReply() {
        assertEquals(1, questionManager.getQuestionReply(questionId).size());
        assertEquals(replies.get(0), questionManager.getQuestionReply(questionId).get(0));
        UUID newReplyUUID = UUID.randomUUID();
        Reply newReply = new Reply("New reply", questionId, userId, newReplyUUID);
        assertEquals(1, questionManager.getQuestionReply(questionId).size());
        questionManager.addReply(questionId, newReply);
        assertEquals(2, questionManager.getQuestionReply(questionId).size());
        assertTrue(questionManager.getQuestionReply(questionId).contains(newReply), "Error, could not find new reply");
        UUID newQuestionUUID = UUID.randomUUID();
        Question otherQuestion = new Question("Other Question", userId, experimentId, newQuestionUUID);
        Reply otherReply = new Reply("Other reply", newQuestionUUID, userId, UUID.randomUUID());
        assertEquals(0, questionManager.getQuestionReply(newQuestionUUID).size());
        questionManager.addQuestion(experimentId, otherQuestion);
        questionManager.addReply(newQuestionUUID, otherReply);
        assertEquals(otherReply, questionManager.getQuestionReply(newQuestionUUID).get(0));
        assertEquals(2, questionManager.getQuestionReply(questionId).size());
    }

    @Test
    public void testGetAllQuestions() {
        assertEquals(1, questionManager.getAllQuestions().iterator().next().size());
        Question newQuestion = new Question("New Question", userId, experimentId, UUID.randomUUID());
        questionManager.addQuestion(experimentId, newQuestion);
        assertEquals(2, questionManager.getAllQuestions().iterator().next().size());
        assertTrue(questionManager.getAllQuestions().iterator().next().contains(questions.get(0)), "Does not contain original item");
        assertTrue(questionManager.getAllQuestions().iterator().next().contains(newQuestion), "Does not contain new item");
    }

    @After
    public void finish() {
        QuestionManager.disableTestMode();
    }
}
