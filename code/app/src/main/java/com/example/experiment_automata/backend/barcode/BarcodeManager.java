package com.example.experiment_automata.backend.barcode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.experiments.ExperimentType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    public void getAllFromFirestore(){
        //checkTestMode?
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference barcodeCollection = db.collection("barcodes");
        barcodeCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        BarcodeReference currentBarcodeRef;
                        UUID experimentId = UUID.fromString((String)document.get("experiment-id"));
                        String barcode = document.getId();
                        ExperimentType type = ExperimentType.valueOf((String)document.get("type"));
                        switch(type){
                            case Binomial:
                                boolean boolVal = (boolean)document.get("value");
                                currentBarcodeRef = new BinomialBarcodeReference(barcode,experimentId,ExperimentType.Binomial,boolVal);
                                break;
                            case Count:
                                currentBarcodeRef = new CountBarcodeReference(barcode,experimentId,ExperimentType.Count);
                                break;
                            case Measurement:
                                float measVal = (float)document.get("value");
                                currentBarcodeRef = new MeasurementBarcodeReference(barcode,experimentId,ExperimentType.Measurement,measVal);
                                break;
                            case NaturalCount:
                                int natVal = (int)document.get("value");
                                currentBarcodeRef = new NaturalBarcodeReference(barcode,experimentId,ExperimentType.NaturalCount,natVal);
                                break;
                            default:
                                //something went wrong when filling the database!
                                currentBarcodeRef = null;
                        }
                        barcodes.put(barcode,currentBarcodeRef);
                    }
                }
                else{
                    //not able to query firestore
                    Log.d("FIRESTORE","Unable to pull barcodes from firestore");
                }
            }
        });
    }


    public HashMap<String, BarcodeReference> getBarcodes() {
        return barcodes;
    }
}
