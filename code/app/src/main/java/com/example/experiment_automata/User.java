package com.example.experiment_automata;

/**
 * This is the user. Contains the information that represents a user.
 */
public class User {
    private int userId;
    private ContactInformation info;
//    private SearchController controller;
//    private Collection<Experiment> ownedExperiments;
//    private Collection<Experiment> participatingExperiments;

    /**
     * Creates the user. Assigns a user id automatically.
     * @param info
     * the ContactInformation object containing the information for the user
     */
    User(ContactInformation info) {
        userId = 1;
        this.info = info;
    }

    /**
     * Creates the stub user class
     */
    User() {
        userId = 1;
        this.info = new ContactInformation("Individual",
                "example@ualberta.ca", "780-555-1234");
    }
}
