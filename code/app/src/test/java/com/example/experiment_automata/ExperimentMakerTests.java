package com.example.experiment_automata;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ExperimentMakerTests
{
    private  ExperimentMaker maker = new ExperimentMaker();
    @Test
    void makeErrorNullInput()
    {
        assertThrows(IllegalArgumentException.class, () ->
        {
            maker.makeExperiment(null, "funny guy eh!");
        });

    }

    @Test
    void makeBinomialExperiment()
    {
        try {
            Experiment x = maker.makeExperiment(ExperimentType.Binomial, "Should binomial");
            assertEquals(BinomialExperiment.class, x.getClass());
        }catch (Exception e){
            fail("Should never happen but the oh well!");
        }

    }

    @Test
    void makeCountExperiment()
    {
        try {
            Experiment x = maker.makeExperiment(ExperimentType.Count, "Should count");
            assertEquals(CountExperiment.class, x.getClass());
        }catch (Exception e){
            fail("Should never happen but the oh well!");
        }

    }

    @Test
    void makeNaturalCountExperiment()
    {
        try {
            Experiment x = maker.makeExperiment(ExperimentType.NaturalCount, "Should count");
            assertEquals(NaturalCountExperiment.class, x.getClass());
        }catch (Exception e){
            fail("Should never happen but the oh well!");
        }

    }

    @Test
    void makeMeasurementExperiment()
    {
        try {
            Experiment x = maker.makeExperiment(ExperimentType.Measurement, "Should count");
            assertEquals(MeasurementExperiment.class, x.getClass());
        }catch (Exception e){
            fail("Should never happen but the oh well!");
        }

    }

}
