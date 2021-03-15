package com.example.experiment_automata.QuestionUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.QuestionsModel.QuestionController;
import com.example.experiment_automata.QuestionsModel.QuestionManager;
import com.example.experiment_automata.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.protobuf.Internal;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionDisplay#newInstance} factory method to
 * create an instance of this fragment.
 *
 * And to maintain the main page view of question display of the experiment.
 *
 *
 * Known Issues:
 *  1. None
 */
public class QuestionDisplay extends Fragment {

    public static final String QUESTION_EXPERIMENT = "QUESTION-UI-CURRENT-EXPERIMENT";
    // this will be a string passed in if editing a question
    public static final String QUESTION = "QUESTION-STRING";
    // this will determine if this dialog is for a question or reply
    public static final String TYPE = "QUESTION-OR-REPLY";
    // this will be the ID of the experiment this belongs to
    public static final String EXPERIMENT = "EXPERIMENT-ID";


    private Experiment currentExperiment;
    private QuestionController questionController;
    private FloatingActionButton addQuestionButton;
    private ListView questionsDisplayList;
    private ArrayAdapter questionDisplayAdapter;
    private ArrayList questionsList;

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
        questionsDisplayList = root.findViewById(R.id.frag_questions_display_list_view);
        //Getting all the questions
        QuestionManager questionManager = ((NavigationActivity)(getActivity())).questionManager;


        //Getting rid of the floating button that adds experiments on every navigation
        getActivity().findViewById(R.id.add_experiment_button).setVisibility(View.GONE);

        addQuestionButton = root.findViewById(R.id.frag_question_display_add_question_button);
        addQuestionButton.setOnClickListener(v ->
        {
            makeQuestion();
        });


        return root;
    }

    private void makeQuestion()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(new AddQuestionFragment()
                        .newInstance("", true, currentExperiment.getExperimentId()), "ADD QUESTION")
                .commit();
    }
}