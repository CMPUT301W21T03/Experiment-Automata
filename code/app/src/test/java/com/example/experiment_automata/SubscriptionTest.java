package com.example.experiment_automata;

import org.junit.jupiter.api.Test;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    ExperimentMaker experimentMaker = new ExperimentMaker();

    private Experiment experiment = null;
    private Experiment experiment2 = null;
    //private User user = new User();

    @Test
    void testConstructor()
    {
        /*
        try
        {
            experiment = experimentMaker.makeExperiment(ExperimentType.Measurement, "test");
            experiment2 = experimentMaker.makeExperiment(ExperimentType.Measurement, "test 2");
            Subscription subscription = new Subscription(user, experiment2);
            Collection<Experiment> answer = subscription.getSubscriptions();
            assertEquals(1, answer.size());
        }
        catch (Exception e)
        {
            // TODO: Deal with exception in test better than this :(
            fail(e.getStackTrace().toString());

        }
         */
        //TODO: Fix unit test
        assert false;
    }
}
