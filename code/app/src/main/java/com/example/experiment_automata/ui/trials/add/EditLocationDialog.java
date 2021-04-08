package com.example.experiment_automata.ui.trials.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.trials.Trial;

public class EditLocationDialog extends DialogFragment {

    public static final String PASSED_TRIAL= "LOCATION-RECV";
    private final int LONGITUDE_MAX = 181;
    private final int LONGITUDE_MIN = -181;
    private final int LATITUDE_MAX = 91;
    private final int LATITUDE_MIN = -91;

    private AddLocationListener listener;


    private EditText latInputBox;
    private EditText longInputBox;
    private Button revertButton;
    private Location current;


    /**
     * This is an interface for any activity using this fragment
     */
    public interface AddLocationListener {

        void onOkPressedLocationUpdate(Double lat, Double longi);
    }

    /**
     * This identifies the listener for the fragment when it attaches
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditLocationDialog.AddLocationListener) {
            listener = (EditLocationDialog.AddLocationListener) context;
        } else {
        }
    }

    private boolean validateInputLocation(double lat, double longi) {
        boolean latRange = lat < LATITUDE_MAX && lat > LATITUDE_MIN;
        boolean longiRange = longi < LONGITUDE_MAX && longi > LONGITUDE_MIN;
        return latRange && longiRange;
    }

    private void revertButtonClick() {
        if(current != null) {
            longInputBox.setText(current.getLongitude() + "");
            latInputBox.setText(current.getLatitude() + "");
        }
    }

    /**
     * This gives instructions for when creating this dialog and prepares it's dismissal
     * @param savedInstancesState
     *   allows you to pass information in if editing an experiment with existing info
     * @return
     *   the dialog that will be created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancesState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location_custom_display, null);

        // link all of the variables with their objects in the UI
        latInputBox = view.findViewById(R.id.add_dialog_frag_lati_input_box);
        longInputBox = view.findViewById(R.id.add_dialog_frag_longi_input_box);
        revertButton = view.findViewById(R.id.add_dialog_revert_to_orginal_loc_btn);

        AlertDialog.Builder build = new AlertDialog.Builder(getContext());

        // prepare the UI elements if experiment has existing information
        Bundle args = getArguments();
        current = ((Trial)(args.getSerializable(PASSED_TRIAL))).getLocation();
        latInputBox.setText(current.getLatitude() + "");
        longInputBox.setText(current.getLongitude() + "");
        revertButton.setOnClickListener(v -> revertButtonClick());

        return build.setView(view).setTitle("Location Customizer")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add Location", (dialog, which) -> {


                    Double inputLongitude = new Double(longInputBox.getText().toString());
                    Double inputLatitude = new Double(latInputBox.getText().toString());
                    while (!validateInputLocation(inputLatitude, inputLongitude)) {
                        Toast.makeText(getContext(), "Location Does not Exist",
                                Toast.LENGTH_LONG).show();
                        inputLongitude = new Double(longInputBox.getText().toString());
                        inputLatitude = new Double(latInputBox.getText().toString());
                        listener.onOkPressedLocationUpdate(inputLatitude, inputLongitude);
                    }
                    dismiss();
                }).create();
    }
}
