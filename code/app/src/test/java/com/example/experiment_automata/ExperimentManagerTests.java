package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.backend.experiments.ExperimentType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExperimentManagerTests {

    private ExperimentMaker experimentMaker;
    private ExperimentManager experimentManager;
    private ArrayList<Experiment<?>> experiments;
    private ArrayList<UUID> experimentReferences;
    private UUID userId;

    @Before
    public void runningSetup()
    {
        experimentMaker = new ExperimentMaker();
        experimentManager = ExperimentManager.getInstance(true);
        experiments = new ArrayList<>();
        experimentReferences = new ArrayList<>();
        userId = UUID.randomUUID();
        Experiment<?> e = ExperimentMaker.makeExperiment(ExperimentType.Binomial,"Second",
                0, false, false,  userId, false, UUID.randomUUID(), true);
        UUID id = e.getExperimentId();
        experimentReferences.add(id);
        experiments.add(e);
        experimentManager.add(id, e);

        e = ExperimentMaker.makeExperiment(ExperimentType.Binomial,"First",
                0, false, false,  UUID.randomUUID(), false, UUID.randomUUID(), true);
        id = e.getExperimentId();
        experimentReferences.add(e.getExperimentId());
        experiments.add(e);
        experimentManager.add(id, e);

    }

    /**
     *  resets the experiment manager instence
     *  Source:
     *      Author:https://stackoverflow.com/users/1230702/teded
     *      Editor:https://stackoverflow.com/users/1230702/teded
     *      Full URL:https://stackoverflow.com/questions/8256989/singleton-and-unit-testing
     * @throws NoSuchFieldException
     */
    @After
    public void cleanWorld() throws NoSuchFieldException, IllegalAccessException {
        Field currentInstance = ExperimentManager.class.getDeclaredField("experimentManager");
        currentInstance.setAccessible(true);
        currentInstance.set(experimentManager, null);
    }

    //Testing small change work as expected
    @Test
    public void appending() {

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
        assertEquals(experimentManager.getAllExperiments().size(), experiments.size());
        ArrayList<UUID> fakeUUID = new ArrayList<>();
        fakeUUID.add(UUID.randomUUID());
        ArrayList<Experiment<?>> fakeExperiments = experimentManager.queryExperiments(fakeUUID);
        assertEquals(0, fakeExperiments.size());
        assertEquals(0, fakeExperiments.size());
    }

    @Test
    public void queryOwnedExperiments() {
        assertEquals(experiments.get(0), experimentManager
                .queryOwnedExperiments("Second", userId).get(0));

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
        assertEquals(0, experimentManager.queryExperiments("test").size());
        assertEquals(0, experimentManager.queryExperiments("garbage").size());
    }

    @Test
    public void queryExperimentsUUIDAndString() {
        ArrayList<UUID> fakeIds = new ArrayList<>();
        fakeIds.add(UUID.randomUUID());
        assertEquals(1, experimentManager.queryExperiments("Second", experimentReferences).size());
        assertEquals(1, experimentManager.queryExperiments("First", experimentReferences).size());
        assertEquals(0, experimentManager.queryExperiments("Test", experimentReferences).size());
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
    }

    @Test
    public void stringQuery() {

        UUID e1 = UUID.randomUUID();
        UUID e2 = UUID.randomUUID();
        String query = "Test Experiment";

        Experiment e = experimentMaker.makeExperiment(ExperimentType.Binomial,query,
                0, false, false,  e1, false, UUID.randomUUID(), true);

        Experiment e20 = experimentMaker.makeExperiment(ExperimentType.Binomial,query,
                0, false, false,  e2, false, UUID.randomUUID(), true);


        experimentManager.add(e1, e);
        experimentManager.add(e2, e20);


        assertEquals(2, experimentManager.queryExperiments(query).size());

        query = query.toLowerCase();
        assertEquals(2, experimentManager.queryExperiments(query).size());

        query = "t";
        assertEquals(3, experimentManager.queryExperiments(query).size());

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

            Experiment<?> ex = ExperimentMaker.makeExperiment(ExperimentType.Count,
                    des, minTrials, location,
                    active, UUID.randomUUID(), false,
                    UUID.randomUUID(), true);
            testValues.add(ex);
            experimentManager.add(random, ex);
        }

        // Sub 2 because of those made in start up
        assertEquals("Not all experiments returned",
                experimentManager.getAllExperiments().size()-2,
                testValues.size());
    }

}
