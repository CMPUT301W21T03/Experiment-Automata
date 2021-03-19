package com.example.experiment_automata.CustomExceptions;


/**
 * Role/Pattern:
 *
 *      Exception class needed to maintain an illegal experiment creation.
 *
 * Known Issue:
 *
 *      1. No longer in use, to be deleted!
 */
public class IllegalExperimentException extends Exception
{
    public IllegalExperimentException(String error)
    {
        super(error);
    }
}
