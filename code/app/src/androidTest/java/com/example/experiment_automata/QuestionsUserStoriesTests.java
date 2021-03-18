package com.example.experiment_automata;


/**
 * Test functionality the deals with
 *  1. us.01.01.01
 *  2.
 *
 * Known Issues:
 *  1. Since we have not added the ability to add coordinates that is not tested
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

public class QuestionsUserStoriesTests {
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


    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
        //Finding the buttons we need to press
        addExperimentButton = currentTestingActivity.findViewById(R.id.add_experiment_button);
    }

    /**
     * Testing user story - us.02.01.01
     * Here we're testing if the we're able to add questions to an experiment
     * and have them be displayed.
     */
    @Test
    public void testingAddingQuestionsExperiment()
    {
        View questionButton = null;
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

        int beforeAddingQuestionCount = currentTestingActivity.questionManager.getAllQuestions().size();
        solo.clickOnText("GUI Test Experiment");
        questionButton = solo.getView(R.id.nav_fragment_experiment_detail_view_qa_button);
        solo.clickOnView(questionButton);
        solo.clickOnView(addExperimentButton);
        View questionBox = solo.getView(R.id.frag_add_edit_question_input_box_diolog);
        solo.enterText((EditText)questionBox,"Test Question");
        solo.clickOnText("Ok");

        int afterAddingQuestionCount = currentTestingActivity.questionManager.getAllQuestions().size();
        assertEquals("Question not added make sure model works", false,
                beforeAddingQuestionCount > afterAddingQuestionCount);
    }
}