package com.example.experiment_automata;

import com.example.experiment_automata.Experiments.ExperimentModel.BinomialExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.CountExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentMaker;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentType;
import com.example.experiment_automata.Experiments.ExperimentModel.MeasurementExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.NaturalCountExperiment;
import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.CountTrial;
import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BinomialExperimentStatTest {

    UUID ownerId = UUID.randomUUID();
    BinomialExperiment binomialExperiment=new BinomialExperiment("This is a test", 5, false, true, ownerId);
    UUID id = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    BinomialTrial successTrial = new BinomialTrial(id, true);

    BinomialTrial failureTrial = new BinomialTrial(id, false);

    @Before
    public void setup() {
        // Reset the binomial experiment
        binomialExperiment = new BinomialExperiment("This is a test", 5, false, true, ownerId);
    }

    @Test
    public void getMeanTest(){
        binomialExperiment.recordTrial(successTrial);
        assertEquals(binomialExperiment.getMean(), 1.0);
        binomialExperiment.recordTrial(failureTrial);
        assertEquals(binomialExperiment.getMean(), 0.5);
        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error for this one with a non-terminating decimal expansion
        assertEquals(binomialExperiment.getMean(), 0.666667);

        // 3/4 successes
        binomialExperiment.recordTrial(successTrial);
        assertEquals(binomialExperiment.getMean(), 0.75);

        // 3/5 successes
        binomialExperiment.recordTrial(failureTrial);
        assert(0.6-Math.pow(10, -6)<binomialExperiment.getMean() && binomialExperiment.getMean()<0.6+Math.pow(10,-6));

        // 3/6 successes

        binomialExperiment.recordTrial(failureTrial);
        assertEquals(binomialExperiment.getMean(), 0.5);

        // 4/7 successes

        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error
        assert(0.5714285-Math.pow(10, -6)<binomialExperiment.getMean() && binomialExperiment.getMean()<0.5714285+Math.pow(10,-6));

    }

    @Test
    public void getMedianTest(){
        // No results recorded so far
        assertEquals(binomialExperiment.getMedian(), 0.5);

        // 1 success, 0 failures
        binomialExperiment.recordTrial(successTrial);

        assertEquals(binomialExperiment.getMedian(), 1.0);

        // 1 success, 2 failures
        binomialExperiment.recordTrial(failureTrial);
        binomialExperiment.recordTrial(failureTrial);

        assertEquals(binomialExperiment.getMedian(), 0.0);

        // 2 success, 2 failures
        binomialExperiment.recordTrial(successTrial);

        assertEquals(binomialExperiment.getMedian(), 0.5);

        // 3 success, 2 failures
        binomialExperiment.recordTrial(successTrial);

        assertEquals(binomialExperiment.getMedian(), 1.0);
    }
}
