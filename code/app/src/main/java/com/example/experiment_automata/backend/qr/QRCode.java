package com.example.experiment_automata.backend.qr;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += BINOMIAL_ID;
        rawContentString = packedString;
        //pack content
        if (trial.getResult()){
            packedString += "1";
        }
        else {
            packedString += "0";
        }
        rawContentString = packedString;
        //create QR image
        try {
            qrCodeImage =  encodeStringToQR(packedString);
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }

    public QRCode(UUID experimentID, CountTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += COUNT_ID;
        //CountTrial result field un-implemented
        //create QR image
        try {
            qrCodeImage =  encodeStringToQR(packedString);
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
        rawContentString = packedString;

    }
    public QRCode(UUID experimentID, MeasurementTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += MEASUREMENT_ID;
        packedString += String.valueOf(trial.getResult());
        //create QR image
        try {
            qrCodeImage =  encodeStringToQR(packedString);
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
        rawContentString = packedString;
    }
    public QRCode(UUID experimentID, NaturalCountTrial trial){//build a Count QR code
        this.experimentID = experimentID;
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += NATURALC_ID;
        packedString += String.valueOf(trial.getResult());
        //create QR image
        try {
            qrCodeImage =  encodeStringToQR(packedString);
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
        rawContentString = packedString;
    }

    /**
     * Encodes a string as a QRCode
     * @param encodedContent
     * string to be encoded
     * @return
     * returns a Bitmap containing the QRCode
     */
    public Bitmap encodeStringToQR(String encodedContent) throws WriterException {
        BitMatrix qrCodeBitMatrix;
        Bitmap qrCodeBitmap;
        try{
            qrCodeBitMatrix = new QRCodeWriter().encode(encodedContent, BarcodeFormat.QR_CODE,DEFAULT_QR_WIDTH,DEAFULT_QR_HEIGHT);
        }
        catch (IllegalArgumentException illegalArgException) {
            // Unsupported format, required for encode
            return null;
        }
        //convert BitMatrix to Bitmap
        int width = qrCodeBitMatrix.getWidth();
        int height = qrCodeBitMatrix.getHeight();
        qrCodeBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                qrCodeBitmap.setPixel(x, y, qrCodeBitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return qrCodeBitmap;

    }

}
