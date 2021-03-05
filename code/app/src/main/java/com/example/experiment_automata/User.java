package com.example.experiment_automata;

import java.util.UUID;

/**
 * This is the user. Contains the information that represents a user.
 */
public class User {
    private static String DEFAULT_UUID_STRING= "00000000-0000-0000-0000-000000000000";//move this to a constants class later
    private UUID userId;//changed from int to UUID
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
        userId = UUID.randomUUID();//generates a random UUID
        this.info = info;
    }

    /**
     * Creates the stub user class
     */
    User() {
        userId = UUID.fromString(DEFAULT_UUID_STRING);//hard code UUID for stub to constant DEFAULT_UUID_STRING
        this.info = new ContactInformation("Individual",
                "example@ualberta.ca", "780-555-1234");
    }
}
