package com.example.experiment_automata.ui.qr;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.qr.BinomialQRCode;
import com.example.experiment_automata.backend.qr.CountQRCode;
import com.example.experiment_automata.backend.qr.ExperimentQRCode;
import com.example.experiment_automata.backend.qr.MeasurementQRCode;
import com.example.experiment_automata.backend.qr.NaturalQRCode;
import com.example.experiment_automata.backend.qr.QRCode;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Role/Pattern:
 *
 *       This class displays a QR code representing the current Experiment
 *
 * Known Issue:
 *
 *      1. Saves an image of the QR code anytime the share button is pressed, regardless if it's cancelled
 */
public class ViewQRFragment extends DialogFragment {
    private ImageView qrImageView;
    private TextView qrValue;
    private Button backButton;
    private ImageButton shareButton;
    private ImageButton saveButton;
    private Bitmap qrCodeImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_qr,container, true);
        backButton = view.findViewById(R.id.qr_code_back_button);
        shareButton = view.findViewById(R.id.qr_code_share);
        saveButton = view.findViewById(R.id.qr_code_save);
        qrImageView = view.findViewById(R.id.qr_code_imageView);
        qrValue = view.findViewById(R.id.qr_value_textView);
        QRCode qrCode;

        Bundle bundle = getArguments();
        String description = bundle.getString("DESCRIPTION");
        UUID experimentUUID = UUID.fromString(bundle.getString("UUID"));
        android.util.Log.d("QR EXPERIMENT ID", experimentUUID.toString());
        String typeString = bundle.getString("TYPE");
        //use switch case and string bundles instead of serializing QR
        switch (typeString){
            case "Experiment":
                qrCode = new ExperimentQRCode(experimentUUID);
                break;
            case "BinomialTrial":
                boolean binomialVal = bundle.getBoolean("BINVAL");
                qrCode = new BinomialQRCode(experimentUUID, binomialVal);
                break;
            case "CountTrial":
                qrCode = new CountQRCode(experimentUUID);
                break;
            case "MeasurementTrial":
                float measurementVal = bundle.getFloat("MEASVAL");
                qrCode = new MeasurementQRCode(experimentUUID, measurementVal);
                break;
            case "NaturalCountTrial":
                int naturalCount = bundle.getInt("NATVAL");
                qrCode = new NaturalQRCode(experimentUUID, naturalCount);
                break;
            default:
                qrCode = null;
        }

        assert qrCode != null;
        qrCodeImage = qrCode.getQrCodeImage();
        qrImageView.setImageBitmap(qrCodeImage);
        qrValue.setText(description);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        shareButton.setOnClickListener(v -> shareImage());
        saveButton.setOnClickListener(v -> saveImage());

        return view;
    }

    private void shareImage() {
        // Save image to cache
        Uri uri;
        final String filename = "qr.jpg";
        File cacheDir = new File(requireActivity().getCacheDir(), "images");
        cacheDir.mkdirs();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(String.format("%s/%s", cacheDir, filename));
            qrCodeImage.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            File f = new File(cacheDir, filename);
            uri = FileProvider.getUriForFile(requireContext(),
                    "com.example.experiment_automata.fileprovider", f);
        } catch (IOException e) {
            e.printStackTrace();
            uri = null;
        }

        // Following tutorial from: https://developer.android.com/training/sharing/send
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("image/jpg");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private String saveImage() {
        ContentResolver cr = requireActivity().getContentResolver();
        String url = MediaStore.Images.Media.insertImage(cr, qrCodeImage,
                UUID.randomUUID().toString(), "Experiment Automata QR code");
        final String saveMessage = "QR code saved!";
        View root = requireActivity().findViewById(R.id.nav_host_fragment);
        Snackbar notification = Snackbar.make(root, saveMessage, Snackbar.LENGTH_SHORT);
        notification.show();
        return url;
    }
}

