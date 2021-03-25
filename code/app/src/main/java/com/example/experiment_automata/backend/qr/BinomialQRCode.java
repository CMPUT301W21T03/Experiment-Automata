package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;

public class BinomialQRCode extends QRCode{
    static final String BINOMIAL_TRUE = "t";
    static final String BINOMIAL_FALSE = "f";
    private Boolean value;
    public BinomialQRCode(UUID experimentID,Boolean value) {
        super(experimentID, QRType.BinomialTrial);
        this.value = value;
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += BINOMIAL_ID;
        this.setRawContentString(packedString);
        //pack content
        this.value = value;
        if(value){
            packedString += BINOMIAL_TRUE;
        }
        else{
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
    public boolean getValue() {
        return value;
    }

}

