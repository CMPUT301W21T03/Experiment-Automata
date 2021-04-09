package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.trials.CountTrial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

public class CountStatsTest {
    private final UUID ownerId = UUID.randomUUID();
    private CountExperiment countExperiment;
    private final UUID id = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();
    private final UUID id3 = UUID.randomUUID();
    private final UUID id4 = UUID.randomUUID();
    private CountTrial successTrial;
    private CountTrial failureTrial;
    private CountTrial ignoreSuccess;
    private CountTrial ignoreFailure;

    @Before
    public void environmentSetup() {
        countExperiment = (CountExperiment) ExperimentMaker.makeExperiment(ExperimentType.Count,
                "This is a test",
                5,
                false,
                true,
                ownerId,
                "MURICA",
                false);
        successTrial = new CountTrial(id);
        failureTrial = new CountTrial(id2);
        ignoreSuccess = new CountTrial(id3);
        ignoreFailure = new CountTrial(id4);
    }

    @Test
    public void getMeanTest() {
        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getMean(), 1);
        countExperiment.recordTrial(failureTrial);
        assertEquals(countExperiment.getMean(), 1);
        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getMean(), 1);
    }

    @Test
    public void getMedianTest() {
        // No results recorded so far
        assertEquals(countExperiment.getMedian(), 1);

        countExperiment.recordTrial(successTrial);

        assertEquals(countExperiment.getMedian(), 1);

        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(failureTrial);

        assertEquals(countExperiment.getMedian(), 1);

        countExperiment.recordTrial(successTrial);

        assertEquals(countExperiment.getMedian(), 1);

        countExperiment.recordTrial(successTrial);

        assertEquals(countExperiment.getMedian(), 1);
    }

    @Test
    public void getSDVTest() {
        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getStdev(), 0);
        countExperiment.recordTrial(failureTrial);
        assertEquals(countExperiment.getStdev(), 0);
        countExperiment.recordTrial(failureTrial);
        assertEquals(countExperiment.getStdev(), 0);
        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getStdev(), 0);
        countExperiment.recordTrial(failureTrial);
        assertEquals(countExperiment.getStdev(), 0);
    }

    @Test
    public void getQuartilesTest1() {
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(successTrial);

        float[] quartiles = countExperiment.getQuartiles();
        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 0);
    }

    @Test
    public void getQuartilesTest2(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(failureTrial);

        float[] quartiles = countExperiment.getQuartiles();
        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 0);
    }

    @Test
    public void getQuartilesTest3() {
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);

        float[] quartiles = countExperiment.getQuartiles();
        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 0);
    }

    @Test
    public void getQuartilesTest4() {
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);

        float[] quartiles = countExperiment.getQuartiles();
        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 0);
    }

    @Test
    public void testIgnore() {
        ignoreSuccess.setIgnore(true);
        ignoreFailure.setIgnore(true);

        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);

        countExperiment.recordTrial(ignoreSuccess);

        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(successTrial);
        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(ignoreFailure);

        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(failureTrial);
        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(failureTrial);

        countExperiment.recordTrial(ignoreSuccess);
        assertEquals(countExperiment.getMean(), 1);

        countExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error
        assertEquals(countExperiment.getMean(), 1);
    }

    @Test
    public void testIgnore2() {
        ignoreSuccess.setIgnore(true);
        ignoreFailure.setIgnore(true);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(failureTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(successTrial);
        countExperiment.recordTrial(ignoreSuccess);
        countExperiment.recordTrial(ignoreFailure);
        countExperiment.recordTrial(ignoreSuccess);
        // Data is: 0, 0, 1, 1, 1, 1 (should be the same as in the quartiles test)

        float[] quartiles = countExperiment.getQuartiles();

        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 0);
    }
}
