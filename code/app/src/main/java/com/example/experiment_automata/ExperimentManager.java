package com.example.experiment_automata;

import java.util.ArrayList;

/**
 * Class made to maintain experiments that the users will make
 *
 */
public class ExperimentManager
{
    private static ArrayList<Experiment> experimentsList;
    private static ExperimentManager experimentManager;

    /**
     * Initializes the experiment manager.
     */
    private ExperimentManager()
    {
        experimentsList = new ArrayList<>();
    }


    /**
     * Gives the maintained experimentManager back to the calling class
     * @return
     *      The maintained experiment class
     */
    public static ExperimentManager getExperimentManager()
    {
        if (experimentManager == null)
            experimentManager = new ExperimentManager();

        return experimentManager;
    }

    /**
     * Adds the given experiment that the user class/caller gives to this class
     *
     * @param experiment
     */
    public void add(Experiment experiment)
    {
        if(experimentManager == null)
            throw new IllegalAccessError();
        else
            experimentsList.add(experiment);
    }

    /**
     * Deletes a given experiment from the currently maintained list
     * @param experiment
     */
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

    /**
     * gets the size of the currently maintained list
     * @return
     *      the size of the current list
     */
    public int getSize()
    {
        return experimentsList.size();
    }

    /**
     * get the experiment at that index
     * @param index
     * @return
     *      the experiment at the given index
     */
    public Experiment get(int index)
    {
        return experimentsList.get(index);
    }

}
