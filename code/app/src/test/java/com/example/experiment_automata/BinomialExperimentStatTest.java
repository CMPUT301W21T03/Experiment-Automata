package com.example.experiment_automata;

import com.example.experiment_automata.Experiments.ExperimentModel.BinomialExperiment;
import com.example.experiment_automata.trials.BinomialTrial;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinomialExperimentStatTest {

    UUID ownerId = UUID.randomUUID();
    BinomialExperiment binomialExperiment=new BinomialExperiment("This is a test", 5, false, true, ownerId);
    UUID id = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();
    UUID id4 = UUID.randomUUID();

    BinomialTrial successTrial = new BinomialTrial(id, true);

    BinomialTrial failureTrial = new BinomialTrial(id2, false);

    BinomialTrial ignoreSuccess = new BinomialTrial(id3, true);

    BinomialTrial ignoreFailure = new BinomialTrial(id4, false);



    @Before
    public void setup() {
        // Reset the binomial experiment
        successTrial = new BinomialTrial(id, true);
        failureTrial = new BinomialTrial(id, false);
        binomialExperiment = new BinomialExperiment("This is a test", 5, false, true, ownerId);
    }

    /**
     * Function to see if two floats are close in value
     * @param first First float to compare to the second one
     * @param second Second float to compare to the first one
     * @return true if f and g are within a close margin of error and false otherwise
     */
    public boolean marginOfError(float first, float second){
        if(Math.abs(first-second)<Math.pow(10,-6)){
            // If the absolute value of the difference between first and second is within an error margin (10^(-6))
            return true;
        }
        else{
            return false;
        }
    }

    @Test
    public void getMeanTest(){
        binomialExperiment.recordTrial(successTrial);
        assertEquals(binomialExperiment.getMean(), 1.0);
        binomialExperiment.recordTrial(failureTrial);
        assertEquals(binomialExperiment.getMean(), 0.5);
        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error for this one with a non-terminating decimal expansion
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.6666667));

        // 3/4 successes
        binomialExperiment.recordTrial(successTrial);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.75));

        // 3/5 successes
        binomialExperiment.recordTrial(failureTrial);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.6));

        // 3/6 successes

        binomialExperiment.recordTrial(failureTrial);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.5));

        // 4/7 successes

        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.57142857));
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

    @Test
    public void getSDVTest(){
        // Numbers for verification computed from https://www.calculator.net/standard-deviation-calculator.html
        // 1 success, 0 failures
        binomialExperiment.recordTrial(successTrial);

        assertEquals(binomialExperiment.getStdev(), 0.0);

        // 1 success, 1 failure

        binomialExperiment.recordTrial(failureTrial);

        assertEquals(binomialExperiment.getStdev(), 0.5);

        // 1 success, 2 failures

        binomialExperiment.recordTrial(failureTrial);

        assertTrue(marginOfError(binomialExperiment.getStdev(), 0.47140452079103f));

        binomialExperiment.recordTrial(successTrial);

        assertEquals(binomialExperiment.getStdev(), 0.5);

        // 2 success, 3 failures
        binomialExperiment.recordTrial(failureTrial);

        assertTrue(marginOfError(binomialExperiment.getStdev(), 0.48989794855664f));

    }

    @Test
    public void getQuartilesTest1(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(successTrial);

        // Data is: 0, 0, 1, 1, 1, 1

        float[] quartiles = binomialExperiment.getQuartiles();

        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 1);


    }

    @Test
    public void getQuartilesTest2(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(failureTrial);

        float[] quartiles = binomialExperiment.getQuartiles();
        assertEquals(quartiles[0], 0) ;
        assertEquals(quartiles[1], 0);
        assertEquals(quartiles[2], 0.5);

    }

    @Test
    public void getQuartilesTest3(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);


        float[] quartiles = binomialExperiment.getQuartiles();
        assertEquals(quartiles[0], 0) ;
        assertEquals(quartiles[1], 0);
        assertEquals(quartiles[2], 0.5);

    }

    @Test
    public void getQuartilesTest4(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);



        float[] quartiles = binomialExperiment.getQuartiles();
        assertEquals(quartiles[0], 0) ;
        assertEquals(quartiles[1], 0);
        assertEquals(quartiles[2], 1);

    }

    @Test
    public void testIgnore(){

        ignoreSuccess.setIgnore(true);
        ignoreFailure.setIgnore(true);

        binomialExperiment.recordTrial(successTrial);
        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(ignoreSuccess);

        // Shouldn't have changed
        assertEquals(binomialExperiment.getMean(), 0.5);


        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error for this one with a non-terminating decimal expansion
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.6666667));

        binomialExperiment.recordTrial(successTrial);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.75));

        // 3/5 successes

        binomialExperiment.recordTrial(ignoreFailure);

        // Should not have changed from above
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.75));

        binomialExperiment.recordTrial(failureTrial);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.6));

        // 3/6 successes

        binomialExperiment.recordTrial(failureTrial);


        // 4/7 successes

        binomialExperiment.recordTrial(ignoreSuccess);
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.5));

        binomialExperiment.recordTrial(successTrial);
        // Within a reasonable margin of error
        assertTrue(marginOfError(binomialExperiment.getMean(), (float) 0.57142857));
    }

    @Test
    public void testIgnore2(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        ignoreSuccess.setIgnore(true);
        ignoreFailure.setIgnore(true);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(failureTrial);

        binomialExperiment.recordTrial(successTrial);

        binomialExperiment.recordTrial(successTrial);

        // These should all be ignored
        binomialExperiment.recordTrial(ignoreSuccess);

        binomialExperiment.recordTrial(ignoreFailure);

        binomialExperiment.recordTrial(ignoreSuccess);
        // Data is: 0, 0, 1, 1, 1, 1 (should be the same as in the quartiles test)

        float[] quartiles = binomialExperiment.getQuartiles();

        assertEquals(quartiles[0], 0);
        assertEquals(quartiles[1], 1);
        assertEquals(quartiles[2], 1);


    }
}
