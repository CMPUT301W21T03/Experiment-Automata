package com.example.experiment_automata;

import android.util.Log;

import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.QuestionsModel.QuestionManager;
import com.example.experiment_automata.QuestionsModel.Reply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class QuestionManagerTest {
    QuestionManager questionManager;
    ArrayList<Question> questions;
    ArrayList<Reply> replies;
    ArrayList<UUID> experimentReferences;
    ArrayList<UUID> userReferences;
    UUID userId1;
    UUID experimentId1;
    UUID userId2;
    UUID experimentId2;
    Question q1, q2, q3;
    Reply r1, r3;

    @BeforeEach
    public void setup() {
        questionManager = QuestionManager.getInstance();
        questions = new ArrayList<>();
        replies = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userReferences = new ArrayList<>();
        userId1 = UUID.randomUUID();
        experimentId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        experimentId2 = UUID.randomUUID();
        Question q1 = new Question("Q1", userId1, experimentId1);
        Question q2 = new Question("Q2", userId1, experimentId1);
        Question q3 = new Question("Q3", userId2, experimentId2);
        Reply r1 = new Reply("R1", userId2);
        Reply r3 = new Reply("R3", userId1);
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        experimentReferences.add(experimentId1);
        experimentReferences.add(experimentId2);
        userReferences.add(userId1);
        userReferences.add(userId2);

    }

    @Test
    public void addQuestion() {
        try {
            questionManager.getTotalQuestions(experimentId1);
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId1, q1);
        assertEquals(1, questionManager.getTotalQuestions(experimentId1));
    }

    // currently fails due to NullPointerException, could be issue with Question class or QuestionManager
    @Test
    public void addReply() {
        try {
            questionManager.getQuestionReply(q1.getQuestionId());
        } catch (IllegalArgumentException e) {}
        questionManager.addReply(q1.getQuestionId(), r1);
        assertEquals(1, questionManager.getQuestionReply(q1.getQuestionId()));
    }

    @Test
    public void getTotalQuestions() {
        try {
            questionManager.getTotalQuestions(experimentId1);
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId1, q1);
        assertEquals(1, questionManager.getTotalQuestions(experimentId1));
        questionManager.addQuestion(experimentId1, q2);
        assertEquals(2, questionManager.getTotalQuestions(experimentId1));
        try {
            questionManager.getTotalQuestions(experimentId2);
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId2, q3);
        assertEquals(2, questionManager.getTotalQuestions(experimentId1));
        assertEquals(1, questionManager.getTotalQuestions(experimentId2));
    }
    
}
