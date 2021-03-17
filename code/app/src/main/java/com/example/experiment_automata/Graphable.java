package com.example.experiment_automata;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

public interface Graphable {
    public List<BarEntry> generateHistogram();

    public List<Entry> generatePlot();
}
