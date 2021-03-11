package com.example.experiment_automata.ui.home;

import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.Experiment;
import com.example.experiment_automata.NavExperimentDetailsFragment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.Screen;
import com.example.experiment_automata.ExperimentListAdapter;


import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<Experiment> experimentsArrayList;
    private ArrayAdapter<Experiment> experimentArrayAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        parentActivity.setCurrentScreen(Screen.ExperimentList);
        parentActivity.setCurrentFragment(this);
        ListView experimentList = (ListView) root.findViewById(R.id.experiment_list);
        experimentsArrayList = new ArrayList<>();
        populateList();
        experimentArrayAdapter = new ExperimentListAdapter(getActivity(), experimentsArrayList);
        experimentList.setAdapter(experimentArrayAdapter);


        ((ListView) root.findViewById(R.id.experiment_list)).setOnItemClickListener((parent, view, position, id) -> {

            String experimentID  = ((TextView)view.findViewById(R.id.experiment__id)).getText().toString();
            Bundle bundle = new Bundle();
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            bundle.putString(NavExperimentDetailsFragment.CURRENT_EXPERIMENT_ID, experimentID);

            bundle.putString(NavExperimentDetailsFragment.CURRENT_EXPERIMENT_ID, experimentID);

            //nav_experiment_details
            navController.navigate(R.id.nav_experiment_details);

        });

        return root;
    }

    /**
     * Populate the ArrayList with experiments
     */
    public void populateList() {
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        experimentsArrayList.clear();
        Log.d("MODE", getArguments().getString("mode"));
        switch (getArguments().getString("mode")) {
            case "owned":
                experimentsArrayList.addAll(parentActivity.experimentManager
                        .queryExperiments(parentActivity.loggedUser.getOwnedExperiments()));
                break;
            case "published":
                experimentsArrayList.addAll(parentActivity.experimentManager
                        .queryPublishedExperiments());
                break;
            case "subscribed":
                experimentsArrayList.addAll(parentActivity.experimentManager
                        .queryExperiments(parentActivity.loggedUser.getSubscriptions()));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Update the list
     */
    public void updateScreen() {
        populateList();
        experimentArrayAdapter.notifyDataSetChanged();
    }
}