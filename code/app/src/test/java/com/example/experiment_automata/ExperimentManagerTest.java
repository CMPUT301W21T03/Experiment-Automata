package com.example.experiment_automata;

import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentManager;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentMaker;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ExperimentManagerTest {
    ExperimentManager experimentManager;
    ArrayList<Experiment> experiments;
    ArrayList<UUID> experimentReferences;
    UUID userId;

    @BeforeEach
    public void setup() {
        ExperimentMaker experimentMaker = new ExperimentMaker();
        experimentManager = new ExperimentManager();
        experiments = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userId = UUID.randomUUID();
        Experiment e = experimentMaker.makeExperiment(ExperimentType.Binomial,
                "Test Experiment First", 0, false, true, userId);
        UUID id = e.getExperimentId();
        experimentReferences.add(id);
        experiments.add(e);
        experimentManager.add(id, e);

        e = experimentMaker.makeExperiment(ExperimentType.Binomial,
                "Test Experiment Second", 0, true, false, UUID.randomUUID());
        id = e.getExperimentId();
        experimentReferences.add(e.getExperimentId());
        experiments.add(e);
        experimentManager.add(id, e);
    }

    @Test
    public void appending() {
        assertEquals(experiments.get(0),
                experimentManager.queryExperiments(experimentReferences).get(0));
        assertEquals(experiments.get(1),
                experimentManager.queryExperiments(experimentReferences).get(1));
        assertEquals(2, experimentManager.getSize());
        Experiment experiment = experiments.get(0);
        try {
            experimentManager.add(experiment.getExperimentId(), experiment);
            fail("Duplicate Experiment ID was not detected as a duplicate");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void deleting() {
        UUID id = experiments.get(0).getExperimentId();
        experimentManager.delete(id);
        assertEquals(1, experimentManager.getSize());
    }

    @Test
    public void ownedExperiments() {
        ArrayList<Experiment> ownedExperiments = experimentManager.getOwnedExperiments(userId);
        assertEquals(experiments.get(0), ownedExperiments.get(0));
    }

    @Test
    public void publishedExperiments() {
        ArrayList<Experiment> publishedExperiments = experimentManager.queryPublishedExperiments();
        // None of the experiments have been published
        assertEquals(0, publishedExperiments.size());
        // Publish all the experiments
        for (Experiment e : experiments) {
            e.setPublished(true);
        }
        publishedExperiments = experimentManager.queryPublishedExperiments();
        assertEquals(2, publishedExperiments.size());
    }

    @Test
    public void stringQuery() {
        String query = "Test Experiment";
        assertEquals(2, experimentManager.queryExperiments(query).size());

        query = query.toLowerCase();
        assertEquals(2, experimentManager.queryExperiments(query).size());

        query = "t";
        assertEquals(2, experimentManager.queryExperiments(query).size());

        query = "second";
        assertEquals(1, experimentManager.queryExperiments(query).size());

        query = "first";
        assertEquals(1, experimentManager.queryExperiments(query).size());
    }

    @Test
    public void testingAllExperimentsReuturned()
    {
        ExperimentMaker maker = new ExperimentMaker();
        ArrayList<Experiment> testValues = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            String des = "" + i;
            int minTrials = i;
            boolean active = (i % 2) == 0;
            boolean location = (i % 3) == 0;
            UUID random = UUID.randomUUID();
            Experiment ex = maker.makeExperiment(ExperimentType.Count, des, minTrials, active, location, random);
            testValues.add(ex);
            experimentManager.add(random, ex);
        }

        // Sub 2 because of those made in start up
        assertEquals("Not all experiments returned",
                experimentManager.getAllExperiments().size()-2,
                testValues.size());
    }
}
