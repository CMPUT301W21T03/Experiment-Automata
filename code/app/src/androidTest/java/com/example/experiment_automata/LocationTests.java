package com.example.experiment_automata;


/**
 * Some experiment user story tests done here
 *
 * US 06.01.01
 * US 06.02.01
 * US 06.03.01
 * US 06.04.01
 *
 */

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LocationTests {
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    // The needed views
    private View addExperimentButton;
    private View locationRequiredButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;
    private View slector;
    private View mapButton;

    //Needed Objects
    private ExperimentMaker maker;
    private Experiment testExperiment;
    private UUID testUUID;
    private static DataBase dataBase;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {

        dataBase = DataBase.getInstanceTesting();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();

        maker = new ExperimentMaker();
        testUUID = UUID.randomUUID();
        testExperiment = maker.makeExperiment(ExperimentType.Count,
                "Test Experiment",
                0,
                true,
                true,
                testUUID);

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
    public void testRequiredLocation(){
        // This method tests US 6.01.01 to test that a geo-location is required and 6.03.01 to see that we are warned about it
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(1000);
        addExperimentButton = solo.getView(R.id.fab_button);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();

        locationRequiredButton = solo.getView(R.id.experiment_require_location_switch);

        solo.sleep(1000);

        solo.clickOnView(locationRequiredButton);

        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);

        solo.sleep(1000);

        solo.clickOnView(acceptNewResults);

        solo.sleep(1000);

        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, "Location Required");

        solo.clickOnText("Ok");

        solo.sleep(1000);

        solo.clickOnText("Location Required");

        solo.clickOnView(addExperimentButton);

        solo.waitForDialogToOpen();

        solo.clickOnText("Okay");

        View v = solo.getView(R.id.count_trial_map_view);
        solo.clickOnView(v);

        assertNotEquals(v, null);

    }

    @Test
    public void testNotRequiredLocation(){
        // This method tests US 6.01.01 to test that when a geo-location is not required, it displays nothing
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(1000);
        addExperimentButton = solo.getView(R.id.fab_button);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();

        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);

        solo.sleep(1000);

        solo.clickOnView(acceptNewResults);

        solo.sleep(1000);

        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, "Location Not Required");

        solo.clickOnText("Ok");

        solo.sleep(1000);

        solo.clickOnText("Location Not Required");

        mapButton = solo.getView(R.id.nav_fragment_experiment_detail_view_map_button);

        solo.clickOnView(mapButton);

        TextView text = (TextView) solo.getView(R.id.map_point_view_experiment_loc_error_display);

        assertEquals(text.getText(), "This Expression Does Not Have Location Trials");

    }

    @Test
    public void testAddLocation(){
        // This method tests US 6.02.01 to add a geolocation, and 6.04.01 to see the map of geolocations 
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(1000);
        addExperimentButton = solo.getView(R.id.fab_button);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();

        locationRequiredButton = solo.getView(R.id.experiment_require_location_switch);

        solo.sleep(1000);

        solo.clickOnView(locationRequiredButton);

        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);

        solo.sleep(1000);

        solo.clickOnView(acceptNewResults);

        solo.sleep(1000);

        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, "Location Required");

        solo.clickOnText("Ok");

        solo.sleep(1000);

        solo.clickOnText("Location Required");

        solo.clickOnView(addExperimentButton);

        solo.waitForDialogToOpen();

        solo.clickOnText("Okay");

        solo.sleep(1000);

        solo.clickOnView(addExperimentButton);

        solo.sleep(1000);

        mapButton = solo.getView(R.id.nav_fragment_experiment_detail_view_map_button);

        solo.clickOnView(mapButton);

        MapView map = (MapView) solo.getView(R.id.map_point_view_fragment_map_display);

        GeoPoint g = (GeoPoint) map.getMapCenter();

        // TODO: Should we do some actual verification with what the geopoint g is?
        assertNotNull(g);



    }
}