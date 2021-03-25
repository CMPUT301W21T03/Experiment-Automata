package com.example.experiment_automata.ui.trials.MapDisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.trials.Trial;

import org.osmdroid.views.MapView;

import java.util.ArrayList;


public class MapPointViewFragment extends Fragment
{
    public static final String CURRENT_EXPERIMENT ="MAP_POINT_VIEW_EXPERIMENT";

    private MapView currentMapDisplay;
    private Experiment currentExperiment;

    public MapPointViewFragment()
    {
        //Needed for fragment to work.
        //Empty for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.map_view_fragment, container, false);
        currentMapDisplay = root.findViewById(R.id.map_point_view_fragment_map_display);
        currentExperiment = (Experiment) getArguments().getSerializable(CURRENT_EXPERIMENT);
        ArrayList<Trial> experimentTrials = currentExperiment.getRecordedTrials();

        if(!currentExperiment.isRequireLocation())
            currentMapDisplay.setVisibility(View.VISIBLE);
        else
        {
            /**
             * Authors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
             * Editors: OSMDROID Contributors on Github (https://github.com/osmdroid/osmdroid/graphs/contributors)
             * Full Link: https://github.com/osmdroid/osmdroid
             */

        }

        return root;
    }
}
