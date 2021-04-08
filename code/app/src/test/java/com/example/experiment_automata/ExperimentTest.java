package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ExperimentTest {
    private final static String description = "Test Experiment";
    private final static Integer minTrials = Integer.MAX_VALUE;
    private final static Boolean requireLocation = Boolean.FALSE;
    private final static Boolean acceptNewResults = Boolean.TRUE;
    private final static UUID owner = UUID.randomUUID();

    @Test
    public void testCountExperiment() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner);
    }

    @Test
    public void testNaturalCountExperiment() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner);
    }

    @Test
    public void testBinomialExperiment() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner);
    }

    @Test
    public void testMeasurementExperiment() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner);
    }
}
