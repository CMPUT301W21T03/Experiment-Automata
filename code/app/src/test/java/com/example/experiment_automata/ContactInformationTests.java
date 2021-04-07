package com.example.experiment_automata;

import com.example.experiment_automata.backend.users.ContactInformation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testSetName()
    {
        String change = "bad";
        ContactInformation contactInformation = new ContactInformation(name,
                email,
                phone);
        contactInformation.setName(change);

        assertEquals("Change did not occur",
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

        assertEquals("Change did not occur",
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

        assertEquals("Change did not occur",
                change,
                contactInformation.getPhone());
    }
}
