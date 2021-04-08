package com.example.experiment_automata;

import com.example.experiment_automata.backend.qr.QRMaker;
import com.example.experiment_automata.backend.qr.QRMalformattedException;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QRTest {
    static final UUID TEST_UUID1 = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final UUID TEST_UUID2 = UUID.fromString("10000000-0000-0000-0000-000000000000");
    static final String TEST_CONTENT_EXPERIMENT = "ATMA00000000-0000-0000-0000-000000000000e";//experiment
    static final String TEST_CONTENT_BIN = "ATMA00000000-0000-0000-0000-000000000000bt";//bin trial true
    static final String TEST_CONTENT_MISSINGHEADER = "00000000-0000-0000-0000-000000000000e";
    static final String TEST_CONTENT_MALFORMATTED = "abc123";
    private QRMaker qrMaker;

    //cannot test getters/setters here because ZXing and bitmaps will not run within junit


    @Test
    public void missingHeaderTest(){
        boolean passed = true;
        qrMaker = new QRMaker();
        try {
            qrMaker.decodeQRString(TEST_CONTENT_MISSINGHEADER);
        } catch (QRMalformattedException qrMalException){
            passed = false;
        }
        assertEquals(passed, false);
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
        assertEquals(passed, false);
    }
}
