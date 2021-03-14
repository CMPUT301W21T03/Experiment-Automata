package com.example.experiment_automata.QRCode;


import android.graphics.Bitmap;

import com.google.firestore.v1.Write;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Controls all custom interactions with QR codes and Bar codes
 * */
public class QRCodeManager {
    //Header for custom QR codes
    private final String AUTOMATA_QR_HEADER = "ATMA";
    private final int DEAFULT_QR_HEIGHT = 300;
    private final int DEFAULT_QR_WIDTH = 300;

    /**
     * Encodes a string as a QRCode
     * @param encodedContent
     * string to be encoded
     * @return
     * returns a bitmap containing the QRCode
     */
    public Bitmap encodeStringToQR(String encodedContent) throws WriterException {
        BitMatrix qrCode;
        try{
            qrCode = new QRCodeWriter().encode(encodedContent, BarcodeFormat.QR_CODE,DEFAULT_QR_WIDTH,DEAFULT_QR_HEIGHT);
        }
        catch (IllegalArgumentException illegalArgException) {
            return null;
        }
        //convert BitMatrix to Bitmap

    }

    /**
     * Unpacks a string from custom ExperimentAutomata QRCode content to string
     * @param code
     * packed ExperimentAutomata QRCode string
     * @return
     * returns the content of the packed QR code String if it's valid, returns an empty string if it's not a valid Experiment Automata QR code
     */
    //keep ability for extra content code at substring[4] for future use
    private String unpackQRString(String code){
        //check header
        if(!code.substring(0,4).equals(AUTOMATA_QR_HEADER)){
            //not an ExperimentAutomata QR code
            return "";//change to raise exception?
        }
        else{
            return code.substring(4);
        }
    }
    /**
     * Packs a string into custom ExperimentAutomata QR code
     * @param content
     * string to pack
     * @return
     * returns the custom ExperimentAutomata QR code
     */
    //keep as method in case we want to have special fields in the future
    private String packQRString(String content){
        return AUTOMATA_QR_HEADER + content;
    }

}
