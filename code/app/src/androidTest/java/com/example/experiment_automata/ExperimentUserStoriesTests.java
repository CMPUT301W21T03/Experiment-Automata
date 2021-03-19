package com.example.experiment_automata;


/**
 * Test functionality the deals with
 *  1. us.01.01.01
 *  2. us.01.02.01
 *  3. us.01.03.01
 *  4. us.01.04.01 - Not yet implemented
 *  5. us.01.05.01
 *  6. us.01.06.01 - Not yet implemented
 *  7. us.01.07.01 - Not yet implemented
 *  8. us.01.08.01 - Not yet implemented
 *  9. us.01.09.01 - Not yet implemented
 *
 * Known Issues:
 *  1. Since we have not added the ability to add coordinates that is not tested
 *  2. Not yet dealing with the owner/experimenter access values
 *
 */


import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentMaker;
import com.example.experiment_automata.Experiments.ExperimentModel.ExperimentType;
import com.example.experiment_automata.ui.home.HomeFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class ExperimentUserStoriesTests
{
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
    private Experiment testExperiment;
    private UUID testUUID;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup()
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
        //Finding the buttons we need to press
        addExperimentButton = currentTestingActivity.findViewById(R.id.add_experiment_button);
        maker = new ExperimentMaker();
        testUUID = UUID.randomUUID();
        testExperiment = maker.makeExperiment(ExperimentType.Count,
                "Test Experiment",
                0,
                true,
                true,
                testUUID);

    }

    private void makeExperiment(String des)
    {
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
        solo.clickOnView(location);
        if(des != "One")
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
    public void testMakeAnExperimentCount()
    {
        makeExperiment("GUI Test Experiment");
        //Clicking on publish button
        publishButton = solo.getView(R.id.publishedCheckbox);
        solo.clickOnView(publishButton);

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
    public void testMakeExperimentCancel()
    {
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
    public void makeEmptyExperiment()
    {
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


        //Clicking on publish button
        publishButton = solo.getView(R.id.publishedCheckbox);
        solo.clickOnView(publishButton);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");

        assertEquals("Empty experiment not displayed",
                true,
                solo.searchText("Experiment Owner"));
    }

    /**
     * Here we're testing if we can un-publish an experiment that the
     * user had made.
     * per(us.01.02.01)
     */
    @Test
    public void testingUnpublishedExperiment()
    {
        makeExperiment("GUI Test Experiment");

        //Clicking on publish button
        publishButton = solo.getView(R.id.publishedCheckbox);
        solo.clickOnView(publishButton);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");


        solo.clickOnActionBarHomeButton();
        solo.clickOnText("My Experiments");
        solo.clickOnView(publishButton);


        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Published Experiments");

        assertEquals("Failed un-publish functionality still displays unpublished experiment",
                false,
                solo.searchText("GUI Test Experiment"));
    }

    /**
     * Testing if we can mark an experiment as inactive so that we can
     * still see the results of that experiment but not add anymore data.
     * As per (us.01.03.01)
     */
    @Test
    public void testingIfWeCanEndAnExperiment()
    {
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
    public void testingAddingTrials()
    {
        Experiment current = null;
        makeExperiment("GUI Test Experiment");
        if(currentTestingActivity.experimentManager.getAllExperiments().size() > 0) {
            current = currentTestingActivity.experimentManager.getAllExperiments().get(0);

            int trialSizeBefore = current.getSize();
            solo.clickOnText(current.getDescription());
            solo.clickOnView(addExperimentButton);
            solo.clickOnView(addExperimentButton);
            solo.clickOnActionBarHomeButton();
            int trialSizeAfter = current.getSize();

            assertEquals("Trials not added", true, trialSizeAfter > trialSizeBefore);
        }
        else
            fail("Should never happen: if it does it's error with robotium");
    }
}
