package com.example.experiment_automata;

import org.junit.function.ThrowingRunnable;
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
        try {
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
        } catch (IllegalExperimentException e) {
            fail(e);
        }
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
}
