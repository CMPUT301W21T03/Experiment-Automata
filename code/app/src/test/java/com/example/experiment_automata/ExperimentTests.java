package com.example.experiment_automata;

import com.example.experiment_automata.backend.DataBase;
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
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExperimentTests {

    private ExperimentMaker maker;
    private UUID userId;


    @Before
    public void setup() {
        maker = new ExperimentMaker();
        userId = UUID.randomUUID();
    }

    @Test
    public void countAdd() {
        CountExperiment experiment = (CountExperiment) ExperimentMaker.makeExperiment(ExperimentType.Count, "Count",
                0, false, true,  userId, false, UUID.randomUUID(), true);
        experiment.recordTrial(new CountTrial(userId));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new CountTrial(userId)));
    }

    @Test
    public void naturalCountAdd() {
        NaturalCountExperiment experiment = (NaturalCountExperiment) ExperimentMaker.makeExperiment(ExperimentType.NaturalCount,
                "Count Experiment", 0, false, true, userId, false, UUID.randomUUID(), true);
        experiment.recordTrial(new NaturalCountTrial(userId, 1));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new NaturalCountTrial(userId, 2)));
    }

    @Test
    public void binomialAdd() {
        BinomialExperiment experiment = (BinomialExperiment) ExperimentMaker.makeExperiment(ExperimentType.Binomial,
                "Count Experiment", 0, false, true, userId, false, UUID.randomUUID(), true);
        experiment.recordTrial(new BinomialTrial(userId, true));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new BinomialTrial(userId, false)));
    }

    @Test
    public void measurementAdd() {
        MeasurementExperiment experiment = (MeasurementExperiment) ExperimentMaker.makeExperiment(ExperimentType.Measurement,
                "Count Experiment", 0, false, true, userId, false, UUID.randomUUID(), true);
        experiment.recordTrial(new MeasurementTrial(userId, 3));
        experiment.setActive(false);
        assertThrows(IllegalStateException.class, () -> experiment.recordTrial(new MeasurementTrial(userId, 42.0f)));
    }

    @Test
    public void minTrials() {
        Integer trials = 10;
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", trials, false, false, userId, false, UUID.randomUUID(), true);
        assertEquals(trials, experiment.getMinTrials());
        trials++;
        experiment.setMinTrials(trials);
        assertEquals(trials, experiment.getMinTrials());
    }

    @Test
    public void publishing() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, userId, false, UUID.randomUUID(), true);
        assertFalse(experiment.isPublished());
        experiment.setPublished(true);
        assertTrue(experiment.isPublished());
    }

    @Test
    public void description() {
        String description = "The cake is a lie";
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                description, 0, false, false, userId, false, UUID.randomUUID(), true);
        assertEquals(description, experiment.getDescription());
        description = "Return to monke";
        experiment.setDescription(description);
        assertEquals(description, experiment.getDescription());
    }

    @Test
    public void requireLocation() {
        Experiment<?> experiment = ExperimentMaker.makeExperiment(ExperimentType.Count,
                "Experiment", 0, false, false, userId, false, UUID.randomUUID(), true);
        assertFalse(experiment.isRequireLocation());
        experiment.setRequireLocation(true);
        assertTrue(experiment.isRequireLocation());
    }
}
