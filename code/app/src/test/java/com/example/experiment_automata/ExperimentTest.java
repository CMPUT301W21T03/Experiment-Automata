package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.Trial;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExperimentTest {
    private final static String description = "Test Experiment";
    private final static Integer minTrials = Integer.MAX_VALUE;
    private final static Boolean requireLocation = Boolean.FALSE;
    private final static Boolean acceptNewResults = Boolean.TRUE;
    private final static UUID owner = UUID.randomUUID();
    private final static Boolean enableFirestoreSupport = Boolean.FALSE;

    @Test
    public void testExperiment() {
        Experiment<CountTrial> experiment = (Experiment<CountTrial>) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
        assertEquals(description, experiment.getDescription());
        assertEquals(minTrials, experiment.getMinTrials());
        assertEquals(requireLocation, experiment.isRequireLocation());
        assertEquals(acceptNewResults, experiment.isActive());
        assertEquals(owner, experiment.getOwnerId());
        assertNotEquals(experiment, ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport)
        );
        assertEquals((Integer) 0, experiment.getSize());
        CountTrial trial = new CountTrial(owner);
        experiment.recordTrial(trial);
        assertEquals((Integer) 1, experiment.getSize());
    }

    @Test
    public void testCountExperiment() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testNaturalCountExperiment() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.NaturalCount, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testBinomialExperiment() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Binomial, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testMeasurementExperiment() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Measurement, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }
}
