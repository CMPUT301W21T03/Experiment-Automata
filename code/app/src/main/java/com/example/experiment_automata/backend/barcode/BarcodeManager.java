package com.example.experiment_automata.backend.barcode;

import com.example.experiment_automata.backend.experiments.ExperimentType;

import java.util.HashMap;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Contains and manages Barcode and trial references
 *
 * Known Issue:
 *
 *      1.
 */
public class BarcodeManager {
    private HashMap<String,BarcodeReference> barcodes;

    public BarcodeManager(){
        barcodes = new HashMap<String,BarcodeReference>();
    }
    /**
     * Add a barcode reference to the barcode manager for a NaturalCount Trial
     * @param barcodeVal
     *   String that is contained in the barcode
     * @param experimentId
     *   UUID of the experiment that the represented trial is a part of
     * @param result
     *   the given trial result
     */
    public void addBarcode(String barcodeVal, UUID experimentId, int result){
        NaturalBarcodeReference barcodeRef = new NaturalBarcodeReference(barcodeVal,experimentId,ExperimentType.NaturalCount,result);
        barcodes.put(barcodeVal,barcodeRef);
    }
    /**
     * Add a barcode reference to the barcode manager for a Measurement Trial
     * @param barcodeVal
     *   String that is contained in the barcode
     * @param experimentId
     *   UUID of the experiment that the represented trial is a part of
     * @param result
     *   the given trial result
     */
    public void addBarcode(String barcodeVal, UUID experimentId, float result){
        MeasurementBarcodeReference barcodeRef = new MeasurementBarcodeReference(barcodeVal,experimentId,ExperimentType.Measurement,result);
        barcodes.put(barcodeVal,barcodeRef);
    }
    /**
     * Add a barcode reference to the barcode manager for a Binomial Trial
     * @param barcodeVal
     *   String that is contained in the barcode
     * @param experimentId
     *   UUID of the experiment that the represented trial is a part of
     * @param result
     *   the given trial result
     */
    public void addBarcode(String barcodeVal, UUID experimentId, boolean result){
       BinomialBarcodeReference barcodeRef = new BinomialBarcodeReference(barcodeVal,experimentId,ExperimentType.Binomial,result);
       barcodes.put(barcodeVal,barcodeRef);
    }
    /**
     * Add a barcode reference to the barcode manager for a Count Trial
     * @param barcodeVal
     *   String that is contained in the barcode
     * @param experimentId
     *   UUID of the experiment that the represented trial is a part of
     */
    public void addBarcode(String barcodeVal, UUID experimentId){
        CountBarcodeReference barcodeRef = new  CountBarcodeReference(barcodeVal,experimentId,ExperimentType.Count);
        barcodes.put(barcodeVal,barcodeRef);
    }

    //add firestore here

    public HashMap<String, BarcodeReference> getBarcodes() {
        return barcodes;
    }
}
