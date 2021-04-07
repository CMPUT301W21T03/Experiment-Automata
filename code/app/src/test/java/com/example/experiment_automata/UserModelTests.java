package com.example.experiment_automata;

import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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


}
