package com.example.experiment_automata;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.experiment_automata.backend.DataBase;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuestionTestsFireBase {

    DataBase dataBase = DataBase.getInstanceTesting();;
    private Solo solo;
    private NavigationActivity currentTestingActivity;

    private UUID experimentId;
    private UUID userId;

    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<>(NavigationActivity.class, true, true);


    @Before
    public void setup() {
        dataBase.getFireStore().disableNetwork();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.requireActivity());
        currentTestingActivity = (NavigationActivity) solo.getCurrentActivity();

        userId = UUID.randomUUID();
        experimentId = UUID.randomUUID();

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


    @Test
    public void createQuestion() {
        Question test = new Question("test", userId, experimentId);
        assertNotNull(test);
    }

    @Test
    public void getQuestion() {
        Question test = new Question("test", userId, experimentId);
        assertEquals("test", test.getQuestion());
    }

    @Test
    public void getQuestionId() {
        Question test = new Question("test", userId, experimentId);
        UUID testId = test.getQuestionId();
        assertNotNull(testId);
    }

    @Test
    public void getQuestionUser() {
        Question test = new Question("test", userId, experimentId);
        UUID testUser = test.getUser();
        assertNotNull(testUser);
        assertEquals(userId, testUser);
    }

    @Test
    public void getQuestionExperiment() {
        Question test = new Question("test", userId, experimentId);
        UUID testExperiment = test.getExperimentId();
        assertNotNull(testExperiment);
        assertEquals(experimentId, testExperiment);
    }

}
