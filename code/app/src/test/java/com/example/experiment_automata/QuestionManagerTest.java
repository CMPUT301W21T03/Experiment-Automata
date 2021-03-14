package com.example.experiment_automata;

import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.QuestionsModel.QuestionManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class QuestionManagerTest {
    QuestionManager questionManager;
    ArrayList<Question> questions;
    ArrayList<UUID> experimentReferences;
    ArrayList<UUID> userReferences;
    UUID userId1;
    UUID experimentId1;
    UUID userId2;
    UUID experimentId2;
    Question q1, q2, q3;

    @BeforeEach
    public void setup() {
        questionManager = new QuestionManager();
        questions = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userReferences = new ArrayList<>();
        userId1 = UUID.randomUUID();
        experimentId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        experimentId2 = UUID.randomUUID();
        Question q1 = new Question("Q1", userId1, experimentId1);
        Question q2 = new Question("Q2", userId1, experimentId1);
        Question q3 = new Question("Q3", userId2, experimentId2);
        experimentReferences.add(experimentId1);
        experimentReferences.add(experimentId2);
        userReferences.add(userId1);
        userReferences.add(userId2);
    }

    @Test
    public void addQuestion() {
        try {
            assertEquals(0, questionManager.getTotalQuestions(experimentId1));
            fail("Should have failed to search for non existent experiment");
        } catch (IllegalArgumentException e) {}
        try {
            assertEquals(0, questionManager.getTotalQuestions(experimentId2));
            fail("Should have failed to search for non existent experiment");
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId1, q1);
        assertEquals(1, questionManager.getTotalQuestions(experimentId1));
        try {
            assertEquals(0, questionManager.getTotalQuestions(experimentId2));
            fail("Should have failed to search for non existent experiment");
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId2, q3);
        assertEquals(1, questionManager.getTotalQuestions(experimentId1));
        assertEquals(1, questionManager.getTotalQuestions(experimentId2));
        questionManager.addQuestion(experimentId1, q2);
        assertEquals(2, questionManager.getTotalQuestions(experimentId1));
        assertEquals(1, questionManager.getTotalQuestions(experimentId2));
    }
    
}
