package com.example.experiment_automata.ExperimentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.QuestionUI.QuestionDisplay;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.Screen;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.local.BundleCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

/**
 * Role/Pattern:
 *      This class deals with the whole view of the experiment that the user has made.
 *      Giving the user the whole display and information needed to maintain experiments.
 *
 * Known Issue:
 *
 *      1. None
 */

public class NavExperimentDetailsFragment extends Fragment {

    private String ERROR_LOG_VALUE = "ERROR_LOG-EXPERIMENT-VIEW";
    public static final String CURRENT_EXPERIMENT_ID = "FRAGMENT_CURRENT_FRAGMENT-ID";


    private String experimentStringId;
    private TextView descriptionView;
    private TextView typeView;
    private ImageButton editImageButton;
    private ImageButton questionsButton;

    private TextView textViewQuartiles;
    private TextView textViewMean;
    private TextView textViewMedian;
    private TextView textViewStdev;
    private BarChart histogram;
    private LineChart resultsPlot;


    public NavExperimentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param experimentStringId ID of the clicked experiment
     * @return
     *  A new instance of fragment FullExperimentView.
     */

    public static NavExperimentDetailsFragment newInstance(String experimentStringId) {
        NavExperimentDetailsFragment fragment = new NavExperimentDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_EXPERIMENT_ID, experimentStringId);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     *  Prebuilt that initializes the given parameters
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experimentStringId = getArguments().getString(CURRENT_EXPERIMENT_ID);
        }
    }


    /**
     *  Reads and sets up the view for the user to see the whole experiment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *  root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nav_experiment_details, container, false);

        NavigationActivity parentActivity = (NavigationActivity) getActivity();
        parentActivity.setCurrentFragment(this);
        parentActivity.setCurrentScreen(Screen.ExperimentDetails);

        descriptionView = root.findViewById(R.id.nav_experiment_details_description);
        typeView = root.findViewById(R.id.nav_experiment_details_experiment_type);
        editImageButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_edit_button);
        questionsButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_qa_button);
        getActivity().findViewById(R.id.add_experiment_button).setVisibility(View.GONE);

        textViewQuartiles = root.findViewById(R.id.quartiles_value);
        textViewMean = root.findViewById(R.id.mean_value);
        textViewMedian = root.findViewById(R.id.median_value);
        textViewStdev = root.findViewById(R.id.stdev_value);

        histogram = root.findViewById(R.id.histogram_chart);
        resultsPlot = root.findViewById(R.id.results_chart);

        if (experimentStringId != null) {
            update(experimentStringId);
        }

        editImageButton.setOnClickListener(v -> {
            Fragment editExperiment = new AddExperimentFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AddExperimentFragment.ADD_EXPERIMENT_CURRENT_VALUE,
                    (((NavigationActivity) getActivity())
                    .getExperimentManager())
                    .getAtUUIDDescription(UUID.fromString(experimentStringId)));

            editExperiment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().add(editExperiment,"EDIT").commit();
        });

        questionsButton.setOnClickListener(v -> {
            launchQuestionView();
        });
        return root;
    }

    /**
     *
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     *
     * @param experimentStringId : The unique id that each experiment contains
     */
    private void update(String experimentStringId) {
        Log.d("UPDATE", "Screen info updated");
        Experiment current = (((NavigationActivity)getActivity()).getExperimentManager())
                .getAtUUIDDescription(UUID.fromString(experimentStringId));
        descriptionView.setText(current.getDescription());
        typeView.setText("" + current.getType());

        // Disable FAB if not accepting new trials
        FloatingActionButton fab = getActivity().findViewById(R.id.add_experiment_button);
        if (!current.isActive()) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }

        if (current.getSize() >= 3) {
            float[] quartiles = current.getQuartiles();
            String quartileString = String.format("q1: %.4f, q3: %.4f", quartiles[0], quartiles[2]);
            textViewQuartiles.setText(quartileString);
        }

        if (current.getSize() >= 1) {
            float mean = current.getMean();
            float median = current.getMedian();
            float stdev = current.getStdev();

            String meanString = String.format("%.4f", mean);
            String medianString = String.format("%.4f", median);
            String stdevString = String.format("%.4f", stdev);

            textViewMean.setText(meanString);
            textViewMedian.setText(medianString);
            textViewStdev.setText(stdevString);

            BarDataSet histogramData = new BarDataSet(current.generateHistogram(), "Histogram");
            histogram.setData(new BarData(histogramData));

            LineDataSet resultsData = new LineDataSet(current.generatePlot(), "Results over time");
            resultsPlot.setData(new LineData(resultsData));
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton fab = getActivity().findViewById(R.id.add_experiment_button);
        fab.setVisibility(View.VISIBLE);
    }

    /**
     *
     * Caller function that sets up the views when an update to the data happens.
     *
     */
    public void updateScreen() {
        update(experimentStringId);
    }


    /**
     * Set up and gets read to launch the questions display.
     */
    private void launchQuestionView() {
        NavigationActivity parentActivity = (NavigationActivity) getActivity();
        Bundle questionBundle = new Bundle();
        questionBundle.putSerializable(QuestionDisplay.QUESTION_EXPERIMENT,
                parentActivity.getExperimentManager()
                    .getAtUUIDDescription(UUID.fromString(experimentStringId)));
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.questionDisplay, questionBundle);
        parentActivity.setCurrentScreen(Screen.Questions);
        parentActivity.setCurrentFragment(this);
    }
}