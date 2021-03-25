package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;
/**
 * Role/Pattern:
 *     Class representing a QR Code containing  a reference to a Natural count Trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class NaturalQRCode extends QRCode{
    private int value;
    public NaturalQRCode(UUID experimentID, int value) {
        super(experimentID, QRType.NaturalCountTrial);
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += NATURALC_ID;
        packedString += String.valueOf(value);
        //create QR image
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
        this.setRawContentString(packedString);
    }
}
