package com.example.experiment_automata.ui.experiments;

import android.annotation.SuppressLint;
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

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.backend.qr.QRType;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.Screen;
import com.example.experiment_automata.ui.qr.ViewQRFragment;
import com.example.experiment_automata.ui.question.QuestionDisplay;
import com.example.experiment_automata.ui.trials.MapDisplay.MapDisplayFragment;
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
    private Experiment<?> experiment;
    private TextView descriptionView;
    private TextView typeView;
    private TextView minTrials;
    private ImageButton editImageButton;
    private ImageButton subscribeButton;
    private ImageButton questionsButton;
    private ImageButton qrButton;
    private ImageButton mapButton;

    private TextView textViewQuartiles;
    private TextView textViewMean;
    private TextView textViewMedian;
    private TextView textViewStdev;
    private BarChart histogram;
    private LineChart resultsPlot;
    private NavigationActivity parentActivity;

    public NavExperimentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     *  Prebuilt that initializes the given parameters
     * @param savedInstanceState the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experimentStringId = getArguments().getString(CURRENT_EXPERIMENT_ID);
            parentActivity = (NavigationActivity) requireActivity();
            assert parentActivity != null;
            parentActivity.setCurrentFragment(this);
            parentActivity.setCurrentScreen(Screen.ExperimentDetails);
            experiment = parentActivity.getExperimentManager()
                    .getAtUUIDDescription(UUID.fromString(experimentStringId));
        }
    }


    /**
     *  Reads and sets up the view for the user to see the whole experiment
     * @param inflater the layout inflater
     * @param container the container
     * @param savedInstanceState the saved instance
     * @return
     *  root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nav_experiment_details, container, false);

        descriptionView = root.findViewById(R.id.nav_experiment_details_description);
        typeView = root.findViewById(R.id.nav_experiment_details_experiment_type);
        minTrials = root.findViewById(R.id.nav_experiment_details_min_trials);
        editImageButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_edit_button);
        subscribeButton = root.findViewById(R.id.nav_fragment_experiment_view_subscribe_button);
        questionsButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_qa_button);
        qrButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_qr_button);
        mapButton = root.findViewById(R.id.nav_fragment_experiment_detail_view_map_button);

        requireActivity().findViewById(R.id.fab_button).setVisibility(View.GONE);

        textViewQuartiles = root.findViewById(R.id.quartiles_value);
        textViewMean = root.findViewById(R.id.mean_value);
        textViewMedian = root.findViewById(R.id.median_value);
        textViewStdev = root.findViewById(R.id.stdev_value);

        histogram = root.findViewById(R.id.histogram_chart);
        resultsPlot = root.findViewById(R.id.results_chart);



        setButtons();

        return root;
    }

    /**
     * Sets the onclick listeners and visibility for buttons.
     */
    private void setButtons() {
        boolean isOwner = experiment.getOwnerId().equals(parentActivity.loggedUser.getUserId());

        if (isOwner) {
            editImageButton.setVisibility(View.VISIBLE);
            editImageButton.setOnClickListener(v -> {
                Fragment editExperiment = new AddExperimentFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddExperimentFragment.ADD_EXPERIMENT_CURRENT_VALUE,
                        (((NavigationActivity) requireActivity())
                                .getExperimentManager())
                                .getAtUUIDDescription(UUID.fromString(experimentStringId)));

                editExperiment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().add(editExperiment, "EDIT").commit();
            });
        } else {
            editImageButton.setVisibility(View.GONE);
        }

        subscribeButton.setVisibility(View.VISIBLE);
        subscribeButton.setOnClickListener(v -> {
            parentActivity.loggedUser.subscribeExperiment(experiment.getExperimentId());
            toggleSubscribeButton();
        });

        questionsButton.setOnClickListener(v -> launchQuestionView());

        qrButton.setOnClickListener(v -> {
            Experiment<?> current = (((NavigationActivity) requireActivity()).getExperimentManager())
                    .getAtUUIDDescription(UUID.fromString(experimentStringId));

            Fragment viewQRFragment = new ViewQRFragment();
            Bundle bundle = new Bundle();
            bundle.putString("UUID", current.getExperimentId().toString());
            bundle.putString("DESCRIPTION", current.getDescription());
            bundle.putString("TYPE", QRType.Experiment.toString());
            viewQRFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().add(viewQRFragment,"QR").commit();
        });

        mapButton.setOnClickListener(v -> {
            Bundle locArgs = new Bundle();
            locArgs.putSerializable(MapDisplayFragment.CURRENT_EXPERIMENT,
                    (((NavigationActivity) requireActivity())
                    .getExperimentManager())
                    .getAtUUIDDescription(UUID.fromString(experimentStringId)));

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.map_display_fragment, locArgs);
            parentActivity.setCurrentScreen(Screen.MAP);
            parentActivity.setCurrentFragment(this);
            parentActivity.findViewById(R.id.fab_button).setVisibility(View.GONE);
        });

        if (experimentStringId != null) {
            update(experimentStringId);
        }
    }

    /**
     *
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     *
     * @param experimentStringId : The unique id that each experiment contains
     */
    @SuppressLint("DefaultLocale")
    public void update(String experimentStringId) {
        Log.d("UPDATE", "Screen info updated");
        Experiment<?> current = (((NavigationActivity) requireActivity()).getExperimentManager())
                .getAtUUIDDescription(UUID.fromString(experimentStringId));
        descriptionView.setText(current.getDescription());
        typeView.setText(current.getType().toString());
        minTrials.setText(String.format("Minimum Trials: %d", current.getMinTrials()));

        // Disable FAB if not accepting new trials
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab_button);
        if (!current.isActive()) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }

        toggleSubscribeButton();


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
    }

    /**
     *
     * Caller function that sets up the views when an update to the data happens.
     *
     */
    public void updateScreen() {
        update(experimentStringId);
    }

    private void toggleSubscribeButton() {
        boolean subscribed = parentActivity.loggedUser.getSubscriptions()
                .contains(experiment.getExperimentId());
        if (subscribed) {
            subscribeButton.setImageResource(R.drawable.ic_baseline_star);
        } else {
            subscribeButton.setImageResource(R.drawable.ic_baseline_star_border);
        }
    }

    /**
     * Set up and gets read to launch the questions display.
     */
    private void launchQuestionView() {
        requireActivity().findViewById(R.id.fab_button).setVisibility(View.VISIBLE);
        NavigationActivity parentActivity = (NavigationActivity) requireActivity();
        Bundle questionBundle = new Bundle();
        assert parentActivity != null;
        questionBundle.putSerializable(QuestionDisplay.QUESTION_EXPERIMENT,
                parentActivity.getExperimentManager()
                    .getAtUUIDDescription(UUID.fromString(experimentStringId)));
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.questionDisplay, questionBundle);
        parentActivity.setCurrentScreen(Screen.Questions);
        parentActivity.setCurrentFragment(this);
    }
}
