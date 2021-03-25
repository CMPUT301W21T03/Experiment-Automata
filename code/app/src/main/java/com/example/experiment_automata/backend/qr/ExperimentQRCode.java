package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;

public class ExperimentQRCode extends QRCode{
    public ExperimentQRCode(UUID experimentID){//build a Binomial QR code
        super(experimentID,QRType.Experiment);
        //pack header
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += EXPERIMENT_ONLY_ID;
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
