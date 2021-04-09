package com.example.experiment_automata.backend.events;

/**
 * Role/Pattern:
 *     Interface for creating event listeners
 *
 */

public interface OnEventListener {
    /**
     * Method to run when the listener gets triggered
     */
    public void onEvent();
}
