package com.example.experiment_automata.backend.qr;

import com.google.zxing.WriterException;

import java.util.UUID;

/**
 * Role/Pattern:
 *     Class representing a QR Code containing only an Experiment Reference
 *
 */
public class ExperimentQRCode extends QRCode<Object> {
    public ExperimentQRCode(UUID experimentID) {
        super(experimentID, QRType.Experiment);
        String packedString = "";
        packedString += AUTOMATA_QR_HEADER;
        packedString += experimentID.toString();
        packedString += EXPERIMENT_ONLY_ID;
        try {
            this.setQrCodeImage(encodeStringToQR(packedString));
        } catch (WriterException wException) {
            wException.printStackTrace();
        }
    }
}
