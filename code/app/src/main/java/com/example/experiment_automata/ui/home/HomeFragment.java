package com.example.experiment_automata.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.ui.experiments.NavExperimentDetailsFragment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.Screen;
import com.example.experiment_automata.ui.experiments.ExperimentListAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Role/Pattern:
 *     Provides the main view control for when the user first enters the home screen.
 *
 *  Known Issue:
 *
 *      1. None
 */

public class HomeFragment extends Fragment {
    private ExperimentManager experimentManager;
    private ArrayList<Experiment<?>> experimentsArrayList;
    private ArrayAdapter<Experiment<?>> experimentArrayAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("HomeFragment", "Entering home fragment: " + getArguments().getString("mode"));
        NavigationActivity parentActivity = ((NavigationActivity) requireActivity());

        experimentManager = parentActivity.experimentManager;
        parentActivity.findViewById(R.id.fab_button).setVisibility(View.VISIBLE);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        parentActivity.setCurrentScreen(Screen.ExperimentList);
        parentActivity.setCurrentFragment(this);
        ListView experimentList = (ListView) root.findViewById(R.id.experiment_list);
        experimentsArrayList = new ArrayList<>();
        populateList();
        experimentArrayAdapter = new ExperimentListAdapter(getActivity(),
                experimentsArrayList, getArguments().getString("mode"), parentActivity.userManager);
        experimentList.setAdapter(experimentArrayAdapter);

        Bundle bundle = new Bundle();
        NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);

        ((ListView) root.findViewById(R.id.experiment_list)).setOnItemClickListener((parent, view, position, id) -> {
            String experimentID  = ((TextView)view.findViewById(R.id.experiment__id)).getText().toString();
            // String values
            bundle.putString(NavExperimentDetailsFragment.CURRENT_EXPERIMENT_ID, experimentID);

            // set current experiment
            Experiment<?> experiment = experimentManager.query(UUID.fromString(experimentID));
            experimentManager.setCurrentExperiment(experiment);

            //nav_experiment_details
            navController.navigate(R.id.nav_experiment_details, bundle);
        });

        return root;
    }

    /**
     * Populate the ArrayList with experiments
     */
    public void populateList() {
        NavigationActivity parentActivity = ((NavigationActivity) requireActivity());
        experimentsArrayList.clear();
        Log.d("MODE", getArguments().getString("mode"));
        switch (getArguments().getString("mode")) {
            case "owned":
                experimentManager.filterFromFirestore("owner",
                        parentActivity.loggedUser.getUserId().toString());
                experimentsArrayList.addAll(experimentManager
                        .queryExperiments(parentActivity.loggedUser.getOwnedExperiments()));
                break;
            case "published":
                experimentManager.filterFromFirestore("published", true);
                parentActivity.userManager.getAllUsersFromFireStore();
                experimentsArrayList.addAll(experimentManager
                        .getPublishedExperiments());
                break;
            case "subscribed":
                List<String> subscriptionList = new ArrayList<>();
                for (UUID experimentId : parentActivity.loggedUser.getSubscriptions()) {
                    subscriptionList.add(experimentId.toString());
                }
                experimentManager.filterFromFirestore(subscriptionList);
                experimentsArrayList.addAll(experimentManager
                        .queryExperiments(parentActivity.loggedUser.getSubscriptions()));
                break;
            case "search":
                // You can only search published experiments
                experimentManager.filterFromFirestore("published", true);
                String query = getArguments().getString("query");
                experimentsArrayList.addAll(experimentManager
                        .queryPublishedExperiments(query));
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
