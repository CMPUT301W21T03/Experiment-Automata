package com.example.experiment_automata.trials;

import android.location.Location;

import java.util.Date;
import java.util.UUID;

public abstract class Trial {
    private UUID collector;
    private Location location;
    private Date date;
//    TODO: uncomment qr line once qr support has been added
//    private QRcode qr;

    public Trial(UUID collector) {
        this.collector = collector;
        this.date = new Date();
    }

    public Trial(UUID collector, Location location) {
        this.collector = collector;
        this.location = location;
        this.date = new Date();
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
}
