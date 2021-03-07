package com.example.experiment_automata;

/**
 * This is a subscription class that manages the relationship between users and the experiments
 *  they're subscribed to
 */
public class Subscription{
    private User user;
    private Collection<Experiment> subscriptions;

    /**
     *
     * @param user User who is subscribed to several experiments
     * @param subscriptions If the user makes several experiment they're subscribed to that to begin with
     */
    public Subscription(User user, Collection<Experiment> subscriptions) {
        this.user = user;
        this.subscriptions = subscriptions;
        for(Experiment exp: subscriptions){
            // Add this user ID to all the experiments they're subscribed to
            exp.addUserId(this.user.userId);
        }
    }

    /**
     * Constructor when the user is subscribed to only one experiment at the start
     * @param user User who starts out subscribed to experiments
     * @param exp The single experiment they're subscribed to
     */
    public Subscription(User user, Experiment exp){
        this.user=user;
        this.subscriptions=new Collection<Experiment>();
        this.subscriptions.add(exp);
        // Add this userID to the list of userID's in the experiment
        exp.addUserId(this.user.userId);
    }

    /**
     * Construction in the case that the user didn't make any experiments
     * @param user User who didn't make any experiments
     */
    public Subscription(User user){
        this.user=user;
        this.subscriptions=new Collection<Experiment>();
    }

    /**
     * Adds an experiment to the collection of experiments
     * @param experiment Experiment to be added to the collection
     */
    public void addExperiment(Experiment experiment){
        // Add Experiment to List of User's Subscriptions
        this.subscriptions.add(experiment);
        // Work in the backwards direction to add the userID to the experiment
        // This way, each experiment will know which users can participate in it
        experiment.addUserId(this.user.userId);
    }

    /**
     * Removes an experiment from a user's collection of experiments
     * @param experiment
     */
    public void removeExperiment(Experiment experiment){
        if(this.subscriptions.contains(experiment)){
            // Removes experiment from list of user's experiments
            this.subscriptions.remove(experiment);
            // Removes userID from experiment's list
            experiment.removeUserId(this.user.userId);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Calls a fragment to show a user's subscriptions
     */
    public void showExperiment(){
        // TO DO
    }

    /**
     * Gets all the subscriptions
     * @return Collection of all the experiments a user is subscribed to
     */
    public Collection<Experiment> getSubscriptions() {
        return subscriptions;
    }
}