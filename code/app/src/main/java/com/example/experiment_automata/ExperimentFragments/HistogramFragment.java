package com.example.experiment_automata.ExperimentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment_automata.R;
import com.github.mikephil.charting.charts.BarChart;

/**
 * Role/Pattern:
 *
 *       This class inflates the histogram fragment.
 *
 * Known Issue:
 *
 *      1. None
 */

public class HistogramFragment extends Fragment {
    public HistogramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_histogram, container, false);
        BarChart chart = (BarChart) root.findViewById(R.id.histogram_chart);
        return root;
    }
}