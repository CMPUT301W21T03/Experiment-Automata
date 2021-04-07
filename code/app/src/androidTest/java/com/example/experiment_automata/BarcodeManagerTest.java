package com.example.experiment_automata;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.barcode.BarcodeManager;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

public class BarcodeManagerTest {
    private DataBase dataBase = DataBase.getInstanceTesting();
    private String barcodeVal = "123456789";
    private UUID experimentId;
    private Solo solo;
    private BarcodeManager barcodeManager;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);

    @Before
    public void init(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        experimentId = UUID.randomUUID();
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getTargetContext());
        barcodeManager = BarcodeManager.getInstance();
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
        Field barcodeManagerField = BarcodeManager.class.getDeclaredField("barcodeManager");
        barcodeManagerField.setAccessible(true);
        barcodeManagerField.set(null,null);
    }

    @Test
    public void testAdd(){
    }
}
