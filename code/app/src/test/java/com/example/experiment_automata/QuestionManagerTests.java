package com.example.experiment_automata;


import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.QuestionManager;
import com.example.experiment_automata.backend.questions.Reply;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class QuestionManagerTests {


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

    @Before
    public void setup() {
        questionManager = QuestionManager.getTestInstence();
        questions = new ArrayList<>();
        replies = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userReferences = new ArrayList<>();
        userId1 = UUID.randomUUID();
        experimentId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        experimentId2 = UUID.randomUUID();
        q1 = new Question("Q1", userId1, experimentId1, true);
        q2 = new Question("Q2", userId1, experimentId1, true);
        q3 = new Question("Q3", userId2, experimentId2, true);
        r1 = new Reply("R1", q1.getQuestionId(), userId2, true);
        r3 = new Reply("R3", q3.getExperimentId(), userId1, true);
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        experimentReferences.add(experimentId1);
        experimentReferences.add(experimentId2);
        userReferences.add(userId1);
        userReferences.add(userId2);
    }

    @After
    public void destroyManager() throws NoSuchFieldException, IllegalAccessException {
        Field currentInstance = QuestionManager.class.getDeclaredField("questionManager");
        currentInstance.setAccessible(true);
        currentInstance.set(questionManager, null);
    }

    @Test
    public void addQuestion()
    {
        int questionCountBefore;
        int questionCountAfter;

        if(experimentId1 == null || q1 == null)
            assertEquals("BAD TEST", 1, 2);

        questionCountBefore = questionManager.getTotalQuestions(experimentId1);
        questionManager.addQuestion(experimentId1, q1);
        questionCountAfter = questionManager.getTotalQuestions(experimentId1);

        assertEquals("Unable to add question: Make sure counts are updated",
                questionCountBefore + 2,
                questionCountAfter);
    }

    // currently fails due to NullPointerException, could be issue with Question class or QuestionManager
    @Test
    public void addReply()
    {
        questionManager.addQuestion(experimentId1, q1);
        questionManager.addReply(q1.getQuestionId(), r1);
        Reply returnedReply = null;
        try
        {
            returnedReply =  questionManager.getQuestionReply(q1.getQuestionId()).get(0);
        }
        catch (IllegalArgumentException e) {}

        assertEquals(r1.getReplyId(), returnedReply.getReplyId());
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

    // work in progress, getting the NullPointerException
    @Test
    public void getExperimentQuestions() {
        try {
            questionManager.getExperimentQuestions(experimentId1);
        } catch (IllegalArgumentException e) {}
        questionManager.addQuestion(experimentId1, q1);
        questionManager.getTotalQuestions(q1.getExperimentId());
    }

    @Test
    public void testGetAllQuestions()
    {
        Collection<ArrayList<Question>> givenQuestion = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            ArrayList temp = new ArrayList();
            Question q = new Question("ddd", UUID.randomUUID(), UUID.randomUUID(), true);
            temp.add(q);
            givenQuestion.add(temp);
            questionManager.addQuestion(UUID.randomUUID(), q);
        }
        assertEquals("Was not able to get the list of all questions",
                questionManager.getAllQuestions().size(),
                givenQuestion.size());
    }

}
