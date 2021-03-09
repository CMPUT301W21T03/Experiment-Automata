package com.example.experiment_automata.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.experiment_automata.Experiment;
import com.example.experiment_automata.R;
import com.example.experiment_automata.SubscriptionAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView subscriptionList = (ListView) root.findViewById(R.id.subscription_list);
        ArrayList<Experiment> subscriptionArrayList = new ArrayList<>();
        ArrayAdapter<Experiment> subscriptionArrayAdapter = new SubscriptionAdapter(getActivity(), subscriptionArrayList);
        subscriptionList.setAdapter(subscriptionArrayAdapter);
        return root;
    }
}