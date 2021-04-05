package com.example.experiment_automata.backend.barcode;

import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
        postBarcodeToFirestore();
    }
    /**
     * Post the current BarcodeReference to firestore
     */
    public void postBarcodeToFirestore(){
        BarcodeReference<T> barcode = this;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> barcodeRefData = new HashMap<>();

        barcodeRefData.put("experiment-id",experimentId.toString());
        barcodeRefData.put("type",type.toString());//store type to make life easier
        barcodeRefData.put("result",result);

        db.collection("barcodes").document(barcodeVal).set(barcodeRefData);
    }

    public String getBarcodeVal() {
        return barcodeVal;
    }
}
