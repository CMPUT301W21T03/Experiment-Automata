package com.example.experiment_automata;

import com.example.experiment_automata.trials.BinomialTrial;
import com.example.experiment_automata.trials.CountTrial;
import com.example.experiment_automata.trials.MeasurementTrial;
import com.example.experiment_automata.trials.NaturalCountTrial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentTest {
    private ExperimentMaker maker;
    private UUID userId;

    @BeforeEach
    public void setup() {
        maker = new ExperimentMaker();
        userId = UUID.randomUUID();
    }

    @Test
    public void countAdd() {
        CountExperiment experiment = (CountExperiment) maker.makeExperiment(ExperimentType.Count,
                "Count Experiment", 0, false, true, userId);
        experiment.recordTrial(new CountTrial(userId));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new CountTrial(userId)));
    }

    @Test
    public void naturalCountAdd() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) maker.makeExperiment(ExperimentType.NaturalCount,
                "Count Experiment", 0, false, true, userId);
        experiment.recordTrial(new NaturalCountTrial(userId, 1));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new NaturalCountTrial(userId, 2)));
    }

    @Test
    public void binomialAdd() {
        BinomialExperiment experiment = (BinomialExperiment) maker.makeExperiment(ExperimentType.Binomial,
                "Count Experiment", 0, false, true, userId);
        experiment.recordTrial(new BinomialTrial(userId, true));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new BinomialTrial(userId, false)));
    }

    @Test
    public void measurementAdd() {
        MeasurementExperiment experiment = (MeasurementExperiment) maker.makeExperiment(ExperimentType.Measurement,
                "Count Experiment", 0, false, true, userId);
        experiment.recordTrial(new MeasurementTrial(userId, 3));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new MeasurementTrial(userId, 42.0f)));
    }
}
