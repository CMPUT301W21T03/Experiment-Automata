package com.example.experiment_automata;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

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
        solo.clickOnImageButton(0);
        solo.waitForText(solo.getString(R.string.menu_profile));
        // Navigate to profile
        solo.clickOnView(solo.getView(R.id.nav_profile));
    }

    @Test
    public void checkProfile() {
        solo.waitForFragmentById(R.id.profile_screen);
        assertEquals(Screen.Profile, currentTestingActivity.getCurrentScreen());
        assertEquals(solo.getString(R.string.profile_contact),
                ((TextView) solo.getView(R.id.profile_contact)).getText().toString());
    }

    @Test
    public void editProfile() {
        solo.waitForFragmentById(R.id.profile_screen);
        // click on edit button
        solo.clickOnImageButton(1);
        // generate new random phone number
        Random random = new Random();
        String newPhoneNumber = String.format("780-%03d-%04d",
                random.nextInt(1000), random.nextInt(10000));
        // change phone number
        EditText phoneNumberField = (EditText) solo.getView(R.id.edit_phone);
        solo.clearEditText(phoneNumberField);
        solo.enterText(phoneNumberField, newPhoneNumber);
        solo.clickOnText("Ok");
        // check for new phone number
        assertTrue(solo.searchText(newPhoneNumber));
    }
}
