package com.example.experiment_automata;

import com.example.experiment_automata.Experiments.ExperimentModel.BinomialExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.CountExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentMaker;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentType;
import com.example.experiment_automata.Experiments.ExperimentModel.MeasurementExperiment;
import com.example.experiment_automata.Experiments.ExperimentModel.NaturalCountExperiment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentMakerTest {
    private ExperimentMaker maker = new ExperimentMaker();

    @Test
    void makeErrorNullInput() {
        assertThrows(NullPointerException.class, () -> {
            maker.makeExperiment(null, "funny guy eh!");
        });
    }

    @Test
    void makeBinomialExperiment() {
        Experiment x = maker.makeExperiment(ExperimentType.Binomial, "Should binomial");
        assertEquals(BinomialExperiment.class, x.getClass());
    }

    @Test
    void makeCountExperiment() {
        Experiment x = maker.makeExperiment(ExperimentType.Count, "Should count");
        assertEquals(CountExperiment.class, x.getClass());
    }

    @Test
    void makeNaturalCountExperiment() {
        Experiment x = maker.makeExperiment(ExperimentType.NaturalCount, "Should count");
        assertEquals(NaturalCountExperiment.class, x.getClass());
    }

    @Test
    void makeMeasurementExperiment() {
        Experiment x = maker.makeExperiment(ExperimentType.Measurement, "Should count");
        assertEquals(MeasurementExperiment.class, x.getClass());
    }
}
