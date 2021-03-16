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

public class NaturalCountStatTest {

    UUID ownerId = UUID.randomUUID();
    NaturalCountExperiment natExperiment=new NaturalCountExperiment("This is a test", 5, false, true, ownerId);
    UUID id = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    @Before
    public void setup() {
        // Reset the binomial experiment
        natExperiment = new NaturalCountExperiment("This is a test", 5, false, true, ownerId);
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
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getMean(), 2.0);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMean(), 2.5);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertEquals(natExperiment.getMean(), 3);

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertEquals(natExperiment.getMean(), 4.5);

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertTrue(marginOfError(natExperiment.getMean(),  4.2f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        assertTrue(marginOfError(natExperiment.getMean(), 3.6666667f));
    }


    @Test
    public void getMedianTest(){
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getMedian(), 2.0);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMedian(), 2.5);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertEquals(natExperiment.getMedian(), 3);

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertEquals(natExperiment.getMedian(), 3.5);

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMedian(),  3);


        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        // 1, 1, 2, 3, 3, 4, 9
        assertEquals(natExperiment.getMedian(), 3);

        //  1, 1, 1, 2, 3, 3, 4, 9
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        assertEquals(natExperiment.getMedian(), 2.5);
        // 1, 1, 1, 1, 2, 3, 3, 4, 9
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));

        assertEquals(natExperiment.getMedian(), 2);


    }

    @Test
    public void getStdevTest(){
        // Numbers for verification computed from https://www.calculator.net/standard-deviation-calculator.html
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getStdev(), 0);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getStdev(), 0.5);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertTrue(marginOfError(natExperiment.getStdev(), 0.81649658092773f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertTrue(marginOfError(natExperiment.getStdev(), 2.6925824035673f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertTrue(marginOfError(natExperiment.getStdev(),  2.4819347291982f));
    }
}
