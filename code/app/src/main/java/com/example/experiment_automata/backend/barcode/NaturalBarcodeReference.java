package com.example.experiment_automata.backend.barcode;

import android.location.Location;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.UUID;
/**
 * Role/Pattern:
 *      Reference to a barcode and Natural Trial pair
 *
 * Known Issue:
 *
 *      1.
 */
public class NaturalBarcodeReference extends BarcodeReference<Integer>{
    public NaturalBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, int result){
        super(barcodeVal,experimentId,type,result,null);
    }

    public NaturalBarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, int result, Location location){
        super(barcodeVal,experimentId,type,result,location);
    }
}
