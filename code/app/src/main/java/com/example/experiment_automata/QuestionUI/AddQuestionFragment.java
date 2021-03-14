package com.example.experiment_automata.QuestionUI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.ExperimentFragments.AddExperimentFragment;
import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.R;

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

    /**
     * This gives instructions for creating a question
     * @param savedInstancesState
     *   allows you to pass information in if editing an experiment with existing info
     * @return
     *   the dialog that will be created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstancesState)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_edit_question_diolog_op_up, null);

        questionInput = view.findViewById(R.id.frag_add_edit_question_input_box_diolog);

        Bundle args = getArguments();

        if(args == null)
        {
            // Something 
        }
        else
        {
            // Something
        }

        return null;
    }
}
