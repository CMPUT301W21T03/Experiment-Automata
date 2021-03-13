package com.example.experiment_automata.CustomExceptions;


/**
 * Role/Pattern:
 *
 *      Exception class needed to maintain an illegal experiment creation.
 *
 * Known Issue:
 *
 *      1. Might not be needed!
 */
public class IllegalExperimentException extends Exception
{
    public IllegalExperimentException(String error)
    {
        super(error);
    }
}
