package com.example.experiment_automata;


/**
 * Exception class needed to maintain an illegal experiment creation.
 */
public class IlleagalExperimentException extends Exception
{
    public IlleagalExperimentException(String error)
    {
        super(error);
    }
}
