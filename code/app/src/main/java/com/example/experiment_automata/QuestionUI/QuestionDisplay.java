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
import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.QuestionsModel.QuestionController;
import com.example.experiment_automata.QuestionsModel.QuestionManager;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.Screen;
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
 *  1. If you try and add a second reply, app crashes (partly by design). Determine
 *     if we should expect multiple replies to a single question or not
 *  2. At the moment, anyone can reply to a question. Add testing to ensure that
 *     owner can not ask questions and that only the only can answer them
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
    private ArrayAdapter<Question> questionDisplayAdapter;
    ArrayList<Question> questionsList = new ArrayList<>();

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

    /**
     * Retrieves the current experiment to query questions on creation
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NavigationActivity)getActivity()).setCurrentFragment(this);
        if (getArguments() != null) {
            currentExperiment = (Experiment) getArguments().getSerializable(QUESTION_EXPERIMENT);
        }
    }

    /**
     * Creates the view for the fragment and connects the components
     * @param inflater
     *   Inflater used to create the root view
     * @param container
     *   Viewgroup containing any information that needs to be shared with the activity
     * @param savedInstanceState
     *   Bundle of parameters if needed
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_display, container, false);
        ListView questionsDisplayList = root.findViewById(R.id.frag_questions_display_list_view);
        //Getting all the questions

        ((NavigationActivity)getActivity()).setCurrentFragment(this);

        try {
            questionsList = ((NavigationActivity) (getActivity()))
                    .questionManager
                    .getExperimentQuestions(currentExperiment.getExperimentId());
        }
        catch (Exception e)
        {
            //TODO: find a better way to deal with this situation
        }

        questionDisplayAdapter = new SingleQuestionDisplay(getContext(), questionsList, getActivity());
        questionsDisplayList.setAdapter(questionDisplayAdapter);

        //Getting rid of the floating button that adds experiments on every navigation
        getActivity().findViewById(R.id.add_experiment_button).setVisibility(View.GONE);

        FloatingActionButton addQuestionButton = root.findViewById(R.id.frag_question_display_add_question_button);
        addQuestionButton.setOnClickListener(v ->
        {
            makeQuestion();
        });

        return root;
    }

    /**
     * Creates a dialog for question creation
     */
    private void makeQuestion()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(AddQuestionFragment
                        .newInstance("", true, currentExperiment.getExperimentId()), "ADD QUESTION")
                .commit();
    }

    /**
     * Resets the experiment so that it displays onto the screen.
     * Source:
     *  Author: https://stackoverflow.com/users/1366455/tolgap
     *  Editor: None
     *  Link: https://stackoverflow.com/questions/15422120/notifydatasetchange-not-working-from-custom-adapter
     */
    public void updateQuestionsList()
    {
        try {
            questionsList.clear();
            questionsList.addAll(
                    ((NavigationActivity)getActivity())
                            .questionManager
                            .getExperimentQuestions(currentExperiment
                                    .getExperimentId()));
            if(questionDisplayAdapter != null) {
                Log.d("updateAdapter", "not null");
                questionDisplayAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            //TODO: deal with this at some point
        }

    }

}