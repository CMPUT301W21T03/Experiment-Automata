package com.example.experiment_automata.ui.qr;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.barcode.BarcodeReference;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.experiments.ExperimentManager;
import com.example.experiment_automata.backend.qr.QRCode;
import com.example.experiment_automata.backend.qr.QRMaker;
import com.example.experiment_automata.backend.qr.QRMalformattedException;
import com.example.experiment_automata.backend.trials.BinomialTrial;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.backend.trials.NaturalCountTrial;
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.experiments.NavExperimentDetailsFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collection;
import java.util.UUID;

/**
 * Fragment for setting up scanning QR codes and barcodes from the hamburger menu. It opens a ScannerActivity.
 *
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment {
    private NavigationActivity parentActivity;
    private ExperimentManager experimentManager;
    private User user;
    private Trial<?> trial;
    private Experiment experiment;

    public ScanQRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = ((NavigationActivity) getActivity());
        experimentManager = parentActivity.experimentManager;
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_q_r, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//when the ScannerActivity is finished
        super.onActivityResult(requestCode, resultCode, data);
        NavController navController = Navigation.findNavController(requireView());
        if (data == null) {
            navController.navigateUp();
            return;
        }
        user = parentActivity.loggedUser;
        String rawScannedContent = data.getStringExtra("QRCONTENTRAW");
        if (data.getBooleanExtra("IS_QR",true)) {
            handleQR(rawScannedContent);
        } else {
            handleBarcode(rawScannedContent);
        }
        navController.navigateUp();
        Bundle args = new Bundle();
        args.putString(NavExperimentDetailsFragment.CURRENT_EXPERIMENT_ID,
                experiment.getExperimentId().toString());
        parentActivity.experimentManager.setCurrentExperiment(experiment);
        navController.navigate(R.id.nav_experiment_details, args);
    }

    private void handleQR(@NonNull String scannedContent) {
        QRCode qrCode;
        QRMaker qrMaker = new QRMaker();
        try {
            Collection<UUID> subscriptions = user.getSubscriptions();
            qrCode = qrMaker.decodeQRString(scannedContent);
            //scanned valid QR
            experiment = experimentManager.getExperiment(qrCode.getExperimentID());
            if (experiment.isPublished() && experiment.isActive()){
                switch (qrCode.getType()){
                    case BinomialTrial:
                        trial = new BinomialTrial(user.getUserId(), (boolean) qrCode.getValue());
                        break;
                    case CountTrial:
                        trial = new CountTrial(user.getUserId());
                        break;
                    case NaturalCountTrial:
                        trial = new NaturalCountTrial(user.getUserId(), (int) qrCode.getValue());
                        break;
                    case MeasurementTrial:
                        trial = new MeasurementTrial(user.getUserId(), (float) qrCode.getValue());

                        break;
                    case Experiment:
                        break;
                }
                if (trial != null) {
                    if (experiment.isRequireLocation()) {
                        parentActivity.addLocationToTrial(trial);
                    }
                    parentActivity.addTrial(experiment, trial);
                    final String message = String.format("%s Trial with value %s scanned successfully in %s",
                            experiment.getType(), qrCode.getValue(), experiment.getDescription());
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(requireView(), "You are not subscribed to the Experiment contained in the scanned QR code", Snackbar.LENGTH_LONG).show();
            }
        } catch (QRMalformattedException qrMalE){
            //malformatted QR
            qrCode = null;
            Log.d("SCANNER","Scanned Malformatted QR");
            Snackbar.make(requireView(), "Scanned QR was not an Experiment-Automata QR Code", Snackbar.LENGTH_LONG).show();
        }
    }

    private void handleBarcode(@NonNull String scannedContent) {
        Log.d("SCANNER","Barcode was scanned");
        BarcodeReference barcodeReference = parentActivity.barcodeManager.getBarcode(scannedContent);
        if (barcodeReference == null) {
            //barcode not associated
            Snackbar.make(requireView(), "Scanned barcode has not been associated with a valid Trial yet", Snackbar.LENGTH_LONG).show();
        } else {
            //add given Trial to experiment
            Snackbar.make(requireView(),"Scanned barcode was found!",Snackbar.LENGTH_LONG).show();
            UUID barcodeExperimentID = barcodeReference.getExperimentId();
            Location barcodeLocation = barcodeReference.getLocation();
            experiment =  experimentManager.getExperiment(barcodeExperimentID);
            if(experiment.isPublished() && experiment.isActive()){
                experiment =  experimentManager.getExperiment(barcodeExperimentID);
                switch (barcodeReference.getType()){
                    case Binomial:
                        trial = new BinomialTrial(user.getUserId(), barcodeLocation,(Boolean) barcodeReference.getResult());
                        break;
                    case Count:
                        trial = new CountTrial(user.getUserId(), barcodeLocation);
                        break;
                    case NaturalCount:
                        trial = new NaturalCountTrial(user.getUserId(), barcodeLocation, (int) barcodeReference.getResult());
                        break;
                    case Measurement:
                        trial = new MeasurementTrial(user.getUserId(), barcodeLocation, (float) barcodeReference.getResult());
                        break;
                }
                parentActivity.addTrial(experiment, trial);
                final String message = String.format("%s Trial with value %s scanned successfully in %s",
                        experiment.getType(), barcodeReference.getResult(), experiment.getDescription());
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
            } else {
                experiment = null;
                Snackbar.make(requireView(),"The scanned barcode is not published",Snackbar.LENGTH_LONG).show();
            }
        }
    }
}