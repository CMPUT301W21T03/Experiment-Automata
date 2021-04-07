package com.example.experiment_automata.backend.events;

public class UpdateEvent {
    private OnEventListener onEventListener;

    public void setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
    }

    public void callback() {
        if (onEventListener != null) {
            onEventListener.onEvent();
        }
    }
}