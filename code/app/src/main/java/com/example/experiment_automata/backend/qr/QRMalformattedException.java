package com.example.experiment_automata.backend.qr;

/**
 * Role/Pattern:
 *     Exception for when read content of QR code is malformatted
 *
 *  Known Issue:
 *
 *      1. None
 */
public class QRMalformattedException extends Exception{
    public QRMalformattedException(String errorMsg){
        super(errorMsg);
    }
}
