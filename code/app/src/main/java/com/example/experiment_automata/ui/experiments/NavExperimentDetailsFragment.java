package com.example.experiment_automata.ui.experiments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.backend.qr.ViewQRFragment;
import com.example.experiment_automata.ui.question.QuestionDisplay;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.Screen;
import com.example.experiment_automata.ui.trials.MapDisplay.MapPointViewFragment;
import com.example.experiment_automata.ui.trials.TrialsFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
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

    public static final String CURRENT_EXPERIMENT_ID = "FRAGMENT_CURRENT_FRAGMENT-ID";


    private String experimentStringId;
    private TextView descriptionView;
    private TextView typeView;
    private ImageButton editImageButton;
    private ImageButton questionsButton;
    private ImageButton qrButton;
    private ImageButton mapButton;

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
        qrButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_qr_button);
        mapButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_map_button);

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

        qrButton.setOnClickListener(v -> {
            Experiment current = (((NavigationActivity)getActivity()).getExperimentManager())
                    .getAtUUIDDescription(UUID.fromString(experimentStringId));

            Fragment viewQRFragment = new ViewQRFragment();
            Bundle bundle = new Bundle();
            bundle.putString("UUID",CURRENT_EXPERIMENT_ID);
            bundle.putString("DESCRIPTION",current.getDescription());
            viewQRFragment.setArguments(bundle);
            //getActivity().getSupportFragmentManager().beginTransaction().show(viewQRFragment);
            getActivity().getSupportFragmentManager().beginTransaction().add(viewQRFragment,"QR").commit();
        });


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mapFragment = new MapPointViewFragment();
                Bundle neededMapData = new Bundle();
                neededMapData.putSerializable(MapPointViewFragment.CURRENT_EXPERIMENT,
                        (((NavigationActivity) getActivity())
                        .getExperimentManager())
                        .getAtUUIDDescription(UUID.fromString(experimentStringId)));
                mapFragment.setArguments(neededMapData);
                getActivity().getSupportFragmentManager().beginTransaction().add(mapFragment, "MAP-D").commit();
            }
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
    public void update(String experimentStringId) {
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
        } else {
            textViewQuartiles.setText(R.string.no_trials);
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

            // Charts
            BarDataSet histogramData = new BarDataSet(current.generateHistogram(), "");
            histogramData.setColors(R.color.purple_500);
            histogram.setData(new BarData(histogramData));
            histogram.getLegend().setEnabled(false);
            histogram.setDrawGridBackground(false);
            histogram.getXAxis().setDrawGridLines(false);
            histogram.getXAxis().setValueFormatter(new LargeValueFormatter());
            histogram.getAxisLeft().setEnabled(false);
            histogram.getAxisLeft().setAxisMinimum(0f);
            histogram.getAxisRight().setEnabled(false);
            histogram.setTouchEnabled(false);
            histogram.getDescription().setEnabled(false);
            histogram.invalidate();

            LineDataSet resultsData = new LineDataSet(current.generatePlot(), "");
            resultsData.setColor(R.color.purple_500);
            resultsData.setFillAlpha(255);
            resultsData.setLineWidth(2f);
            resultsData.setDrawCircles(false);
            resultsData.setDrawValues(false);
            resultsData.setCircleColor(R.color.purple_500);
            resultsData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            resultsPlot.setData(new LineData(resultsData));
            resultsPlot.getLegend().setEnabled(false);
            resultsPlot.getXAxis().setEnabled(false);
            histogram.getXAxis().setValueFormatter(new LargeValueFormatter());
            resultsPlot.setTouchEnabled(false);
            resultsPlot.getDescription().setEnabled(false);
            resultsPlot.invalidate();

            // trial results
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                if (fragment instanceof TrialsFragment) {
                    ((TrialsFragment) fragment).updateView();
                    break;
                }
            }
        } else {
            textViewMean.setText(R.string.no_trials);
            textViewMedian.setText(R.string.no_trials);
            textViewStdev.setText(R.string.no_trials);
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