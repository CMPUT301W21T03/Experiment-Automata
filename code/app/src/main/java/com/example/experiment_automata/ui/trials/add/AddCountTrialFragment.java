package com.example.experiment_automata.ui.trials.add;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.CountExperiment;
import com.example.experiment_automata.backend.qr.QRCode;
import com.example.experiment_automata.backend.qr.QRMaker;
import com.example.experiment_automata.backend.qr.QRMalformattedException;
import com.example.experiment_automata.backend.qr.QRType;
import com.example.experiment_automata.backend.trials.CountTrial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.qr.ScannerActivity;
import com.example.experiment_automata.ui.qr.ViewQRFragment;
import com.example.experiment_automata.ui.trials.MapDisplay.MapUtility;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.views.MapView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCountTrialFragment extends Fragment {
    private View root;

    public AddCountTrialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_count_trial, container, false);
        TextView description = root.findViewById(R.id.count_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) requireActivity());
        CountExperiment experiment = (CountExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());
        ImageButton scanQRButton = root.findViewById(R.id.count_trial_qr_button);
        scanQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ScannerActivity.class);
            startActivityForResult(intent,1);
        });

        ImageButton viewQRButton = root.findViewById(R.id.count_trial_qr_generate_button);
        viewQRButton.setOnClickListener(v -> {
            Fragment viewQRFragment = new ViewQRFragment();
            Bundle bundle = new Bundle();
            bundle.putString("UUID", experiment.getExperimentId().toString());
            bundle.putString("DESCRIPTION",experiment.getDescription());
            bundle.putString("TYPE", QRType.CountTrial.toString());
            viewQRFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().add(viewQRFragment,"QR").commit();
        });
        CountExperiment currentExperiment = (CountExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(currentExperiment.getDescription());
        MapView currentMapDisplay = root.findViewById(R.id.count_trial_map_view);

        parentActivity.currentTrial = new CountTrial(parentActivity.loggedUser.getUserId(), 0);
        MapUtility utility = new MapUtility(currentExperiment, currentMapDisplay, getContext(), parentActivity, parentActivity.currentTrial);
        utility.setRevertBack(root.findViewById(R.id.add_count_trial_revert_loc_bttn));
        utility.run();

        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }
        String rawQRContent =  data.getStringExtra("QRCONTENTRAW");
        if (data.getBooleanExtra("IS_QR",true)) {
            QRMaker qrMaker = new QRMaker();
            QRCode<?> qrCode;
            try {
                qrCode = qrMaker.decodeQRString(rawQRContent);
                if (qrCode.getType() == QRType.CountTrial) {
                    Snackbar.make(root, "Scanned QR Successfully!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(root, "Scanned QR was of incorrect type", Snackbar.LENGTH_LONG).show();
                }
            } catch (QRMalformattedException qrMalE) {
                Snackbar.make(root, "Scanned QR was not an Experiment-Automata QR Code", Snackbar.LENGTH_LONG).show();
            }
        } else {
            NavigationActivity parentActivity = ((NavigationActivity) requireActivity());
            Location location = parentActivity.currentTrial.getLocation();
            CountExperiment experiment = (CountExperiment) parentActivity.experimentManager.getCurrentExperiment();
            parentActivity.barcodeManager.addBarcode(rawQRContent,experiment.getExperimentId(),location);
            Snackbar.make(root, "Scanned Barcode " + rawQRContent + " was associated with this Trials Value", Snackbar.LENGTH_LONG).show();
        }
    }
}
