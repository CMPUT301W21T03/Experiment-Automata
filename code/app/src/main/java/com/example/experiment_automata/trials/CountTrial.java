package com.example.experiment_automata.trials;

import android.location.Location;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *     Holds the information needed to maintain and track a count trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class CountTrial extends Trial {

    public CountTrial(UUID collector) {
        super(collector);
    }

    public CountTrial(UUID collector, Location location) {
        super(collector, location);
    }
}
