package com.example.experiment_automata;

import com.example.experiment_automata.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.Collection;
import java.util.List;

public interface Graphable {
    public List<Entry> generateHistogram(Collection<Trial> trials);

    public List<Entry> generatePlot(Collection<Trial> trials);
}
