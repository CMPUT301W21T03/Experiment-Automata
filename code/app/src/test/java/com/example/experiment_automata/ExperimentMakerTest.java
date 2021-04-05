package com.example.experiment_automata;

import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentMakerTest {
    @Test
    void makeErrorNullInput() {
        assertThrows(NullPointerException.class, () -> {
            ExperimentMaker.makeExperiment(null, "funny guy eh!", 0,
                    false, false, UUID.randomUUID());
        });
    }

    @Test
    void makeBinomialExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Binomial, "Should binomial",
                0, false, false, UUID.randomUUID());
        assertEquals(BinomialExperiment.class, x.getClass());
    }

    @Test
    void makeCountExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Count, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(CountExperiment.class, x.getClass());
    }

    @Test
    void makeNaturalCountExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.NaturalCount, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(NaturalCountExperiment.class, x.getClass());
    }

    @Test
    void makeMeasurementExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Measurement, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(MeasurementExperiment.class, x.getClass());
    }
}
