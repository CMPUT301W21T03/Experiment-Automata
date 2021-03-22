package com.example.experiment_automata.backend.experiments;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.List;
/**
 * Role/Pattern:
 *     provides contract for graphs
 *
 *  Known Issue:
 *
 *      1. None
 */
public interface Graphable {
    /**
     * makes a histogram
     * @return a list of bar entry
     */
    public List<BarEntry> generateHistogram();

    /**
     * make a plot
     * @return list of entries
     */
    public List<Entry> generatePlot();
}
