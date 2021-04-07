package com.example.experiment_automata;

import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class UserModelTests
{

    private User testUser;
    private UUID testId;
    private ContactInformation contactInformation;

    @Before
    public void makeTestUser()
    {
        testId = UUID.randomUUID();
        contactInformation =
                new ContactInformation("1", "2", "3");
        testUser = new User(true, contactInformation, testId);
    }

    @Test
    public void testGetInstance()
    {
        User testUser = User.getInstance(UUID.randomUUID(), true);
        assertEquals("User does not return a user instence",
                true,
                User.class.isInstance(testUser));
    }

    @Test
    public void testGetUserId()
    {
        assertEquals("User does not return user ID",
                testId,
                testUser.getUserId());
    }

    @Test
    public void testGetContactInfoName()
    {
        String name = contactInformation.getName();
        ContactInformation returnedContactInfo = testUser.getInfo();
        assertEquals("Name returned is not correct",
                name,
                returnedContactInfo.getName());
    }

    @Test
    public void testGetContactInfoEmail()
    {
        String email = contactInformation.getEmail();
        ContactInformation returnedContactInfo = testUser.getInfo();
        assertEquals("Email returned is not correct",
                email,
                returnedContactInfo.getEmail());
    }

    @Test
    public void testGetContactInfoPhone()
    {
        String phone = contactInformation.getPhone();
        ContactInformation returnedContactInfo = testUser.getInfo();
        assertEquals("Phone returned is not correct",
                phone,
                returnedContactInfo.getPhone());
    }

    @Test
    public void testGetOwnedExperiments()
    {
        assertNull("Test user did not return the correct owned amount",
                testUser.getOwnedExperiments());
    }

    @Test
    public void testGetSubscriptions()
    {
        assertNull("Test user did not return the correct subs amount",
                testUser.getSubscriptions());
    }

    @Test
    public void testSetSubscriptions()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        testList.add(UUID.randomUUID());
        testUser.setSubscribedExperiments(testList);
        assertEquals("Set subs failed to be set",
                1,
                testUser.getSubscriptions().size());
    }

    @Test
    public void testSetOwnedExperiments()
    {
        testUser.setOwnedExperiments(new ArrayList<>());
        assertNotNull("Set subs failed to be set",
                testUser.getOwnedExperiments());
    }

    @Test
    public void testSetSubscriptionsNotNull()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        testList.add(UUID.randomUUID());
        testUser.setSubscribedExperiments(testList);
        testUser.setSubscribedExperiments(new ArrayList<>());
        assertEquals("Set subs failed to be set",
                0,
                testUser.getSubscriptions().size());
    }

    @Test
    public void testSetOwnedExperimentsNotNull()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        testList.add(UUID.randomUUID());
        testUser.setOwnedExperiments(new ArrayList<>());

        testUser.setOwnedExperiments(testList);
        assertEquals("Set subs failed to be set",1,
                testUser.getOwnedExperiments().size());
    }

    @Test
    public void testSetSubscriptionsNull()
    {
        testUser.setSubscribedExperiments(new ArrayList<>());
        testUser.setSubscribedExperiments(null);
        assertEquals("Set subs failed to be set",
                0,
                testUser.getSubscriptions().size());
    }

    @Test
    public void testSetOwnedExperimentsNull()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        testList.add(UUID.randomUUID());

        testUser.setOwnedExperiments(null);

        testUser.setOwnedExperiments(testList);
        assertNotNull("Set subs failed to be set",
                testUser.getOwnedExperiments().size());
    }

    @Test
    public void testSetTestMode()
    {
        try {
            Field currentMode = User.class.getDeclaredField("testMode");
            currentMode.setAccessible(true);
            testUser.setTestMode(false);
            assertEquals("Test mode not set",
                    false,
                    (boolean)currentMode.get(testUser));
        }catch (Exception e)
        {
            fail("Exceptions thrown");
        }
    }

    @Test
    public void testSubscribeExperimentNotInList()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        UUID testExperimentId = UUID.randomUUID();
        testList.add(UUID.randomUUID());
        testUser.setSubscribedExperiments(testList);
        int oldSize = testUser.getSubscriptions().size();
        testUser.subscribeExperiment(testExperimentId);
        assertEquals("Subscribe experiment not added",
                oldSize+1,
                testUser.getSubscriptions().size());
    }

    @Test
    public void testSubscribeExperimentInList()
    {
        ArrayList<UUID> testList = new ArrayList<>();
        UUID testExperimentId = UUID.randomUUID();
        testList.add(UUID.randomUUID());
        testUser.setSubscribedExperiments(testList);
        testUser.subscribeExperiment(testExperimentId);
        int oldSize = testUser.getSubscriptions().size();
        testUser.subscribeExperiment(testExperimentId);
        assertEquals("Subscribe experiment not added",
                oldSize-1,
                testUser.getSubscriptions().size());
    }
}
