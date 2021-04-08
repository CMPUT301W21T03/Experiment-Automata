package com.example.experiment_automata.backend.barcode;

import android.location.Location;

import com.example.experiment_automata.backend.DataBase;
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
    private Location location;
    private T result;


    public BarcodeReference(String barcodeVal, UUID experimentId, ExperimentType type, T result, Location location)   {
        this.barcodeVal = barcodeVal;
        this.experimentId = experimentId;
        this.type = type;
        this.result = result;
        this.location = location;

        postBarcodeToFirestore();
    }
    /**
     * Post the current BarcodeReference to firestore
     */
    public void postBarcodeToFirestore(){
        BarcodeReference<T> barcode = this;
        DataBase database = DataBase.getInstance();
        FirebaseFirestore db = database.getFireStore();
        Map<String,Object> barcodeRefData = new HashMap<>();

        barcodeRefData.put("experiment-id", experimentId.toString());
        barcodeRefData.put("type", type.toString());
        barcodeRefData.put("result", result);
        if (location == null){
        //must write nulls to overwrite values
            barcodeRefData.put("longitude",null);
            barcodeRefData.put("latitude",null);
        } else {
            barcodeRefData.put("longitude",location.getLongitude());
            barcodeRefData.put("latitude",location.getLatitude());
        }


        db.collection("barcodes").document(barcodeVal).set(barcodeRefData);
    }

    public ExperimentType getType() {
        return type;
    }

    public UUID getExperimentId() {
        return experimentId;
    }

    public String getBarcodeVal() {
        return barcodeVal;
    }

    public T getResult() {
        return result;
    }

    public Location getLocation() {
        return location;
    }
}
