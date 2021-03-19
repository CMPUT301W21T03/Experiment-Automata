package com.example.experiment_automata.QuestionUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;

import java.util.UUID;

/**
 * Role/Pattern:
 *
 *       This class provides a dialog that will return the string inputted for a question
 *       If you want to use a question, pass true into the type parameter
 *       If you want to use a reply, pass false into the type parameter
 *       You should pass an empty string as the first parameter unless editing a question/reply
 *
 * Known Issue:
 *
 *      1. currently is only designed to create questions
 *      2. will need to add the ability to create replies
 *      3. not sure if we need the ability to edit questions/replies
 */

// note for developing:
// If you want to use a question, pass true into the type parameter
// If you want to use a reply, pass false into the type parameter
// You should pass an empty string as the first parameter unless editing a question/reply

// Basic layout of this fragment inspired by lab work in CMPUT 301
// Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain,
// https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class AddQuestionFragment extends DialogFragment
{
    // this will be a string passed in if editing a question
    public static final String QUESTION = "QUESTION-STRING";
    // this will determine if this dialog is for a question or reply
    public static final String TYPE = "QUESTION-OR-REPLY";
    // this will be the ID of the experiment or question this belongs to (depending on reply vs question)
    public static final String OWNER = "OWNER-ID";

    private EditText questionInput;
    private String experimentId;
    private String question;

    private AddQuestionFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener
    {
        void onOkPressedQuestion(String question, UUID Experiment);
        void onOkPressedReply(String reply, UUID questionId);
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
            listener = (AddQuestionFragment.OnFragmentInteractionListener) context;
        } else {

            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This will create a new instance of this fragment with an experiment
     * @param question
     *   The question that will be edited
     * @param type
     *   A boolean for if it's a question (true) or reply (false) being created
     * @return
     *   a fragment to edit an questions's information
     */
    public static AddQuestionFragment newInstance(String question, Boolean type, UUID ownerId)
    {
        AddQuestionFragment questionFragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putString(QUESTION, question);
        if (type) {
            args.putString(TYPE, "Add Question");
        } else {
            args.putString(TYPE, "Add Reply");
        }
        args.putSerializable(OWNER, ownerId);
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
        AlertDialog.Builder build = new AlertDialog.Builder(getContext());

        // this will determine if it is asking for a question or reply
        String dialogType;
        // this is the experiment UUID that this question belongs to
        UUID ownerId = (UUID) args.getSerializable(OWNER);

        if (args != null) {
            dialogType = args.getString(TYPE);
            questionInput.setText(args.getString(QUESTION));
        } else {
            // assume if no bundle args that it's asking for a question
            dialogType = "Add Question";
        }

        // update the hint text of the question input depending on what the dialogType is
        if (dialogType == "Add Question") {
            questionInput.setHint("Question");
            return build.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentQuestion = questionInput.getText().toString();
                            listener.onOkPressedQuestion(currentQuestion, ownerId);
                        }
                    }).create();
        } else {
            questionInput.setHint("Reply");
            return build.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentReply = questionInput.getText().toString();
                            listener.onOkPressedReply(currentReply, ownerId);
                        }
                    }).create();
        }
    }
}
