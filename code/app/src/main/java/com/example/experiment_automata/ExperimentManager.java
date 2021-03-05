package com.example.experiment_automata;

import java.util.ArrayList;

public class ExperimentManager
{
    private static ArrayList<Experiment> experimentsList;
    private static ExperimentManager experimentManager;

    private ExperimentManager()
    {
        experimentsList = new ArrayList<>();
    }

    public static ExperimentManager getExpermentManager()
    {
        if (experimentManager == null)
            experimentManager = new ExperimentManager();

        return experimentManager;
    }

    public void add(Experiment experiment)
    {
        if(experimentManager == null)
            throw new IllegalAccessError();
        else
            experimentsList.add(experiment);
    }

    public void delete(Experiment experiment)
    {
        for(Experiment e : experimentsList)
        {
            if(e.compare(experiment))
                experimentsList.remove(e);
            else
                continue;
        }
        throw new IllegalArgumentException();
    }

    public int getSize()
    {
        return experimentsList.size();
    }
}
