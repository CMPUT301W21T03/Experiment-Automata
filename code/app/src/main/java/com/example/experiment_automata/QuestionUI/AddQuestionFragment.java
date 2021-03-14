package com.example.experiment_automata.QuestionUI;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.ExperimentFragments.AddExperimentFragment;
import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.QuestionsModel.Question;

/**
 * Role/Pattern:
 *
 *       This class provides the framework needed to make and edit questions
 *
 * Known Issue:
 *
 *      1. None
 */

// Basic layout of this fragment inspired by lab work in CMPUT 301
// Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain,
// https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class AddQuestionFragment extends DialogFragment
{
    public static final String QUESTION_KEY = "QUESTION";

    private EditText questionInput;
    private AddExperimentFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Question newQuestion);
    }

    /**
     * This identifies the listener for the fragment when it attaches
     * @param context
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof AddQuestionFragment.OnFragmentInteractionListener)
        {
            listener = (AddExperimentFragment.OnFragmentInteractionListener) context;
        } else {

            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This will create a new instance of this fragment with an experiment
     * @param question
     *   The question that will be edited
     * @return
     *   a fragment to edit an questions's information
     */
    public static AddQuestionFragment newInstance(Question question)
    {
        AddQuestionFragment questionFragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_KEY, question);
        questionFragment.setArguments(args);
        return questionFragment;
    }
}
