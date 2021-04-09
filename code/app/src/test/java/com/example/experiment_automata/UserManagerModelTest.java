package com.example.experiment_automata;

import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.backend.users.UserManager;

import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserManagerModelTest {
    /**
     * Role: Testing the UserManager class
     */
    UserManager userManager;

    @After
    public void destroyManager() throws NoSuchFieldException, IllegalAccessException {
        Field manager = UserManager.class.getDeclaredField("userManager");
        manager.setAccessible(true);
        manager.set(userManager, null);
    }

    /**
     * Note the testing here is only
     * for when the class is in test-mode
     * if it is in regular mode then the system
     * is tested when doing the intent tests.
     */
    @Test
    public void testGetInstance() {
         userManager = UserManager.getInstance(true);
        assertNotNull("UserManager did not return an instance of itself", userManager);

    }

    @Test
    public void testSizeCorrect() {
        userManager = UserManager.getInstance(true);
        assertEquals("Size is wrong",
                0,
                userManager.getSize());
    }

    @Test
    public void testAddNoInside() {
        userManager = UserManager.getInstance(true);
        User testUser = new User(true, null, UUID.randomUUID());
        userManager.add(testUser);
        assertEquals("Failed to add value to user manager",
                1,
                userManager.getSize());
    }

    @Test
    public void testAddAlreadyInside() {
        userManager = UserManager.getInstance(true);
        UUID already = UUID.randomUUID();
        User testUser = new User(true, null, already);
        User testUser2 = new User(true, null, already);
        userManager.add(testUser);
        userManager.add(testUser2);
        assertEquals("Added the same user into the system",
                1,
                userManager.getSize());
    }

    @Test
    public void testGetSpecificUser() {
        userManager = UserManager.getInstance(true);
        UUID already = UUID.randomUUID();
        UUID already2 = UUID.randomUUID();
        User testUser = new User(true, null, already);
        User testUser2 = new User(true, null, already2);
        userManager.add(testUser);
        userManager.add(testUser2);

        assertEquals("Didn't get the correct user from system",
                testUser2.getUserId(),
                userManager.getSpecificUser(already2).getUserId());
    }
}
