package com.example.experiment_automata;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavExperimentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * */
public class NavExperimentDetailsFragment extends Fragment {

    private String ERROR_LOG_VALUE = "ERROR_LOG-EXPERIMENT-VIEW";
    public static final String CURRENT_EXPERIMENT_ID = "FRAGMENT_CURRENT_FRAGMENT-ID";


    // TODO: Rename and change types of parameters
    private String experimentStringId;
    private String description;
    private TextView descriptionView;
    private TextView typeView;

    public NavExperimentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param experimentStringId ID of the clicked experiment
     * @return A new instance of fragment FullExperimentView.
     */

    public static NavExperimentDetailsFragment newInstance(String experimentStringId) {
        NavExperimentDetailsFragment fragment = new NavExperimentDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_EXPERIMENT_ID, experimentStringId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experimentStringId = getArguments().getString(CURRENT_EXPERIMENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_nav_experiment_details, container, false);
        Experiment current;
        getArguments();
        descriptionView = root.findViewById(R.id.nav_experiment_details_description);
        typeView = root.findViewById(R.id.nav_experiment_details_experiment_type);
        if (experimentStringId == null)
            Log.d(ERROR_LOG_VALUE, "Should never happen");
        else
        {
            current = (((NavigationActivity)getActivity()).getExperimentManager())
                    .getAtUUIDDescription(UUID.fromString(experimentStringId));

            if(current != null)
            {
                descriptionView.setText(current.getDescription());
                typeView.setText("" + current.getType());
            }
            Log.d("TEST_A", "" + experimentStringId);
        }


        return root;
    }
}