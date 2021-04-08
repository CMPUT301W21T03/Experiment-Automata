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
        this.setRawContentString(packedString);
        //pack content
        setValue(result);
        if (result) {
            packedString += BINOMIAL_TRUE;
        } else{
            packedString += BINOMIAL_FALSE;
        }
        this.setRawContentString(packedString);
        //create QR image
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        }
        catch (WriterException wException){
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
                setValue(true);
                break;
            case BinomialQRCode.BINOMIAL_FALSE:
                setValue(false);
                break;
        }
        try {
            this.setQrCodeImage(encodeStringToQR(rawContent));
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }

}
