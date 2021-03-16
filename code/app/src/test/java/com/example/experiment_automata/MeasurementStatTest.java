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

public class MeasurementStatTest {

    UUID ownerId = UUID.randomUUID();
    MeasurementExperiment mesExperiment=new MeasurementExperiment("This is a test", 5, false, true, ownerId);
    UUID id = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    @Before
    public void setup() {
        // Reset the binomial experiment
        mesExperiment = new MeasurementExperiment("This is a test", 5, false, true, ownerId);
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
        mesExperiment.recordTrial(new MeasurementTrial(id, 2.4f));
        assertEquals(mesExperiment.getMean(), 2.4f);
        mesExperiment.recordTrial(new MeasurementTrial(id, 3.1f));
        assertTrue(marginOfError(mesExperiment.getMean(), 2.75f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 4.61f));
        assertTrue(marginOfError(mesExperiment.getMean(), 3.37f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 9f));
        assertTrue(marginOfError(mesExperiment.getMean(), 4.7775f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 14.12f));
        assertTrue(marginOfError(mesExperiment.getMean(), 6.646f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 16.1234f));
        assertTrue(marginOfError(mesExperiment.getMean(), 8.225566666666667f));
    }


    @Test
    public void getMedianTest(){
        mesExperiment.recordTrial(new MeasurementTrial(id, 2.4f));
        assertEquals(mesExperiment.getMedian(), 2.4f);
        mesExperiment.recordTrial(new MeasurementTrial(id, 3.1f));
        assertTrue(marginOfError(mesExperiment.getMedian(), 2.75f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 4.61f));
        assertTrue(marginOfError(mesExperiment.getMedian(), 3.1f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 9f));
        assertTrue(marginOfError(mesExperiment.getMedian(), 3.855f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 14.12f));
        assertTrue(marginOfError(mesExperiment.getMedian(), 4.61f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 16.1234f));
        assertTrue(marginOfError(mesExperiment.getMedian(), 6.805f));
    }

    @Test
    public void getStdevTest(){
        // Numbers for verification were computed from https://www.calculator.net/standard-deviation-calculator.html
        mesExperiment.recordTrial(new MeasurementTrial(id, 2.4f));
        assertEquals(mesExperiment.getStdev(), 0f);

        mesExperiment.recordTrial(new MeasurementTrial(id, 3.1f));
        assertTrue(marginOfError(mesExperiment.getStdev(), 0.35f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 4.61f));
        assertTrue(marginOfError(mesExperiment.getStdev(), 0.92220749653571f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 9f));
        assertTrue(marginOfError(mesExperiment.getStdev(), 2.56534963504f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 14.12f));
        assertTrue(marginOfError(mesExperiment.getStdev(), 4.3852005655386f));

        mesExperiment.recordTrial(new MeasurementTrial(id, 16.1234f));
        assertTrue(marginOfError(mesExperiment.getStdev(), 5.3385523261357f));
    }
}
