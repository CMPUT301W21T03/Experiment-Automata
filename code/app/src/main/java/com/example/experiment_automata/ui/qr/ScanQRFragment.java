package com.example.experiment_automata.ui.qr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.experiment_automata.backend.trials.Trial;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.NavigationActivity;
import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment for setting up scanning QR codes and barcodes from the hamburger menu. It opens a ScannerActivity.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanQRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanQRFragment extends Fragment {
    private NavigationActivity parentActivity;
    private ExperimentManager experimentManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanQRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanQR.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanQRFragment newInstance(String param1, String param2) {
        ScanQRFragment fragment = new ScanQRFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = ((NavigationActivity) getActivity());
        experimentManager = parentActivity.experimentManager;
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        startActivityForResult(intent,1);

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
        if (data == null) {
            return;
        }
        String rawScannedContrent = data.getStringExtra("QRCONTENTRAW");
        if (data.getBooleanExtra("IS_QR",true)){//Scanned obj was QR
            QRCode qrCode;
            QRMaker qrMaker = new QRMaker();
            try{
                qrCode = qrMaker.decodeQRString(rawScannedContrent);
                //scanned valid QR
                if (experimentManager.containsExperiment(qrCode.getExperimentID())){//change this to check subscribed experiments
                    //add location ability later
                    User user = parentActivity.loggedUser;
                    Trial trial;
                    switch (qrCode.getType()){
                        case BinomialTrial: //temp test implementation ONLY FOR BINOMIAL TRIALS
                            trial = new BinomialTrial(user.getUserId(),(Boolean) qrCode.getValue());
                            Experiment binExperiment =  experimentManager.getExperiment(qrCode.getExperimentID());//don't think this will work
                            parentActivity.addTrial(binExperiment,trial);
                            break;
                        case CountTrial:
                            break;
                        case NaturalCountTrial:
                            break;
                        case MeasurementTrial:
                            break;
                    }


                }
                else{
                    Snackbar.make(getView(),"Experiment in QR not found",Snackbar.LENGTH_LONG).show();
                }
            }
            catch (QRMalformattedException qrMalE){
                //malformatted QR
                qrCode = null;
                Log.d("SCANNER","Scanned Malformatted QR");
                Snackbar.make(getView(),"Scanned QR was not an Experiment-Automata QR Code",Snackbar.LENGTH_LONG).show();

            }
        }
        else{//scanned obj was barcode
            Log.d("SCANNER","Barcode was scanned");
            BarcodeReference barcodeReference = parentActivity.barcodeManager.getBarcode(rawScannedContrent);
            if (barcodeReference == null) {
                //barcode not associated
                Snackbar.make(getView(),"Scanned barcode has not been associated with a valid Trial yet",Snackbar.LENGTH_LONG).show();
            }
            else{
                //add given Trial to experiment
                Snackbar.make(getView(),"Scanned barcode was found!",Snackbar.LENGTH_LONG).show();
            }
        }
        NavController navController = Navigation.findNavController(getView());
        navController.navigateUp();//return to parent
    }


}