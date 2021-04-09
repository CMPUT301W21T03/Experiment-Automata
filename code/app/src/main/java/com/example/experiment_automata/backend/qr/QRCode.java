package com.example.experiment_automata.backend.qr;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.UUID;
/**
 * Role/Pattern:
 *     Class representing a QR code through it's image and content fields.
 *     format is oulined here: https://github.com/CMPUT301W21T03/Experiment-Automata/wiki/Custom-QR-Code-Content-Format
 */
public abstract class QRCode<T> {
    //Header for custom QR codes
    static final String AUTOMATA_QR_HEADER = "ATMA";
    static final int DEAFULT_QR_HEIGHT = 600;
    static final int DEFAULT_QR_WIDTH = 600;
    static final String BINOMIAL_ID = "b";
    static final String COUNT_ID = "c";
    static final String MEASUREMENT_ID = "m";
    static final String NATURALC_ID = "n";
    static final String EXPERIMENT_ONLY_ID = "e";
    private final UUID experimentID;
    private QRType type;
    private Bitmap qrCodeImage;
    private T value;

    public QRCode(UUID experimentID, QRType type) {
        this.experimentID = experimentID;
        this.type = type;
    }

    public QRCode(String rawContentString) {
        this.experimentID = UUID.fromString(rawContentString.substring(4, 41));
    }

    /**
     * Encodes a string as a QRCode
     * @param encodedContent
     * string to be encoded
     * @return
     * returns a Bitmap containing the QRCode
     */
    public Bitmap encodeStringToQR(String encodedContent) throws WriterException {
        BitMatrix qrCodeBitMatrix;
        Bitmap qrCodeBitmap;
        try{
            qrCodeBitMatrix = new QRCodeWriter().encode(encodedContent, BarcodeFormat.QR_CODE,DEFAULT_QR_WIDTH,DEAFULT_QR_HEIGHT);
        }
        catch (IllegalArgumentException illegalArgException) {
            // Unsupported format, required for encode
            return null;
        }
        //convert BitMatrix to Bitmap
        //Bitmap conversion from ρяσѕρєя K,https://stackoverflow.com/questions/19337448/generate-qr-code-directly-into-imageview
        int width = qrCodeBitMatrix.getWidth();
        int height = qrCodeBitMatrix.getHeight();
        qrCodeBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                qrCodeBitmap.setPixel(x, y, qrCodeBitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return qrCodeBitmap;
    }

    public void setType(QRType type) {
        this.type = type;
    }

    public void setQrCodeImage(Bitmap qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public QRType getType() {
        return type;
    }

    public UUID getExperimentID() {
        return experimentID;
    }

    public Bitmap getQrCodeImage() {
        return qrCodeImage;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
