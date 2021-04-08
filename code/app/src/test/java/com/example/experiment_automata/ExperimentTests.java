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
;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ExperimentTests {

    private ExperimentMaker maker;
    private UUID userId;

    private final static String description = "Test Experiment";
    private final static Integer minTrials = Integer.MAX_VALUE;
    private final static Boolean requireLocation = Boolean.FALSE;
    private final static Boolean acceptNewResults = Boolean.TRUE;
    private final static UUID owner = UUID.randomUUID();
    private final static Boolean enableFirestoreSupport = Boolean.FALSE;

    @org.junit.jupiter.api.Test
    public void testExperiment() {
        Experiment<CountTrial> experiment = (CountExperiment) ExperimentMaker.makeExperiment(
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

        maker = new ExperimentMaker();
        userId = UUID.randomUUID();
    }


    @org.junit.jupiter.api.Test
    public void testCountExperiment() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Count, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @org.junit.jupiter.api.Test
    public void testNaturalCountExperiment() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.NaturalCount, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @org.junit.jupiter.api.Test
    public void testBinomialExperiment() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Binomial, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @org.junit.jupiter.api.Test
    public void testMeasurementExperiment() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(
                ExperimentType.Measurement, description, minTrials, requireLocation,
                acceptNewResults, owner, enableFirestoreSupport);
        assertNotNull(experiment);
    }

    @org.junit.jupiter.api.Test
    public void countAdd() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Count Experiment", 0, false, true, owner, false);
        assert experiment != null;
        experiment.recordTrial(new CountTrial(owner));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new CountTrial(owner)));
    }

    @org.junit.jupiter.api.Test
    public void naturalCountAdd() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(ExperimentType.NaturalCount,
                "Count Experiment", 0, false, true, owner, false);
        assert experiment != null;
        experiment.recordTrial(new NaturalCountTrial(owner, 1));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new NaturalCountTrial(owner, 2)));
    }

    @org.junit.jupiter.api.Test
    public void binomialAdd() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(ExperimentType.Binomial,
                "Count Experiment", 0, false, true, owner, false);
        assert experiment != null;
        experiment.recordTrial(new BinomialTrial(owner, true));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new BinomialTrial(owner, false)));
    }

    @org.junit.jupiter.api.Test
    public void measurementAdd() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(ExperimentType.Measurement,
                "Count Experiment", 0, false, true, owner, false);
        assert experiment != null;
        experiment.recordTrial(new MeasurementTrial(owner, 3));
        experiment.setActive(false);
        assertFalse(experiment.isActive());
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new MeasurementTrial(owner, 42.0f)));
    }

    @org.junit.jupiter.api.Test
    public void minTrials() {
        Integer trials = 10;
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", trials, false, false, owner, false);
        assert experiment != null;
        Assert.assertEquals(trials, experiment.getMinTrials());
        trials++;
        experiment.setMinTrials(trials);
        Assert.assertEquals(trials, experiment.getMinTrials());
    }

    @org.junit.jupiter.api.Test
    public void publishing() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, owner, false);
        assert experiment != null;
        assertFalse(experiment.isPublished());
        experiment.setPublished(true);
        assertTrue(experiment.isPublished());
    }

    @org.junit.jupiter.api.Test
    public void description() {
        String description = "The cake is a lie";
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                description, 0, false, false, owner, false);
        assert experiment != null;
        Assert.assertEquals(description, experiment.getDescription());
        description = "Return to monke";
        experiment.setDescription(description);
        Assert.assertEquals(description, experiment.getDescription());
    }

    @org.junit.jupiter.api.Test
    public void requireLocation() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, owner, false);
        assert experiment != null;
        assertFalse(experiment.isRequireLocation());
        experiment.setRequireLocation(true);
        assertTrue(experiment.isRequireLocation());
    }
}
