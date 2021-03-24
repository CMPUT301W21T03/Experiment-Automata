package com.example.experiment_automata.backend.users;

import android.content.SharedPreferences;

/**
 * Role/Pattern:
 *     The contact information of a user.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class ContactInformation {
    private String name;
    private String email;
    private String phone;
    private SharedPreferences preferences;
    boolean editable;

    /**
     * Creates a new contact information.
     * @param name
     * The name of the contact
     * @param email
     * The email address of the contact
     * @param phone
     * The phone number of the contact
     */
    public ContactInformation(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.editable = false;
    }

    /**
     * Creates new contact information for local user on device
     * @param preferences
     */
    public ContactInformation(SharedPreferences preferences) {
        this.name = preferences.getString("userName", "name");
        this.email = preferences.getString("userEmail", "email");
        this.phone = preferences.getString("userPhone", "phone");
        this.preferences = preferences;
        this.editable = true;
    }

    /**
     * Get the name of the contact
     * @return
     *  The contact's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the contact
     * @param name
     *  The contact's new name
     */
    public void setName(String name) {
        if (this.editable) {
            this.name = name;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userName", name);
            editor.apply();
        }
    }

    /**
     * Get the email address of the contact
     * @return
     *  The contact's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address of the contact
     * @param email
     *  The contact's new email address
     */
    public void setEmail(String email) {
        if (this.editable) {
            this.email = email;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userName", name);
            editor.apply();
        }
    }

    /**
     * Get the phone number of the contact
     * @return
     *  The contact's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the phone number of the contact
     * @param phone
     *  The contact's new phone number
     */
    public void setPhone(String phone) {
        if (this.editable) {
            this.phone = phone;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userName", name);
            editor.apply();
        }
    }

    /**
     * Set all contact information variables
     * @param name
     * @param email
     * @param phone
     */
    public void setAll(String name, String email, String phone) {
        this.setName(name);
        this.setEmail(email);
        this.setPhone(phone);
    }
}