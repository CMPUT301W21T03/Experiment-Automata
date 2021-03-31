package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


/**
 * Role/Pattern:
 *     The main trial type class of which the different trials are parts of.
 *
 *  Known Issue:
 *
 *      1. None
 */
public abstract class Trial<T> implements Serializable {
    private UUID collector;
    private Location location;
    private Date date;
    private boolean ignore;
    protected T result;
//    TODO: uncomment qr line once qr support has been added
//    private QRcode qr;

    public Trial(UUID collector, T result) {
        this.collector = collector;
        this.date = new Date();
        this.ignore = false;
        this.result = result;
    }

    public Trial(UUID collector, Location location, T result) {
        this.collector = collector;
        this.location = location;
        this.date = new Date();
        this.ignore = false;
        this.result = result;
    }

    /**
     * Returns the UUID of the Experimenter who made the trial
     * @return
     *  The UUID of an Experimenter
     */
    public UUID getCollector() { return collector; }

    /**
     * Returns the date of the trial.
     * @return
     *  date of the trial
     */
    public Date getDate() { return date; }

    /**
     * Set whether to ignore the trial result in statistic calculations.
     * @param ignore
     *  boolean to set whether to ignore the trial result
     */
    public void setIgnore(boolean ignore) { this.ignore = ignore; }

    /**
     * Return whether to ignore the trial result in statistic calculations.
     * @return
     *  whether to ignore
     */
    public boolean isIgnored() { return ignore; }

    /**
     * Sets the current location of a trial.
     *
     * @param newLocation the new location we want to change to.
     */
    public void setLocation(Location newLocation)
    {
        this.location = newLocation;
    }

    /**
     * get the currently set location for this particular trial.
     * @return
     *      the currently set location
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * get the the type of the current trial
     * @return
     *  the type of the current trial
     */
    public abstract String getType();

    /**
     *  Gets the result of the single trial recorded.
     * @return
     *  The result of the trial
     */
    public T getResult() {
        return result;
    }

    /**
     * Set the result of the trial.
     * @param result
     *  the result we want to set
     */
    public void setResult(T result) {
        this.result = result;
    }
}
