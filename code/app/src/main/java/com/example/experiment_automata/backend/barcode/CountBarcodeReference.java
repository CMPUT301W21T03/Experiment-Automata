package com.example.experiment_automata.backend.barcode;

import android.location.Location;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;

/**
 * Role/Pattern:
 *      Reference to a barcode and Count Trial pair
 *
 */
public class CountBarcodeReference extends BarcodeReference<Object> {
    public CountBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type){
        super(barcodeVal,experimentId,type,null,null);
    }

    public CountBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, Location location){
        super(barcodeVal,experimentId,type,null,location);
    }
}
