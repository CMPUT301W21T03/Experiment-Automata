package com.example.experiment_automata.QuestionUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;
import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.QuestionsModel.Question;
import com.example.experiment_automata.QuestionsModel.Reply;
import com.example.experiment_automata.R;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Role/Pattern:
 *     Controls a single view for each of the questions that have been
 *     asked about the experiment.
 *
 *  Known Issue:
 *
 *      1. None
 *
 *  Citation:
 *
 *      1. Partly inspired by lab of 301 lab
 *
 */
public class SingleQuestionDisplay extends ArrayAdapter
{
    private ArrayList<Question> currentExperimentQuestions;
    private Context context;
    private ImageButton replyButton;
    private TextView questionView;
    private NavigationActivity mainActivity;
    private Question currentQuestion;
    private ListView replyListView;
    private ArrayAdapter<Reply> replyArrayAdapter;

    /**
     * Constructor takes in an array list of questions and a context to set the attributes properly
     * @param context
     *   Context needed to create the view if needed
     * @param currentExperimentQuestions
     *   All of the questions to be shown
     * @param mainActivity
     *   MainActivity to access the questionManager
     */
    public SingleQuestionDisplay(Context context, ArrayList currentExperimentQuestions, Activity mainActivity)
    {
        super(context, 0, currentExperimentQuestions);
        this.context = context;
        this.currentExperimentQuestions = currentExperimentQuestions;
        this.mainActivity = (NavigationActivity) mainActivity;

    }

    /**
     * This method prepares the view with the proper XML file
     * @param position
     *   Index of the question
     * @param convertView
     *   XML we are trying to connect
     * @param parent
     *   Paretn view of the current file
     * @return
     *   View with all the attributes set properly
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        currentQuestion = currentExperimentQuestions.get(position);
        if(root == null)
        {
            root = LayoutInflater.from(context).inflate(R.layout.main_question_display, parent, false);
            update(root);
        }
        questionView = root.findViewById(R.id.main_question_display_question_view);
        replyButton = root.findViewById(R.id.main_question_display_reply_button);

        if(currentExperimentQuestions != null) {
            setView(root, position);
        }
        return root;
    }

    /**
     * Sets up the display
     * @param root
     *   the view created for the fragment
     * @param pos
     *   which position within the array adapter
     */
    private void setView(View root, int pos)
    {
        // try and find a reply for the current question
        update(root);
        replyButton.setOnClickListener(v -> dealingWithReply(pos));
        ((TextView) (root.findViewById(R.id.main_question_display_question_view)))
                .setText(currentQuestion.getQuestion());

    }

    /**
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     */
    private void update(View root)
    {
        replyListView = root.findViewById(R.id.main_question_display_list_view);
        ArrayList<Reply> currentReplies = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
        replyArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.reply_layout_list, currentReplies);
        replyListView.setAdapter(replyArrayAdapter);
        replyArrayAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    /**
     * Prepares the reply fragment when creating a reply
     * @param position
     *   Position of the question the reply is being made for
     */
    private void dealingWithReply(int position)
    {
        Fragment replyFragment = AddQuestionFragment.newInstance("", false,
                currentExperimentQuestions.get(position).getQuestionId());
        mainActivity.getSupportFragmentManager().beginTransaction().add(replyFragment, "Reply").commit();
        notifyDataSetChanged();
    }

}
