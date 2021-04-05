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
public class BinomialQRCode extends QRCode{
    static final String BINOMIAL_TRUE = "t";
    static final String BINOMIAL_FALSE = "f";
    private Boolean result;

    public BinomialQRCode(UUID experimentID, Boolean result) {
        super(experimentID, QRType.BinomialTrial);
        this.result = result;
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += BINOMIAL_ID;
        this.setRawContentString(packedString);
        //pack content
        this.result = result;
        if(result){
            packedString += BINOMIAL_TRUE;
        }
        else{
            packedString += BINOMIAL_FALSE;
        }
        this.setRawContentString(packedString);
        //create QR image
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        } catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }
    public BinomialQRCode(String rawContent) {
        super(rawContent);
        if (!rawContent.substring(41, 42).equals(QRCode.BINOMIAL_ID)) {
            //if incorrect type
        }

        String content = rawContent.substring(42);
        switch (content) {
            case BinomialQRCode.BINOMIAL_TRUE:
                result = true;
                break;
            case BinomialQRCode.BINOMIAL_FALSE:
                result = false;
                break;
        }
        try {
            this.setQrCodeImage(encodeStringToQR(rawContent));
        } catch (WriterException wException) {
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }
    public boolean getValue() {
        return result;
    }
}

