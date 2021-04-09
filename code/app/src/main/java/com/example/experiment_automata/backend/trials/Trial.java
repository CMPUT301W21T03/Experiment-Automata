package com.example.experiment_automata.backend.trials;

import android.location.Location;

import java.io.Serializable;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * Role/Pattern:
 *     The main trial type class of which the different trials are parts of.
 */
public abstract class Trial<T> implements Serializable {
    private final UUID userId;
    private final UUID trialId;
    private Location location;
    private long timestamp;
    private boolean ignore;
    protected T result;

    public Trial(UUID userId, long timestamp, T result) {
        this.userId = userId;
        this.ignore = false;
        this.result = result;
        this.trialId = UUID.randomUUID();
        if (timestamp == 0) {
            this.timestamp = new Date().getTime();
        }
    }

    public Trial(UUID userId, long timestamp,  Location location, T result) {
        this.userId = userId;
        this.location = location;
        this.ignore = false;
        this.result = result;
        this.trialId = UUID.randomUUID();
        if (timestamp == 0) {
            this.timestamp = new Date().getTime();
        }
    }


    /**
     * Returns the UUID of the trial
     * @return
     *  The UUID of a Trial
     */
    public UUID getTrialId() {
        return trialId;
    }

    /**
     * Returns the UUID of the Experimenter who made the trial
     * @return
     *  The UUID of an Experimenter
     */
    public UUID getUserId() { return userId; }

    /**
     * Returns the date of the trial.
     * @return
     *  date of the trial
     */
    public long getTimestamp() { return timestamp; }

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
    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    /**
     * get the currently set location for this particular trial.
     * @return
     *      the currently set location
     */
    public Location getLocation() {
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
