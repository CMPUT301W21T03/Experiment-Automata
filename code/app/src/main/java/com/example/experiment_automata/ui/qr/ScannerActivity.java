package com.example.experiment_automata.ui.qr;
//parts of scanner implementation taken from ZXing-embedded example program https://github.com/journeyapps/zxing-android-embedded

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.experiment_automata.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Role/Pattern:
 *
 *       This class opens a camera viewport for scanner qr codes
 *
 * Known Issue:
 *
 *      1. no barcode/qr return functionality.
 *      2. Barcodes untested
 */
public class ScannerActivity extends AppCompatActivity {
    DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan code");
        integrator.setOrientationLocked(false);//enable screen rotation for zxing activity in manifest
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("SCANNER","Canceled scan" );
            } else {
                Log.d("SCANNER","Scanned: " + result.getContents() + " of type " + result.getContents().getClass().getName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }
}
