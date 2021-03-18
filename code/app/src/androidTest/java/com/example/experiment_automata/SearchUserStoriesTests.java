package com.example.experiment_automata;

/**
 * Test functionality the deals with
 *  1. us.05.01.01
 *  2. us.05.02.01
 *
 * Known Issues:
 *  1.
 *
 */

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;

public class SearchUserStoriesTests
{
    private Solo solo;
    private NavigationActivity currentTestingActivity;


    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
    }

    
}
