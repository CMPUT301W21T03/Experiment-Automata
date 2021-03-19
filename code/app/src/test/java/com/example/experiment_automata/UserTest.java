package com.example.experiment_automata;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.experiment_automata.UserInformation.ContactInformation;
import com.example.experiment_automata.UserInformation.User;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserTest {
        private Activity testingActivity;
        private SharedPreferences preferences;
        private User testUser;
        private UUID testUserId;
        private ContactInformation testUserInfo;
        private String testUserName;
        private String testUserEmail;
        private String testUserPhone;
        private User returnUser;
        private UUID returnUserId;
        private ContactInformation returnUserInfo;
        private String returnUserName;
        private String returnUserEmail;
        private String returnUserPhone;

        @Before
        public void setUp() {
                testingActivity = new Activity();
                preferences = testingActivity.getSharedPreferences("experiment_automata_user_testing", MODE_PRIVATE);
                testUser = new User(preferences);
                testUserId = testUser.getUserId();
                testUserInfo = testUser.getInfo();
                testUserName = testUserInfo.getName();
                testUserEmail = testUserInfo.getEmail();
                testUserPhone = testUserInfo.getPhone();
                returnUser = new User(preferences);
                returnUserId = returnUser.getUserId();
                returnUserInfo = returnUser.getInfo();
                returnUserName = returnUserInfo.getName();
                returnUserEmail = returnUserInfo.getEmail();
                returnUserPhone = returnUserInfo.getPhone();
        }

        @Test
        public void checkNewUserId() {
                assertEquals(testUserId, returnUserId);
        }

        @Test
        public void checkNewUserName() {
                assertEquals(testUserName, returnUserName);
        }

        @Test
        public void checkNewUserEmail() {
                assertEquals(testUserEmail, returnUserEmail);
        }

        @Test
        public void checkNewUserPhone() {
                assertEquals(testUserPhone, returnUserPhone);
        }
}