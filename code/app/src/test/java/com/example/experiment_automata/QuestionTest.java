package com.example.experiment_automata;

import com.example.experiment_automata.backend.questions.Question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;

public class QuestionTest {
    private UUID experimentId;
    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
        experimentId = UUID.randomUUID();
    }

    @Test
    public void createQuestion() {
        Question test = new Question("test", userId, experimentId);
        assertNotNull(test);
    }

    @Test
    public void getQuestion() {
        Question test = new Question("test", userId, experimentId);
        assertEquals("test", test.getQuestion());
    }

    @Test
    public void getQuestionId() {
        Question test = new Question("test", userId, experimentId);
        UUID testId = test.getQuestionId();
        assertNotNull(testId);
    }

    @Test
    public void getQuestionUser() {
        Question test = new Question("test", userId, experimentId);
        UUID testUser = test.getUser();
        assertNotNull(testUser);
        assertEquals(userId, testUser);
    }

    @Test
    public void getQuestionExperiment() {
        Question test = new Question("test", userId, experimentId);
        UUID testExperiment = test.getExperimentId();
        assertNotNull(testExperiment);
        assertEquals(experimentId, testExperiment);
    }
}
