package com.example.experiment_automata;

import android.util.Log;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some user profile user story tests done here
 *
 * US 04.01.01
 * US 04.02.01
 *
 */
public class UserTests {
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);

    @Before
    public void setup() throws InterruptedException {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();
        // Open navigation drawer
        solo.clickOnScreen(72, 140); // tested on Pixel 3a
        solo.waitForText(solo.getString(R.string.menu_profile));
        // Navigate to profile
        solo.clickOnView(solo.getView(R.id.nav_profile));
    }

    @Test
    public void checkProfile() {
        assertEquals(Screen.Profile, currentTestingActivity.getCurrentScreen());
        assertEquals(solo.getString(R.string.profile_contact),
                ((TextView) solo.getView(R.id.profile_contact)).getText().toString());
    }
}
