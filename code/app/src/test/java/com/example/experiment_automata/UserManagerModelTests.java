package com.example.experiment_automata;

import com.example.experiment_automata.backend.users.UserManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UserManagerModelTests
{
    private UserManager userManager;


    /**
     * Note the testing here is only
     * for when the class is in test-mode
     * if it is in regular mode then the system
     * is tested when doing the intent tests.
     */
    @Test
    public void testGetInstance()
    {
        UserManager manager = UserManager.getInstance(true);
        assertTrue("UserManager did not return an instence of itself",
                UserManager.class.isInstance(manager));

    }
}
