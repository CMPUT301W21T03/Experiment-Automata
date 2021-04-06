package com.example.experiment_automata;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.experiments.BinomialExperiment;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public class ExperimentMakerTestsFirebase
{
    DataBase dataBase = DataBase.getInstanceTesting();;
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        dataBase.getFireStore().disableNetwork();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();

        /**
         * Sources
         * Author:https://stackoverflow.com/users/7699270/nur-el-din
         * Editor:https://stackoverflow.com/users/6463791/satan-pandeya
         * Full:https://stackoverflow.com/questions/15993314/clicking-on-action-bar-menu-items-in-robotium
         */
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @After
    public void endTest() throws NoSuchFieldException, IllegalAccessException {
        dataBase.getFireStore().disableNetwork();
        dataBase.getFireStore().terminate();
        dataBase.getFireStore().clearPersistence();
        solo.finishOpenedActivities();
        Field testMode = DataBase.class.getDeclaredField("testMode");
        Field currentInstence = DataBase.class.getDeclaredField("current");
        Field dbInstence = DataBase.class.getDeclaredField("db");
        testMode.setAccessible(true);
        currentInstence.setAccessible(true);
        dbInstence.setAccessible(true);
        testMode.setBoolean(dataBase, true);
        currentInstence.set(currentInstence, null);
        dbInstence.set(dbInstence, null);
        dataBase = DataBase.getInstanceTesting();
    }


    @Test
    public void makeErrorNullInput() {
        assertThrows(NullPointerException.class, () -> {
            ExperimentMaker.makeExperiment(null, "funny guy eh!", 0,
                    false, false, UUID.randomUUID());
        });
    }

    @Test
    public void makeBinomialExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Binomial, "Should binomial",
                0, false, false, UUID.randomUUID());
        assertEquals(BinomialExperiment.class, x.getClass());
    }

    @Test
    public void makeCountExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Count, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(CountExperiment.class, x.getClass());
    }

    @Test
    public void makeNaturalCountExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.NaturalCount, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(NaturalCountExperiment.class, x.getClass());
    }

    @Test
    public void makeMeasurementExperiment() {
        Experiment<?> x = ExperimentMaker.makeExperiment(ExperimentType.Measurement, "Should count",
                0, false, false, UUID.randomUUID());
        assertEquals(MeasurementExperiment.class, x.getClass());
    }
}
