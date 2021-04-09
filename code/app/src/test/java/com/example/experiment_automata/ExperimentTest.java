package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;
import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExperimentTest {
    private final static String description = "Test Experiment";
    private final static String region = "San Jose";
    private final static Integer minTrials = Integer.MAX_VALUE;
    private final static Boolean requireLocation = Boolean.FALSE;
    private final static Boolean acceptNewResults = Boolean.TRUE;
    private final static UUID owner = UUID.randomUUID();
    private final static Boolean enableFirestoreSupport = Boolean.FALSE;

    @Test
    public void testExperiment() {
        Experiment<CountTrial> experiment = (CountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, region, enableFirestoreSupport);
        assertNotNull(experiment);
        assertEquals(description, experiment.getDescription());
        assertEquals(region, experiment.getRegion());
        assertEquals(minTrials, experiment.getMinTrials());
        assertEquals(requireLocation, experiment.isRequireLocation());
        assertEquals(acceptNewResults, experiment.isActive());
        assertEquals(owner, experiment.getOwnerId());
        assertNotEquals(experiment, ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, region, enableFirestoreSupport)
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
                acceptNewResults, owner, region, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testNaturalCountExperiment() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.NaturalCount, description, minTrials, requireLocation,
                acceptNewResults, owner, region, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testBinomialExperiment() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Binomial, description, minTrials, requireLocation,
                acceptNewResults, owner, region, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void testMeasurementExperiment() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Measurement, description, minTrials, requireLocation,
                acceptNewResults, owner, region, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @Test
    public void countAdd() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Count Experiment", 0, false, true, owner, region, false);
        assert experiment != null;
        experiment.recordTrial(new CountTrial(owner));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new CountTrial(owner)));
    }

    @Test
    public void naturalCountAdd() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(ExperimentType.NaturalCount,
                "Count Experiment", 0, false, true, owner, region, false);
        assert experiment != null;
        experiment.recordTrial(new NaturalCountTrial(owner, 1));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new NaturalCountTrial(owner, 2)));
    }

    @Test
    public void binomialAdd() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(ExperimentType.Binomial,
                "Count Experiment", 0, false, true, owner, region, false);
        assert experiment != null;
        experiment.recordTrial(new BinomialTrial(owner, true));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new BinomialTrial(owner, false)));
    }

    @Test
    public void measurementAdd() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(ExperimentType.Measurement,
                "Count Experiment", 0, false, true, owner, region, false);
        assert experiment != null;
        experiment.recordTrial(new MeasurementTrial(owner, 3));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new MeasurementTrial(owner, 42.0f)));
    }

    @Test
    public void minTrials() {
        int trials = 10;
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", trials, false, false, owner, region, false);
        assert experiment != null;
        assertEquals(trials, (int) experiment.getMinTrials());
        trials++;
        experiment.setMinTrials(trials);
        assertEquals(trials, (int) experiment.getMinTrials());
    }

    @Test
    public void publishing() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, owner, region, false);
        assert experiment != null;
        assertFalse(experiment.isPublished());
        experiment.setPublished(true);
        assertTrue(experiment.isPublished());
    }

    @Test
    public void description() {
        String description = "The cake is a lie";
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                description, 0, false, false, owner, region, false);
        assert experiment != null;
        assertEquals(description, experiment.getDescription());
        description = "Return to monke";
        experiment.setDescription(description);
        assertEquals(description, experiment.getDescription());
    }

    @Test
    public void region() {
        String region = "Canada";
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                description, 0, false, false, owner, region, false);
        assert experiment != null;
        assertEquals(region, experiment.getRegion());
        region = "Return to monke";
        experiment.setRegion(region);
        assertEquals(region, experiment.getRegion());
    }

    @Test
    public void requireLocation() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, owner, region, false);
        assert experiment != null;
        assertFalse(experiment.isRequireLocation());
        experiment.setRequireLocation(true);
        assertTrue(experiment.isRequireLocation());
    }
}
