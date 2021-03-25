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

import com.example.experiment_automata.ui.NavigationActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class SearchUserStoriesTests
{
    private Solo solo;
    private NavigationActivity currentTestingActivity;
    private View addExperimentButton;
    private View descriptionEdit;
    private View countTrialsEdit;
    private View location;
    private View acceptNewResults;



    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
        addExperimentButton = currentTestingActivity.findViewById(R.id.fab_button);
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
     * test for us.05.01.01
     * Searching using some data given
     */
    @Test
    public void testSearchingUsingQuery()
    {
        String testQuery = "One";
        makeExperiment(testQuery);
        makeExperiment("two");
        makeExperiment("three");
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.enterText(0, testQuery);

        boolean checkOne = solo.searchText(testQuery, 2);
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.enterText(0, "Dog");
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
        String testQuery = "CAT";
        makeExperiment(testQuery);
        makeExperiment("two");
        makeExperiment("three");
        solo.clickOnView(solo.getView(R.id.app_bar_search));
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
        String testQuery = "One";
        makeExperiment(testQuery);
        makeExperiment("two");
        makeExperiment("three");
        solo.clickOnView(solo.getView(R.id.app_bar_search));
        solo.enterText(0, testQuery);

        boolean checkOne = solo.searchText(testQuery, 2);
        boolean statusCheck = solo.searchText("Inactive");
        boolean ownerId = solo.searchText("Experiment Owner");

        assertEquals("Not all fields of experiment found", true, checkOne && statusCheck && ownerId);
    }

}
