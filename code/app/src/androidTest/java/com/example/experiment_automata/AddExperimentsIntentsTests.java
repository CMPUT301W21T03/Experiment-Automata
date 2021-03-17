package com.example.experiment_automata;

import android.app.Instrumentation;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AddExperimentsIntentsTests
{
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    // The needed views
    private View addExperimentButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;





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
    }

    /**
     * Here we're testing if we can see the experiment that the user has published.
     *
     * Per US 01.01.01.
     *(As an owner, I want to publish an experiment with a description, a region, and a minimum number of trials)
     *
     * Since they're 4 different types of experiments
     * let's make 1 through the GUI and the rest as automated.
     *
     * (This test is following the golden path)
     */
    @Test
    public void testMakeAnExperimentCount()
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
        solo.clickOnText("Ok");

        assertEquals("Failed to find experiment text please make sure model works",
                true,
                solo.searchText("GUI Test Experiment"));

    }

    /**
     * Here we're testing if change our minds and decide not to make an experiment.
     *
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


}
