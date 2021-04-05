package com.example.experiment_automata;


/**
 * Some experiment user story tests done here
 *
 * US 01.06.01
 * US 01.07.01
 * US 01.08.01
 * US 01.09.01
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

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BinomialTrialTests {
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    // The needed views
    private View addExperimentButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;
    private View slector;

    //Needed Objects
    private ExperimentMaker maker;
    private Experiment testExperiment;
    private UUID testUUID;
    private DataBase dataBase;

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
    public void endTest() {
        dataBase.getFireStore().terminate();
        dataBase.getFireStore().clearPersistence();
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

        //Selecting Binomial Experiment
        slector = solo.getView(R.id.experiment_type_spinner);
        solo.clickOnView(slector);
        solo.clickOnText("Binomial");

        //Setting the boxes
        location = solo.getView(R.id.experiment_require_location_switch);
        acceptNewResults = solo.getView(R.id.experiment_accept_new_results_switch);
        solo.clickOnView(location);
        if (des != "One")
            solo.clickOnView(acceptNewResults);
        solo.clickOnText("Ok");
    }

    private void addBinomialTrial(boolean click) {
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.fab_button));
        solo.sleep(2000);
        if(click) {
            solo.waitForText("Passed");
            solo.clickOnText("Passed");
        }
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.fab_button));
        solo.sleep(2000);
    }


    /**
     * Testing US 01.08.01 here we're ignoring a trial and making sure the
     * quartile sentence changes to meet that when the trial is ignored
     */
    @Test
    public void testIntentIgnoringTrials() {

        //Running without the DB
        //currentTestingActivity.experimentManager.enableTestMode();
        //currentTestingActivity.questionManager.enableTestMode();
        //currentTestingActivity.userManager.enableTestMode();
        //currentTestingActivity.stopRemindingMe = true;

        String testDes = "Testing Intent";
        makeExperiment(testDes);
        solo.clickOnText(testDes);
        // Adding 3 trial
        for (int i = 0; i < 3; i++) {
            addBinomialTrial(i % 2 == 0);
        }
        View qur = solo.getView(R.id.quartiles_text);
        String qur1 = ((TextView)qur).getText().toString();
        solo.clickOnView(solo.getView(R.id.trial_ignore_checkbox));
        View qur2 = solo.getView(R.id.mean_value);
        String qur3 = ((TextView)qur2).getText().toString();

        assertNotEquals("Mean is the same when it should not be", qur1, qur3);

    }

    /**
     * Testing US 01.09.01 to see if IQR values are displayed correctly.
     * After finish adding some number of trials
     */
    @Test
    public void testIntentIQRNumbersAppear() {

        //Running without the DB
        //currentTestingActivity.experimentManager.enableTestMode();
        //currentTestingActivity.questionManager.enableTestMode();
        //currentTestingActivity.userManager.enableTestMode();
        //currentTestingActivity.stopRemindingMe = true;

        Double medianTestValue = 1.000;
        Double meanTestValue = 1.000;
        String testDes = "Testing Intent";
        makeExperiment(testDes);
        solo.clickOnText(testDes);
        // Adding 4 trial
        for (int i = 0; i < 4; i++) {
            addBinomialTrial(true);
        }
        String quertile = ((TextView)solo.getView(R.id.quartiles_value)).getText().toString();
        Double median = new Double(((TextView)solo.getView(R.id.median_value)).getText().toString());
        Double mean = new Double(((TextView)solo.getView(R.id.mean_value)).getText().toString());

        assertEquals("Mean wrong", meanTestValue, mean, 0.00001);
        assertEquals("Median wrong", medianTestValue, median, 0.00001);
        assertNotEquals("Quartile set wrong", null, quertile);
    }

    /**
     * Testing US 01.06.01 check to see if the title shows as we cannot
     * really compare graphs without overcomplicated results
     */
    @Test
    public void testIntentChartHistogramDataDisplayed() {

        String testDes = "Testing Intent";
        makeExperiment(testDes);
        solo.clickOnText(testDes);
        addBinomialTrial(true);
        assertTrue("Chart data not displayed", solo.searchText("Histogram"));
    }

    /**
     * Testing US 01.07.01 check to see if the title shows as we cannot
     * really compare graphs without overcomplicated results
     */
    @Test
    public void testIntentChartPlotDataDisplayed() {

        String testDes = "Testing Intent";
        makeExperiment(testDes);
        solo.clickOnText(testDes);
        addBinomialTrial(true);
        assertTrue("Chart data not displayed", solo.searchText("Results"));
    }
}