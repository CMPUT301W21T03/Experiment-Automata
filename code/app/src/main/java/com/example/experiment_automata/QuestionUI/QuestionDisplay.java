package com.example.experiment_automata.QuestionUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.QuestionsModel.QuestionController;
import com.example.experiment_automata.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionDisplay#newInstance} factory method to
 * create an instance of this fragment.
 *
 * And to maintain the main page view of question display of the experiment.
 */
public class QuestionDisplay extends Fragment {

    public static final String QUESTION_EXPERIMENT = "QUESTION-UI-CURRENT-EXPERIMENT";


    private Experiment currentExperiment;
    private QuestionController questionController;
    private FloatingActionButton addQuestionButton;

    public QuestionDisplay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentExperiment is the experiment that contains the needed questions
     * @return A new instance of fragment QuestionDisplay.
     */

    public static QuestionDisplay newInstance(Experiment currentExperiment) {
        QuestionDisplay fragment = new QuestionDisplay();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_EXPERIMENT, currentExperiment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentExperiment = (Experiment) getArguments().getSerializable(QUESTION_EXPERIMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_display, container, false);

        //Getting rid of the floating button that adds experiments on every navigation
        getActivity().findViewById(R.id.add_experiment_button).setVisibility(View.GONE);



        return root;
    }
}