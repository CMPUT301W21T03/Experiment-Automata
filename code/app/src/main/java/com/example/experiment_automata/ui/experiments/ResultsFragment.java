package com.example.experiment_automata.ui.experiments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment_automata.R;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }
}