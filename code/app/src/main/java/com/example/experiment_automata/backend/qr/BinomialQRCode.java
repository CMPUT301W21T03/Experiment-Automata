package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;
/**
 * Role/Pattern:
 *     Class representing a QR Code containing a reference to a Binomial Trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class BinomialQRCode extends QRCode<Boolean>{
    static final String BINOMIAL_TRUE = "t";
    static final String BINOMIAL_FALSE = "f";

    public BinomialQRCode(UUID experimentID, Boolean result) {
        super(experimentID, QRType.BinomialTrial);
        setValue(result);
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += BINOMIAL_ID;
        //pack content
        setValue(result);
        if (result) {
            packedString += BINOMIAL_TRUE;
        } else {
            packedString += BINOMIAL_FALSE;
        }
        //create QR image
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        } catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }
}
