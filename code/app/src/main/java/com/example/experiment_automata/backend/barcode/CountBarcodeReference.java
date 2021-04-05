package com.example.experiment_automata.backend.barcode;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and Count Trial pair
 *
 * Known Issue:
 *
 *      1.
 */
public class CountBarcodeReference extends BarcodeReference<Object> {
    public CountBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type){
        super(barcodeVal,experimentId,type,null);
    }
}
