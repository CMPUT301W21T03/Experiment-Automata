package com.example.experiment_automata;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.barcode.BinomialBarcodeReference;
import com.example.experiment_automata.backend.barcode.CountBarcodeReference;
import com.example.experiment_automata.backend.barcode.MeasurementBarcodeReference;
import com.example.experiment_automata.backend.barcode.NaturalBarcodeReference;
import com.example.experiment_automata.backend.experiments.ExperimentType;
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


public class BarcodeTest {
    private DataBase dataBase = DataBase.getInstanceTesting();
    private String barcodeVal = "123456789";
    private UUID experimentId;
    private Solo solo;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);

    @Before
    public void init(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        experimentId = UUID.randomUUID();
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
    public void testBinomialBarcodeReference(){
        BinomialBarcodeReference barcode = new BinomialBarcodeReference(barcodeVal, experimentId,  ExperimentType.Binomial, true);
        assertEquals(BinomialBarcodeReference.class.isInstance(barcode),true);
    }
    @Test
    public void testCountBarcodeReference(){
        CountBarcodeReference barcode = new CountBarcodeReference(barcodeVal, experimentId,  ExperimentType.Count);
        assertEquals(CountBarcodeReference.class.isInstance(barcode),true);
    }
    @Test
    public void testMeasurementBarcodeReference(){
        MeasurementBarcodeReference barcode = new MeasurementBarcodeReference(barcodeVal, experimentId,  ExperimentType.Measurement, (float)1);
        assertEquals(MeasurementBarcodeReference.class.isInstance(barcode),true);
    }
    @Test
    public void testNaturalBarcodeReference(){
        NaturalBarcodeReference barcode = new NaturalBarcodeReference(barcodeVal, experimentId,  ExperimentType.NaturalCount, 1);
        assertEquals(NaturalBarcodeReference.class.isInstance(barcode),true);
    }
    @Test
    public void testBarcodeSuper(){
        BinomialBarcodeReference barcode = new BinomialBarcodeReference(barcodeVal, experimentId,  ExperimentType.Binomial, true);
        assertEquals(BinomialBarcodeReference.class.isInstance(barcode),true);
        assertEquals(barcode.getBarcodeVal(),barcodeVal);
        assertEquals(barcode.getResult(),true);
        assertEquals(barcode.getExperimentId(),experimentId);
        assertEquals(barcode.getType(),ExperimentType.Binomial);
    }
}