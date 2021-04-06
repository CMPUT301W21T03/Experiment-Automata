package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Class representing a QR Code containing a reference to a Count Trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class CountQRCode extends QRCode<Object>{
    public CountQRCode(UUID experimentID) {
        super(experimentID, QRType.CountTrial);
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += COUNT_ID;
        //create QR image
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        }
        catch (WriterException wException){
            //return special bitmap maybe?
            wException.printStackTrace();
        }
    }

}
