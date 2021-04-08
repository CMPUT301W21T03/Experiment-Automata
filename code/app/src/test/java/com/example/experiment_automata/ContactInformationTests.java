package com.example.experiment_automata;

import android.content.SharedPreferences;

import com.example.experiment_automata.backend.users.ContactInformation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContactInformationTests {

    private String name = "name";
    private String email = "email";
    private String phone = "phone";
    /**
     * Testing contact information
     *
     * Note: Testing the shared preferences constructor
     *       cannot be done in this class.
     */

    @Test
    public void testGetName()
    {
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);

        assertEquals("Contact information did not get the right name",
                name,
                contactInformation.getName());
    }

    @Test
    public void testGetEmail()
    {
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);

        assertEquals("Contact information did not get the right email",
                email,
                contactInformation.getEmail());
    }

    @Test
    public void testGetPhone()
    {
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);

        assertEquals("Contact information did not get the right phone",
                phone,
                contactInformation.getPhone());
    }


    /**
     * These tests are such that the edit flag is false
     */
    @Test
    public void testSetName()
    {
        String change = "bad";
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);
        contactInformation.setName(change);

        assertNotEquals("Change did not occur",
                change,
                contactInformation.getName());
    }

    @Test
    public void testSetEmail()
    {
        String change = "bad";
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);
        contactInformation.setEmail(change);

        assertNotEquals("Change did not occur",
                change,
                contactInformation.getEmail());
    }

    @Test
    public void testSetPhone()
    {
        String change = "bad";
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);
        contactInformation.setPhone(change);

        assertNotEquals("Change did not occur",
                change,
                contactInformation.getPhone());
    }

    @Test
    public void testSetAllNotEditable()
    {
        String change = "bad";
        ContactInformation contactInformation
                = new ContactInformation(name,
                                        email,
                                        phone);

        contactInformation.setAll(change, change, change);

        assertNotEquals("Name not set", change,
                contactInformation.getName());
        assertNotEquals("Email not set", change,
                contactInformation.getEmail());
        assertNotEquals("Phone not set", change,
                contactInformation.getPhone());
    }

    /**
     * Testing the not edit field is not feasible  
     */
}
