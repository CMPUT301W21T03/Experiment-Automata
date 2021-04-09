package com.example.experiment_automata.ui.question;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.experiment_automata.backend.experiments.Experiment;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.Screen;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
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

    private Experiment<?> currentExperiment;
    ListView questionsDisplayList;
    ArrayList<Question> questionsList = new ArrayList<>();

    /**
     * Retrieves the current experiment to query questions on creation
     * @param savedInstanceState the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NavigationActivity)requireActivity()).setCurrentFragment(this);
        if (getArguments() != null) {
            currentExperiment = (Experiment<?>) getArguments().getSerializable(QUESTION_EXPERIMENT);
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
     * @return the view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_display, container, false);
        questionsDisplayList = root.findViewById(R.id.frag_questions_display_list_view);
        //Getting all the questions

        ((NavigationActivity)requireActivity()).setCurrentFragment(this);

        try {
            ((NavigationActivity)requireActivity()).questionManager.getQuestionsFromFirestore();

            questionsList = ((NavigationActivity) (requireActivity()))
                    .questionManager
                    .getExperimentQuestions(currentExperiment.getExperimentId());
        } catch (Exception ignored) {}

        SingleQuestionDisplay questionDisplayAdapter = new SingleQuestionDisplay(getContext(), questionsList, requireActivity());
        questionsDisplayList.setAdapter(questionDisplayAdapter);

        //Getting rid of the floating button that adds experiments on every navigation
        requireActivity().findViewById(R.id.fab_button).setVisibility(View.GONE);

        return root;
    }

    /**
     * Creates a dialog for question creation
     */
    public void makeQuestion() {
        requireActivity().getSupportFragmentManager().beginTransaction()
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
    public void updateQuestionsList() {
        questionsDisplayList.setAdapter(new SingleQuestionDisplay(getContext(), ((NavigationActivity) requireActivity())
                .questionManager
                .getExperimentQuestions(currentExperiment
                        .getExperimentId()), requireActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.fab_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((NavigationActivity)(requireActivity())).setCurrentScreen(Screen.ExperimentDetails);
    }
}
