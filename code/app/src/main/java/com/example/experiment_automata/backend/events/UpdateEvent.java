package com.example.experiment_automata.backend.events;

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
