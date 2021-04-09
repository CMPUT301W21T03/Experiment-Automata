package com.example.experiment_automata;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
        private UUID testUserId;
        private String testUserName;
        private String testUserEmail;
        private String testUserPhone;
        private UUID returnUserId;
        private String returnUserName;
        private String returnUserEmail;
        private String returnUserPhone;

        @Before
        public void setUp() {
                Activity testingActivity = new Activity();
                SharedPreferences preferences = testingActivity.getSharedPreferences("experiment_automata_user_testing", MODE_PRIVATE);
                User testUser = new User(preferences);
                testUserId = testUser.getUserId();
                ContactInformation testUserInfo = testUser.getInfo();
                testUserName = testUserInfo.getName();
                testUserEmail = testUserInfo.getEmail();
                testUserPhone = testUserInfo.getPhone();
                User returnUser = new User(preferences);
                returnUserId = returnUser.getUserId();
                ContactInformation returnUserInfo = returnUser.getInfo();
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