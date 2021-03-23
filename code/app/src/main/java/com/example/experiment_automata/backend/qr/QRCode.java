package com.example.experiment_automata.backend.qr;

import android.graphics.Bitmap;

import java.util.UUID;

public class QRCode {
    //Header for custom QR codes
    static String AUTOMATA_QR_HEADER = "ATMA";
    static int DEAFULT_QR_HEIGHT = 600;
    static int DEFAULT_QR_WIDTH = 600;
    private String rawContentString;
    private UUID experimentID;
    private Bitmap qrCodeImage;

    public QRCode(UUID experimentID){//build a QR code
        this.experimentID = experimentID;
    }

    public String packBinomialQRString(UUID experimentID){
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();


    }
}
