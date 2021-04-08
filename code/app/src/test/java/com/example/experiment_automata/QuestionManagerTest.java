package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.QuestionManager;
import com.example.experiment_automata.backend.questions.Reply;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class QuestionManagerTest {
    private QuestionManager questionManager;
    private ArrayList<Question> questions;
    private ArrayList<Reply> replies;
    private ArrayList<UUID> questionReferences;
    private ArrayList<UUID> replyReferences;
    private String question;
    private UUID userId;
    private UUID experimentId;
    private UUID questionId;
    private String reply;
    private UUID replyId;

    @BeforeEach
    public void runningSetup() {
        QuestionManager.enableTestMode();
        questionManager = QuestionManager.getInstance();
        questions = new ArrayList<>();
        questionReferences = new ArrayList<>();
        replies = new ArrayList<>();
        replyReferences = new ArrayList<>();
        question = "Question";
        userId = UUID.randomUUID();
        experimentId = UUID.randomUUID();
        questionId = UUID.randomUUID();
        Question q = new Question(question, userId, experimentId, questionId);
        questions.add(q);
        questionReferences.add(questionId);
        questionManager.addQuestion(experimentId, q);
        reply = "Reply";
        replyId = UUID.randomUUID();
        Reply r = new Reply(reply, questionId, userId, replyId);
        replies.add(r);
        replyReferences.add(replyId);
        questionManager.addReply(questionId, r);
    }

    @Test
    public void testAddQuestion() {
        assertEquals(questions.get(0), questionManager.getQuestion(questionId));
        Question newQuestion = new Question(question, userId, experimentId, UUID.randomUUID());
        try {
            questionManager.getQuestion(newQuestion.getQuestionId());
            fail("Should not have found the question");
        } catch(IllegalArgumentException ignored) {}
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

    @After
    public void finish() {
        QuestionManager.disableTestMode();
    }
}
