package com.example.experiment_automata.backend.barcode;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and Binomial Trial pair
 *
 * Known Issue:
 *
 *      1.
 */
public class BinomialBarcodeReference extends BarcodeReference<Boolean> {
    public BinomialBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, boolean result){
        super(barcodeVal,experimentId,type,result);
    }
}
