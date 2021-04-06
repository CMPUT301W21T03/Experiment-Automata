package com.example.experiment_automata;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.experiments.NaturalCountExperiment;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class NaturalCountStatTest {

    private Solo solo;

    DataBase dataBase = DataBase.getInstanceTesting();;
    UUID ownerId = UUID.randomUUID();
    NaturalCountExperiment natExperiment=new NaturalCountExperiment("This is a test", 5, false, true, ownerId);
    UUID id = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();
    UUID id4 = UUID.randomUUID();

    NaturalCountTrial ignoreNatural = new NaturalCountTrial(id3, 3);
    NaturalCountTrial ignoreNatural2 = new NaturalCountTrial(id4, 5);

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // Reset the binomial experiment
        natExperiment = new NaturalCountExperiment("This is a test", 5, false, true, ownerId);
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

    /**
     * Function to see if two floats are close in value
     * @param first First float to compare to the second one
     * @param second Second float to compare to the first one
     * @return true if f and g are within a close margin of error and false otherwise
     */
    public boolean marginOfError(float first, float second){
        if(Math.abs(first-second)<Math.pow(10,-6)){
            // If the absolute value of the difference between first and second is within an error margin (10^(-6))
            return true;
        }
        else{
            return false;
        }
    }

    @Test
    public void getMeanTest(){
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getMean(), 2.0, 0.01);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMean(), 2.5, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertEquals(natExperiment.getMean(), 3, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertEquals(natExperiment.getMean(), 4.5, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertTrue(marginOfError(natExperiment.getMean(),  4.2f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        assertTrue(marginOfError(natExperiment.getMean(), 3.6666667f));
    }


    @Test
    public void getMedianTest(){
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getMedian(), 2.0, 0.01);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMedian(), 2.5, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertEquals(natExperiment.getMedian(), 3, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertEquals(natExperiment.getMedian(), 3.5, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getMedian(),  3, 0.01);


        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        // 1, 1, 2, 3, 3, 4, 9
        assertEquals(natExperiment.getMedian(), 3, 0.01);

        //  1, 1, 1, 2, 3, 3, 4, 9
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));
        assertEquals(natExperiment.getMedian(), 2.5, 0.01);
        // 1, 1, 1, 1, 2, 3, 3, 4, 9
        natExperiment.recordTrial(new NaturalCountTrial(id, 1));

        assertEquals(natExperiment.getMedian(), 2, 0.01);


    }

    @Test
    public void getStdevTest(){
        // Numbers for verification computed from https://www.calculator.net/standard-deviation-calculator.html
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getStdev(), 0, 0.01);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getStdev(), 0.5,0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertTrue(marginOfError(natExperiment.getStdev(), 0.81649658092773f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertTrue(marginOfError(natExperiment.getStdev(), 2.6925824035673f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertTrue(marginOfError(natExperiment.getStdev(),  2.4819347291982f));
    }

    @Test
    public void getQuartilesTest1(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php
        natExperiment.recordTrial(new NaturalCountTrial(id, 2));

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));


        natExperiment.recordTrial(new NaturalCountTrial(id, 4));

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));

        float[] quartiles = natExperiment.getQuartiles();

        assertTrue(marginOfError(quartiles[0], 2.5f));
        assertTrue(marginOfError(quartiles[1], 3.0f));
        assertTrue(marginOfError(quartiles[2], 6.5f));


    }

    @Test
    public void getQuartilesTest2(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));

        natExperiment.recordTrial(new NaturalCountTrial(id, 7));

        natExperiment.recordTrial(new NaturalCountTrial(id, 11));

        natExperiment.recordTrial(new NaturalCountTrial(id, 16));

        natExperiment.recordTrial(new NaturalCountTrial(id, 19));

        natExperiment.recordTrial(new NaturalCountTrial(id, 22));

        float[] quartiles = natExperiment.getQuartiles();
        assertEquals(quartiles[0], 7, 0.01);
        assertTrue(marginOfError(quartiles[1], 13.5f));
        assertEquals(quartiles[2], 19, 0.01);

    }

    @Test
    public void getQuartilesTest3(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));

        natExperiment.recordTrial(new NaturalCountTrial(id, 7));

        natExperiment.recordTrial(new NaturalCountTrial(id, 11));

        natExperiment.recordTrial(new NaturalCountTrial(id, 16));

        float[] quartiles = natExperiment.getQuartiles();
        assertEquals(quartiles[0], 5, 0.01);
        assertTrue(marginOfError(quartiles[1], 9f));
        assertTrue(marginOfError(quartiles[2], 13.5f));

    }

    @Test
    public void getQuartilesTest4(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));

        natExperiment.recordTrial(new NaturalCountTrial(id, 7));

        natExperiment.recordTrial(new NaturalCountTrial(id, 11));

        float[] quartiles = natExperiment.getQuartiles();
        assertEquals(quartiles[0], 3, 0.01);
        assertEquals(quartiles[1], 7, 0.01);
        assertEquals(quartiles[2], 11, 0.01);

    }


    @Test
    public void testIgnore(){
        // True values computed from https://www.calculatorsoup.com/calculators/statistics/quartile-calculator.php

        ignoreNatural.setIgnore(true);

        ignoreNatural2.setIgnore(true);

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));

        natExperiment.recordTrial(new NaturalCountTrial(id, 7));

        natExperiment.recordTrial(new NaturalCountTrial(id, 11));

        natExperiment.recordTrial(ignoreNatural);

        natExperiment.recordTrial(ignoreNatural2);

        natExperiment.recordTrial(ignoreNatural);

        natExperiment.recordTrial(ignoreNatural2);

        // These should be the same as in getQuartilesTest4

        float[] quartiles = natExperiment.getQuartiles();
        assertEquals(quartiles[0], 3, 0.01);
        assertEquals(quartiles[1], 7, 0.01);
        assertEquals(quartiles[2], 11, 0.01);

    }

    @Test
    public void testIgnore2(){
        // Numbers for verification computed from https://www.calculator.net/standard-deviation-calculator.html
        ignoreNatural.setIgnore(true);

        ignoreNatural2.setIgnore(true);

        natExperiment.recordTrial(ignoreNatural);

        natExperiment.recordTrial(ignoreNatural2);

        natExperiment.recordTrial(ignoreNatural);

        natExperiment.recordTrial(ignoreNatural2);

        // Should be the same as in the median test since the above trials are ignored

        natExperiment.recordTrial(new NaturalCountTrial(id, 2));
        assertEquals(natExperiment.getStdev(), 0, 0.01);
        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertEquals(natExperiment.getStdev(), 0.5, 0.01);

        natExperiment.recordTrial(new NaturalCountTrial(id, 4));
        assertTrue(marginOfError(natExperiment.getStdev(), 0.81649658092773f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 9));
        assertTrue(marginOfError(natExperiment.getStdev(), 2.6925824035673f));

        natExperiment.recordTrial(new NaturalCountTrial(id, 3));
        assertTrue(marginOfError(natExperiment.getStdev(),  2.4819347291982f));
    }
}
