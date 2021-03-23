package com.example.experiment_automata.backend.qr;

import android.graphics.Bitmap;

import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;

import java.util.UUID;
/**
 * Role/Pattern:
 *     Class representing a QR code through it's image and content fields.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class QRCode {
    //Header for custom QR codes
    static String AUTOMATA_QR_HEADER = "ATMA";
    static int DEAFULT_QR_HEIGHT = 600;
    static int DEFAULT_QR_WIDTH = 600;
    static String BINOMIAL_ID = "b";
    static String COUNT_ID = "c";
    static String MEASUREMENT_ID = "m";
    static String NATURALC_ID = "n";
    private String rawContentString;
    private UUID experimentID;
    private Bitmap qrCodeImage;

    public QRCode(UUID experimentID, BinomialTrial trial){//build a Binomial QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += BINOMIAL_ID;
        if (trial.getResult()){
            packedString += "1";
        }
        else {
            packedString += "0";
        }
        rawContentString = packedString;
    }

    public QRCode(UUID experimentID, CountTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += COUNT_ID;
        //CountTrial result field un-implemented
        rawContentString = packedString;
    }
    public QRCode(UUID experimentID, MeasurementTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += MEASUREMENT_ID;
        packedString += String.valueOf(trial.getResult());
        rawContentString = packedString;
    }
    public QRCode(UUID experimentID, NaturalCountTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += NATURALC_ID;
        packedString += String.valueOf(trial.getResult());
        rawContentString = packedString;
    }



}
