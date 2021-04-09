package com.example.experiment_automata;


/**
 * Test functionality the deals with
 *  1. us.01.01.01
 *  2. us.01.02.01
 *  3. us.01.03.01
 *  4. us.01.04.01 - Not yet implemented
 *  5. us.01.05.01
 *
 * Known Issues:
 *  1. Since we have not added the ability to add coordinates that is not tested
 *  2. Not yet dealing with the owner/experimenter access values
 *
 */


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentMaker;
import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExperimentUserStoriesTests {
    DataBase dataBase = DataBase.getInstanceTesting();
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    // The needed views
    private View addExperimentButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;
    private View publishButton;

    //Needed Objects
    private ExperimentMaker maker;
    private Experiment<?> testExperiment;
    private UUID testUUID;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();

        maker = new ExperimentMaker();
        testUUID = UUID.randomUUID();
        testExperiment = maker.makeExperiment(ExperimentType.Count,
                "Test Experiment",
                0,
                true,
                true,
                testUUID,
                "Jasper",
                false);

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
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
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

        //Setting the boxes
        location = solo.getView(R.id.experiment_require_location_switch);
        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);
        if (des != "One")
            solo.clickOnView(acceptNewResults);
        solo.clickOnText("Ok");
    }

    /**
     * Here we're testing if we can see the experiment that the user has published.
     *
     * Per US 01.01.01.
     *(As an owner, I want to publish an experiment with a description, a region, and a minimum number of trials)
     *
     * Since they're 4 different types of experiments
     * let's make 1 through the GUI and the rest as automated unit tests since it's the same
     * idea except here we're testing the GUI.
     *
     * (This test is following the golden path)
     */
    @Test
    public void testMakeAnExperimentCount() {

        makeExperiment("GUI Test Experiment");
        //Clicking on publish button
        solo.sleep(1000);
        publishButton = solo.getView(R.id.publishedCheckbox);

        solo.clickOnView(publishButton);
        solo.sleep(1000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");


        assertEquals("Failed to find experiment text please make sure model works",
                true,
                solo.searchText("GUI Test Experiment"));

    }

    /**
     * Here we're testing if change our minds and decide not to make an experiment.
     *(Testing what happens if we cancel after we make an experiment )
     * per(us.01.01.01)
     */
    @Test
    public void testMakeExperimentCancel() {
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        addExperimentButton = solo.getView(R.id.fab_button);
        assertNotEquals("Can't find + button", null, addExperimentButton);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();
        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        assertNotEquals("Can't find description box", null, descriptionEdit);

        //We need to fill the form to add the experiment
        //Writing description
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, "GUI Test Experiment");
        //Writing min num trials

        countTrialsEdit = solo.getView(R.id.experiment_min_trials_editText);
        assertNotEquals("Can't find description box", null, countTrialsEdit);
        solo.clickOnView(countTrialsEdit);
        solo.enterText((EditText) countTrialsEdit, "3");

        //Setting the boxes
        location = solo.getView(R.id.experiment_require_location_switch);
        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);
        solo.clickOnView(location);
        solo.clickOnView(acceptNewResults);
        solo.clickOnText("Cancel");

        assertEquals("Failed to find experiment text please sure cancel logic works",
                false,
                solo.searchText("GUI Test Experiment"));
    }

    /**
     * Here we're trying to see if the user can enter an empty experiment into the their
     * published views.
     * per(us.01.01.01)
     */
    @Test
    public void makeEmptyExperiment() {
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        addExperimentButton = solo.getView(R.id.fab_button);
        assertNotEquals("Can't find + button", null, addExperimentButton);

        //Click from the home screen the + button to make an experiment
        solo.clickOnView(addExperimentButton);
        solo.waitForDialogToOpen();
        descriptionEdit = solo.getView(R.id.create_experiment_description_editText);
        assertNotEquals("Can't find description box", null, descriptionEdit);

        //We need to fill the form to add the experiment
        //Writing description
        solo.clickOnView(descriptionEdit);
        solo.enterText((EditText) descriptionEdit, "");
        //Writing min num trials

        countTrialsEdit = solo.getView(R.id.experiment_min_trials_editText);
        assertNotEquals("Can't find description box", null, countTrialsEdit);
        solo.clickOnView(countTrialsEdit);
        solo.enterText((EditText) countTrialsEdit, "");

        //Setting the boxes
        location = solo.getView(R.id.experiment_require_location_switch);
        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);
        solo.clickOnView(location);
        solo.clickOnView(acceptNewResults);
        solo.clickOnText("Ok");

        assertEquals("Empty experiment not displayed",
                false,
                solo.searchText("BAD"));
    }

    /**
     * Here we're testing if we can un-publish an experiment that the
     * user had made.
     * per(us.01.02.01)
     */
    @Test
    public void testingUnpublishedExperiment() {
        makeExperiment("GUI Unpub Experiment");


        //Clicking on publish button
        publishButton = solo.getView(R.id.publishedCheckbox);
        solo.clickOnView(publishButton);
        solo.sleep(2000);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");
        solo.sleep(2000);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.sleep(2000);
        solo.clickOnView(publishButton);


        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");
        solo.sleep(2000);

        assertEquals("Failed un-publish functionality still displays unpublished experiment",
                false,
                solo.searchText("GUI Unpub Experiment"));
    }

    /**
     * Testing if we can mark an experiment as inactive so that we can
     * still see the results of that experiment but not add anymore data.
     * As per (us.01.03.01)
     */
    @Test
    public void testingIfWeCanEndAnExperiment() {
        View editButton = null;
        makeExperiment("GUI Test Experiment");

        //Clicking on experiment
        solo.clickOnText("GUI Test Experiment");
        editButton = solo.getView(R.id.nav_fragment_experiment_detail_view_edit_button);
        assertNotEquals("Could not find edit button please check view", null, editButton);
        solo.clickOnView(editButton);
        solo.clickOnView(acceptNewResults);
        solo.clickOnText("Ok");

        solo.clickOnActionBarHomeButton();

        assertEquals("Failed to disable experiment: Please make sure bools are checked",
                true,
                solo.searchText("Inactive"));
    }

    /**
     * Testing if we can add trials to an experiment
     * as per(us.01.05.01)
     */
    @Test
    public void testingAddingTrials() {
        Experiment<?> current = null;
        String des = "Evil String";
        makeExperiment(des);
        solo.sleep(2000);
        current = currentTestingActivity.experimentManager.getLastAdded();

        int trialSizeBefore = current.getSize();
        solo.clickOnText(des);
        solo.sleep(2000);
        solo.clickOnView(addExperimentButton);
        solo.sleep(2000);

        solo.clickOnView(addExperimentButton);
        solo.sleep(2000);

        solo.sleep(2000);
        int trialSizeAfter = currentTestingActivity.experimentManager.getExperiment(current.getExperimentId()).getSize();
        solo.sleep(2000);

        assertEquals("Trials not added", true, trialSizeAfter > trialSizeBefore);
    }

    /**
     * Testing if we can navigate to an experimenter's profile from the trial result
     */
    @Test
    public void testingLinkToExperimenter() {
        String experimentDescription = "GUI Test Experiment";
        makeExperiment(experimentDescription);
        solo.sleep(2000);
        solo.clickOnText(experimentDescription);
        solo.clickOnView(addExperimentButton);
        solo.clickOnView(addExperimentButton);
        User user = currentTestingActivity.loggedUser;
        solo.clickOnText(user.getInfo().getName());
        solo.waitForFragmentById(R.id.profile_screen);
        assertEquals(Screen.Profile, currentTestingActivity.getCurrentScreen());
        assertEquals(solo.getString(R.string.profile_contact),
                ((TextView) solo.getView(R.id.profile_contact)).getText().toString());
    }
}
