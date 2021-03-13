package com.example.experiment_automata.UserInformation;

/**
 * The contact information of a user.
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
    public ContactInformation(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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
        this.name = name;
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
        this.email = email;
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
        this.phone = phone;
    }
}
