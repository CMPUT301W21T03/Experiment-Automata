package com.example.experiment_automata;

import com.example.experiment_automata.backend.qr.QRMaker;
import com.example.experiment_automata.backend.qr.QRMalformattedException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

//cannot test getters/setters here because ZXing and bitmaps will not run within junit
public class QRTest {
    static final String TEST_CONTENT_MISSINGHEADER = "00000000-0000-0000-0000-000000000000e";
    static final String TEST_CONTENT_MALFORMATTED = "abc123";
    private QRMaker qrMaker;

    @Test
    public void missingHeaderTest(){
        boolean passed = true;
        qrMaker = new QRMaker();
        try {
            qrMaker.decodeQRString(TEST_CONTENT_MISSINGHEADER);
        } catch (QRMalformattedException qrMalException){
            passed = false;
        }
        assertFalse(passed);
    }

    @Test
    public void malformattedContentTest(){
        boolean passed = true;
        qrMaker = new QRMaker();
        try {
            qrMaker.decodeQRString(TEST_CONTENT_MALFORMATTED);
        } catch (QRMalformattedException qrMalException){
            passed = false;
        }
        assertFalse(passed);
    }
}
