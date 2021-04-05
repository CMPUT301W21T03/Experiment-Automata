package com.example.experiment_automata.backend.barcode;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and trial pair
 *
 * Known Issue:
 *
 *      1.
 */
public abstract class BarcodeReference<T> {
    private String barcodeVal;
    private UUID experimentId;
    private ExperimentType type;
    private T result;

    public BarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, T result)   {
        this.barcodeVal = barcodeVal;
        this.experimentId = experimentId;
        this.type = type;
        this.result = result;
    }
}
