package com.example.experiment_automata.ExperimentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment_automata.R;
import com.github.mikephil.charting.charts.LineChart;
/**
 * Role/Pattern:
 *
 *       This fragment class inflates the results fragment view.
 *
 * Known Issue:
 *
 *      1. None
 */
public class ResultsFragment extends Fragment {
    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_results, container, false);
        LineChart chart = (LineChart) root.findViewById(R.id.results_chart);
        return root;
    }
}