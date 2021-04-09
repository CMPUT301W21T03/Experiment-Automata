package com.example.experiment_automata.backend.barcode;

import android.location.Location;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and Binomial Trial pair
 *
 */
public class BinomialBarcodeReference extends BarcodeReference<Boolean> {
    public BinomialBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, boolean result){
        super(barcodeVal,experimentId,type,result,null);
    }

    public BinomialBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, boolean result, Location location){
        super(barcodeVal,experimentId,type,result,location);
    }
}
