package com.example.experiment_automata.ui.trials.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.MeasurementExperiment;
import com.example.experiment_automata.backend.qr.MeasurementQRCode;
import com.example.experiment_automata.backend.qr.QRCode;
import com.example.experiment_automata.backend.qr.QRMaker;
import com.example.experiment_automata.backend.qr.QRMalformattedException;
import com.example.experiment_automata.backend.qr.QRType;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.qr.ScannerActivity;
import com.example.experiment_automata.ui.qr.ViewQRFragment;
import com.google.android.material.snackbar.Snackbar;
import com.example.experiment_automata.backend.trials.MeasurementTrial;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.trials.MapDisplay.MapUtility;

import org.osmdroid.views.MapView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeasurementTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeasurementTrialFragment extends Fragment {

    private ImageButton scanQRButton;
    private ImageButton viewQRButton;
    private View root;
    private EditText measurementValue;
    private MapView currentMapDisplay;
    private MapUtility utility;

    public AddMeasurementTrialFragment() {
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
        root = inflater.inflate(R.layout.fragment_add_measurement_trial, container, false);
        TextView description = (TextView) root.findViewById(R.id.measurement_trial_experiment_description);
        NavigationActivity parentActivity = ((NavigationActivity) getActivity());
        MeasurementExperiment experiment = (MeasurementExperiment) parentActivity.experimentManager.getCurrentExperiment();
        description.setText(experiment.getDescription());
        measurementValue = root.findViewById(R.id.add_measurement_value);

        scanQRButton = root.findViewById(R.id.add_measurement_qr_button);
        scanQRButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {//open scanner
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivityForResult(intent,1);
                //startActivity(intent);

            }
        });
        viewQRButton = root.findViewById(R.id.add_measurement_qr_generate_button);
        viewQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//display QR
                // add content when done
                Fragment viewQRFragment = new ViewQRFragment();
                Bundle bundle = new Bundle();
                bundle.putString("UUID", experiment.getExperimentId().toString());
                bundle.putString("DESCRIPTION",experiment.getDescription());
                bundle.putString("TYPE", QRType.MeasurementTrial.toString());
                try {
                    bundle.putFloat("MEASVAL", Float.parseFloat(measurementValue.getText().toString()));//check value?
                    viewQRFragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction().add(viewQRFragment, "QR").commit();
                } catch (NumberFormatException e) {
                    Snackbar.make(root, "Cannot create QR code with empty value", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        currentMapDisplay = root.findViewById(R.id.measurement_trial_experiment_map_view);

        parentActivity.currentTrial = new MeasurementTrial(parentActivity.loggedUser.getUserId(), 0);
        utility = new MapUtility(experiment, currentMapDisplay, getContext(), parentActivity, parentActivity.currentTrial);
        utility.setRevertBack(root.findViewById(R.id.add_measurment_trial_revert_loc_bttn));
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
        Log.d("ACTIVITYRESULT","val " + data.getStringExtra("QRCONTENTRAW"));
        QRMaker qrMaker = new QRMaker();
        QRCode qrCode;
        try{
            qrCode =qrMaker.decodeQRString(rawQRContent);
            if (qrCode.getType() == QRType.MeasurementTrial){
                measurementValue.setText(String.valueOf(((MeasurementQRCode)qrCode).getValue()));
                Log.d("SCANNER","Scanned QR Successfully!");
                Snackbar.make(root,"Scanned QR Successfully!", Snackbar.LENGTH_LONG).show();

            }
            else{
                //send error tray message
                Log.d("SCANNER","Scanned QR was of incorrect type " + qrCode.getType().toString());
                Snackbar.make(root, "Scanned QR was of incorrect type", Snackbar.LENGTH_LONG).show();
            }
        }
        catch (QRMalformattedException qrMalE){
            //malformatted QR
            qrCode = null;
            Log.d("SCANNER","Scanned Malformatted QR");
            Snackbar.make(root, "Scanned QR was not an Experiment-Automata QR Code", Snackbar.LENGTH_LONG).show();
        }
    }

}