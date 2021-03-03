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
     */
    Experimenter(ContactInformation info) {
        userId = 1;
        this.info = info;
    }
}
