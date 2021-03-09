package com.example.experiment_automata;
/**
    Description: This class only goal is to deal with the view groups given
    Reason to Live: The reason for this class to give us control of what is
                    inflated in the recycler view. Giving us that control
                    so we can have a basic UI for each given experiment.


------Citations------
    Authors:

    These guides were originally created and a
    dapted by Nathan Esquenazi as a part of our CodePath training and bootcamps.
    We have also had contributions from many community members including:

    Nidhi Shah (CodePath Alumni and Staff)
    Roger Hu (CodePath Alumni and Instructor)
    Nick Aiwazian (CodePath Alumni and Instructor)
    Kevin Leong (CodePath Alumni and Instructor)
    Michael Alan Huff (CodePath Alumni and Mentor)
    Vibhor Bharadwaj (CodePath Alumni and Mentor)
    Ari Lacenski (CodePath Alumni)
    Chunyan Song (CodePath Alumni)
    Vishal Kapoor (CodePath Alumni)
    Trevor Elkins
    Adrian Romero
    Aaron Fleshner
    Steven Dobek

    Base URL: https://guides.codepath.com
    Full URL: https://guides.codepath.com/android/using-the-recyclerview

[
The MIT License (MIT)
Copyright (c) 2015 CodePath, Inc.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.
]

------Citations------
 */


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExperimentListAdapter extends RecyclerView.Adapter
{
    public static final String EXPERIMENT_EDIT_MESSAGE = "EDIT";

    private ExperimentManager experimentManager;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }


    public ExperimentListAdapter(ExperimentManager experimentManager)
    {
        this.experimentManager = experimentManager;
    }


    //(Modified to make work for my purpose)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View currentExperimentView = layoutInflater.inflate(R.layout.experiment_list_view, parent, false);
        return new ViewHolder(currentExperimentView);
    }

    // (Modified Significantly to make work for my purpose)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        Experiment experiment = experimentManager.get(position);
        TextView textView = holder.itemView.findViewById(R.id.experiment_list_view_exp_name);
        if(textView == null)
            Log.d("EDD_F", "CHECK");
        textView.setText(experiment.getDescription());

    }

    //(Modified to make work for my purpose)
    @Override
    public int getItemCount() {
        return experimentManager.getSize();
    }
}
