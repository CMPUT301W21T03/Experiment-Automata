package com.example.experiment_automata;

/**
 * Test functionality the deals with
 *  1. us.05.01.01
 *  2. us.05.02.01 - Some aspects of the experiment have yet to be implemented.
 *
 * Known Issues:
 *  1. Experiment owner is right now is a single value that does not change
 *      since some aspects of the user class is not yet finished.
 *
 */

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class SearchUserStoriesTests
{
    private DataBase dataBase = DataBase.getInstanceTesting();
    private Solo solo;
    private NavigationActivity currentTestingActivity;
    private View addExperimentButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;
    private View slector;



    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
        addExperimentButton = currentTestingActivity.findViewById(R.id.fab_button);
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


    private void makeExperiment(String des) {

        addExperimentButton = solo.getView(R.id.fab_button);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();
        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        assertNotEquals("Can't find description box", null, descriptionEdit);

        //We need to fill the form to add the experiment
        //Writing description
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, des);

        //Writing min num trials
        countTrialsEdit = solo.getView(R.id.experiment_min_trials_editText);
        assertNotEquals("Can't find description box", null, countTrialsEdit);
        solo.clickOnView(countTrialsEdit);
        solo.enterText((EditText) countTrialsEdit, "3");

        //Selecting Binomial Experiment
        slector = solo.getView(R.id.experiment_type_spinner);
        solo.clickOnView(slector);
        solo.clickOnText("Binomial");

        //Setting the boxes
        location = solo.getView(R.id.experiment_require_location_switch);
        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);
        if (des != "One")
            solo.clickOnView(acceptNewResults);
        solo.clickOnText("Ok");
    }

    /**
     * test for us.05.01.01
     * Searching using some data given
     */
    @Test
    public void testSearchingUsingQuery()
    {
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        String testQuery = "One";
        makeExperiment(testQuery);
        solo.sleep(1000);
        makeExperiment("two");
        solo.sleep(1000);
        makeExperiment("three");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.sleep(1000);
        solo.enterText(0, testQuery);

        boolean checkOne = solo.searchText(testQuery, 2);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.sleep(1000);
        solo.enterText(0, "Dog");
        solo.sleep(1000);
        boolean checkTwo = solo.searchText("Dog");
        assertEquals("Error when searching query was not proper", true, checkOne && checkTwo);

    }

    /**
     * test for us.05.02.01
     * Displaying the search results based on some query.
     * Checking for active
     */
    @Test
    public void testSearchDisplayingResultsActive()
    {
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        String testQuery = "CAT";
        makeExperiment(testQuery);
        solo.sleep(2000);
        makeExperiment("two");
        solo.sleep(2000);
        makeExperiment("three");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.sleep(2000);
        solo.enterText(0, testQuery);

        boolean checkOne = solo.searchText(testQuery, 2);
        boolean statusCheck = solo.searchText("Active");
        boolean ownerId = solo.searchText("Experiment Owner");

        assertEquals("Not all fields of experiment found", true, checkOne && statusCheck && ownerId);
    }

    /**
     * test for us.05.02.01
     * Displaying the search results based on some query.
     * Checking for active
     */
    @Test
    public void testSearchDisplayingResultsInactive()
    {
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        String testQuery = "CAT";
        makeExperiment(testQuery);
        solo.sleep(2000);
        makeExperiment("two");
        solo.sleep(2000);
        makeExperiment("three");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.enterText(0, testQuery);

        boolean checkOne = solo.searchText(testQuery, 2);
        boolean statusCheck = solo.searchText("Inactive");
        boolean ownerId = solo.searchText("Experiment Owner");

        assertEquals("Not all fields of experiment found", true, checkOne && statusCheck && ownerId);
    }

}
