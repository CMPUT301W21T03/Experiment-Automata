package com.example.experiment_automata;


/**
 * Exception class needed to maintain an illegal experiment creation.
 */
public class IllegalExperimentException extends Exception
{
    public IllegalExperimentException(String error)
    {
        super(error);
    }
}
