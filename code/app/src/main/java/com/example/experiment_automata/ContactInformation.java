package com.example.experiment_automata;

/**
 * The contact information of an experimenter.
 */
public class ContactInformation {
    private String name;
    private String email;
    private String phone;

    /**
     * Creates a new contact information.
     * @param name
     * The name of the contact
     * @param email
     * The email address of the contact
     * @param phone
     * The phone number of the contact
     */
    ContactInformation(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
