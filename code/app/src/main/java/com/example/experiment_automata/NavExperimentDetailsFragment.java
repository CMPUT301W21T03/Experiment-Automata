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
 * change needs to happen here
 */
public class NavExperimentDetailsFragment extends Fragment {

    public static final String CURRENT_EXPERIMENT_ID = "FRAGMENT_CURRENT_FRAGMENT-ID";

    private TextView description;
    private TextView type;
    private ImageButton editExperimentButton;
    //TODO: Add remaining buttons when they become needed


    public NavExperimentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment nav_experiment_details.
     */
    // TODO: Rename and change types and number of parameters
    public static NavExperimentDetailsFragment newInstance() {
        NavExperimentDetailsFragment fragment = new NavExperimentDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_nav_experiment_details, container, false);

        String experimentID = null;
        if (savedInstanceState == null)
            Log.d("TEST_F", "failed");
        else
             experimentID = getArguments().getString(NavExperimentDetailsFragment.CURRENT_EXPERIMENT_ID);

        Log.d("TEST_T", "x" + experimentID);

        return root;
    }
}