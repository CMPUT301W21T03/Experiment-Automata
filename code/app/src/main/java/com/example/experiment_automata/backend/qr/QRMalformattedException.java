package com.example.experiment_automata.backend.qr;

/**
 * Role/Pattern:
 *     Exception for when read content of QR code is malformatted
 *
 */
public class QRMalformattedException extends Exception{
    public QRMalformattedException(String errorMsg){
        super(errorMsg);
    }
}
