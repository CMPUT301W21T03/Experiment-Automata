package com.example.experiment_automata.backend.events;

/**
 * Role/Pattern:
 *      Class implementation of on event listener made for whenever
 *      we want to update something based on an event
 *
 */

public class UpdateEvent {
    private OnEventListener onEventListener;

    /**
     * Set a new listener for the event.
     * @param listener the new listener
     */
    public void setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
    }

    /**
     * The callback method that will get triggered.
     * Place this whenever you want to call the listener's onEvent method
     */
    public void callback() {
        if (onEventListener != null) {
            onEventListener.onEvent();
        }
    }
}
