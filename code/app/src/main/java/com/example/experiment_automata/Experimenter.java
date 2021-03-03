package com.example.experiment_automata;

/**
 * This is the experimenter. Contains the information that represents a user.
 */
public class Experimenter {
    private int userId;
    private ContactInformation info;
//    private SearchController controller;
//    private Collection<Experiment> ownedExperiments;
//    private Collection<Experiment> participatingExperiments;

    /**
     * Creates the experimenter. Assigns a user id automatically.
     * @param info
     * the ContactInformation object containing the information for the experimenter
     */
    Experimenter(ContactInformation info) {
        userId = 1;
        this.info = info;
    }

    /**
     * Creates the stub class experimenter
     */
    Experimenter() {
        userId = 1;
        this.info = new ContactInformation("Individual",
                "example@ualberta.ca", "780-555-1234");
    }
}
