package com.example.experiment_automata.QRCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;

/**
 * Role/Pattern:
 *
 *       This class displays a QR code representing the current Experiment
 *
 * Known Issue:
 *
 *      1. None
 */
public class ViewQRFragment extends DialogFragment {

    private ImageView qrImageView;
    private Button backButton;
    private String experimentUUIDString;
    private QRCodeManager qrManager;
    private Bitmap qrCode;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_qr,container, false);
        backButton = view.findViewById(R.id.qr_code_back_button);
        qrImageView = view.findViewById(R.id.qr_code_imageView);
        qrManager = new QRCodeManager();

        Bundle bundle = getArguments();
        experimentUUIDString = bundle.getString("UUID");
        qrCode = qrManager.createQRFromUUID(experimentUUIDString);
        qrImageView.setImageBitmap(qrCode);


        return view;
    }
}

