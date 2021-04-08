package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.trials.CountTrial;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestionTest {
    private final static String description = "Example Question";
    private final static UUID owner = UUID.randomUUID();
    private final static UUID experiment = UUID.randomUUID();
    private final static UUID questionID = UUID.randomUUID();
    private final static Boolean enableFirestoreSupport = Boolean.FALSE;

    @Test
    public void testExperiment() {
        Question question = new Question(description, owner, experiment, questionID);
        assertNotNull(question);
        assertEquals(description, question.getQuestion());
        assertEquals(owner, question.getUser());
        assertEquals(experiment, question.getExperimentId());
        assertEquals(questionID, question.getQuestionId());
    }
}
