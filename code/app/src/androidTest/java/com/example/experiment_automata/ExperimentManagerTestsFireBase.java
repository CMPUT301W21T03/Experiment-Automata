package com.example.experiment_automata;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.trials.Trial;
import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExperimentManagerTestsFireBase {

    private ExperimentManager experimentManager;
    private ArrayList<Experiment<?>> experiments;
    private ArrayList<UUID> experimentReferences;
    private UUID userId;


    @Before
    public void runningSetup() {
        ExperimentMaker experimentMaker = new ExperimentMaker();
        experimentManager = new ExperimentManager();
        experiments = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userId = UUID.randomUUID();
        Experiment<?> e = ExperimentMaker.makeExperiment(ExperimentType.Binomial,
                "Test Experiment First", 0, false, true, userId);
        UUID id = e.getExperimentId();
        experimentReferences.add(id);
        experiments.add(e);
        experimentManager.add(id, e);

        e = ExperimentMaker.makeExperiment(ExperimentType.Binomial,
                "Test Experiment Second", 0, true, false, UUID.randomUUID());
        id = e.getExperimentId();
        experimentReferences.add(e.getExperimentId());
        experiments.add(e);
        experimentManager.add(id, e);

        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
    }

    //Testing small change work as expected
    @Test
    public void appending() {
        assertEquals(experiments.get(0),
                experimentManager.queryExperiments(experimentReferences).get(0));
        assertEquals(experiments.get(1),
                experimentManager.queryExperiments(experimentReferences).get(1));
        assertEquals(2, experimentManager.getSize());
        Experiment<?> experiment = experiments.get(0);
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
        ArrayList<Experiment<?>> ownedExperiments = experimentManager.getOwnedExperiments(userId);
        assertEquals(experiments.get(0), ownedExperiments.get(0));
    }

    @Test
    public void queryExperimentsUUID() {
        ArrayList<Experiment<?>> foundExperiments = experimentManager.queryExperiments(experimentReferences);
        assertEquals(experiments, foundExperiments);

        ArrayList<UUID> fakeUUID = new ArrayList<>();
        fakeUUID.add(UUID.randomUUID());
        ArrayList<Experiment<?>> fakeExperiments = experimentManager.queryExperiments(fakeUUID);
        assertEquals(0, fakeExperiments.size());
        assertEquals(0, fakeExperiments.size());
    }

    @Test
    public void queryOwnedExperiments() {
        assertEquals(experiments.get(0), experimentManager
                .queryOwnedExperiments("Test", userId).get(0));
        assertEquals(0,  experimentManager
                .queryOwnedExperiments("Garbage", userId).size());
        assertEquals(0,  experimentManager
                .queryOwnedExperiments("Test", UUID.randomUUID()).size());
    }

    @Test
    public void publishedExperiments() {
        ArrayList<Experiment<?>> publishedExperiments = experimentManager.getPublishedExperiments();
        // None of the experiments have been published
        assertEquals(0, publishedExperiments.size());
        // Publish all the experiments
        for (Experiment<?> e : experiments) {
            e.setPublished(true);
        }
        publishedExperiments = experimentManager.getPublishedExperiments();
        assertEquals(2, publishedExperiments.size());
    }

    @Test
    public void queryExperimentsString() {
        assertEquals(1, experimentManager.queryExperiments("Second").size());
        assertEquals(1, experimentManager.queryExperiments("First").size());
        assertEquals(2, experimentManager.queryExperiments("test").size());
        assertEquals(0, experimentManager.queryExperiments("garbage").size());
    }

    @Test
    public void queryExperimentsUUIDAndString() {
        ArrayList<UUID> fakeIds = new ArrayList<>();
        fakeIds.add(UUID.randomUUID());
        assertEquals(1, experimentManager.queryExperiments("Second", experimentReferences).size());
        assertEquals(1, experimentManager.queryExperiments("First", experimentReferences).size());
        assertEquals(2, experimentManager.queryExperiments("Test", experimentReferences).size());
        assertEquals(0, experimentManager.queryExperiments("first", fakeIds).size());
    }

    @Test
    public void queryPublishedExperiments() {
        assertEquals(0, experimentManager.queryPublishedExperiments("First").size());
        assertEquals(0, experimentManager.queryPublishedExperiments("Garbage").size());
        for (Experiment<?> e : experiments) {
            e.setPublished(true);
        }
        assertEquals(1, experimentManager.queryPublishedExperiments("First").size());
        assertEquals(0, experimentManager.queryPublishedExperiments("Garbage").size());
        assertEquals(2, experimentManager.queryPublishedExperiments("Experiment").size());
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
    public void testingAllExperimentsReturned() {
        ArrayList<Experiment<?>> testValues = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            String des = "" + i;
            int minTrials = i;
            boolean active = (i % 2) == 0;
            boolean location = (i % 3) == 0;
            UUID random = UUID.randomUUID();
            Experiment<?> ex = ExperimentMaker.makeExperiment(ExperimentType.Count, des, minTrials, active, location, random);
            testValues.add(ex);
            experimentManager.add(random, ex);
        }

        // Sub 2 because of those made in start up
        assertEquals("Not all experiments returned",
                experimentManager.getAllExperiments().size()-2,
                testValues.size());
    }

}
