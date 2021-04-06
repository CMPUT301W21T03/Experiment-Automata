package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;
/**
 * Role/Pattern:
 *     Class representing a QR Code containing a reference to a Measurement Trial
 *
 *  Known Issue:
 *
 *      1. None
 */
public class MeasurementQRCode extends QRCode<Float>{

    public MeasurementQRCode(UUID experimentID, float value) {
        super(experimentID, QRType.MeasurementTrial);
        setValue(value);
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += MEASUREMENT_ID;
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
