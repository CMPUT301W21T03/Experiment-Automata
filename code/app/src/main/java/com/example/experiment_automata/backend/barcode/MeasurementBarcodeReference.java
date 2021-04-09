package com.example.experiment_automata.backend.barcode;

import android.location.Location;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and Measurement Trial pair
 *
 */
public class MeasurementBarcodeReference extends BarcodeReference<Float> {
    public MeasurementBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, float result){
        super(barcodeVal,experimentId,type,result,null);
    }

    public MeasurementBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, float result, Location location){
        super(barcodeVal,experimentId,type,result,location);
    }
}
