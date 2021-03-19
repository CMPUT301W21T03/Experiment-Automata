package com.example.experiment_automata;


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

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
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

    private void makeExperiment(String des) {
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

    private void addBinomalTrial(boolean click)
    {
        solo.clickOnView(addExperimentButton);
        View binmoailTrailRadio = solo.getView(R.id.add_binomial_value);
        if(click)
            solo.clickOnView(binmoailTrailRadio);
    }


    @Test
    public void testIntentIgnoringTrials() {
        String testDes = "Testing Intent";
        makeExperiment(testDes);
        solo.clickOnText(testDes);
        solo.clickOnView(addExperimentButton);
        // Adding 5 trial
        for (int i = 0; i < 5; i++) {
            addBinomalTrial(i % 2 == 0);
        }



        assertNotEquals("Mean is the same when it should not be", 1, 1);
    }
}