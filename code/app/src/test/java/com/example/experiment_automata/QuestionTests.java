package com.example.experiment_automata;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.ui.NavigationActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuestionTests {

    private UUID experimentId;
    private UUID userId;


    @Before
    public void setup() {

        userId = UUID.randomUUID();
        experimentId = UUID.randomUUID();
    }


    @Test
    public void createQuestion() {
        Question test = new Question("test", userId, experimentId, true);
        assertNotNull(test);
    }

    @Test
    public void getQuestion() {
        Question test = new Question("test", userId, experimentId, true);
        assertEquals("test", test.getQuestion());
    }

    @Test
    public void getQuestionId() {
        Question test = new Question("test", userId, experimentId, true);
        UUID testId = test.getQuestionId();
        assertNotNull(testId);
    }

    @Test
    public void getQuestionUser() {
        Question test = new Question("test", userId, experimentId, true);
        UUID testUser = test.getUser();
        assertNotNull(testUser);
        assertEquals(userId, testUser);
    }

    @Test
    public void getQuestionExperiment() {
        Question test = new Question("test", userId, experimentId, true);
        UUID testExperiment = test.getExperimentId();
        assertNotNull(testExperiment);
        assertEquals(experimentId, testExperiment);
    }

}
