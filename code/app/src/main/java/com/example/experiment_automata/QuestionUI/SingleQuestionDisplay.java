package com.example.experiment_automata.QuestionUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class SingleQuestionDisplay extends ArrayAdapter<Question>
{
    public static final String CHECKING_REPLY_CLK = "TEST_REP_B";
    private ArrayList<Question> currentExperimentQuestions;
    private Context context;
    private ImageButton replyButton;
    private TextView questionView;
    private TextView replyView;
    private NavigationActivity mainActivity;
    private Question currentQuestion;


    public SingleQuestionDisplay(Context context, ArrayList currentExperimentQuestions, Activity mainActivity)
    {
        super(context, 0, currentExperimentQuestions);
        this.context = context;
        this.currentExperimentQuestions = currentExperimentQuestions;
        this.mainActivity = (NavigationActivity) mainActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        currentQuestion = currentExperimentQuestions.get(position);
        if(root == null)
        {
            root = LayoutInflater.from(context).inflate(R.layout.main_question_display, parent, false);
        }
        questionView = root.findViewById(R.id.main_question_display_question_view);
        replyButton = root.findViewById(R.id.main_question_display_reply_button);
        replyView = root.findViewById(R.id.main_question_display_reply);

        if(currentExperimentQuestions != null) {
            setView(root, position);
        }
        return root;
    }


    private void setView(View root, int pos)
    {
        // try and find a reply for the current question
        Reply currentReply;
        try {
            currentReply = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
            Log.d("replyID", currentQuestion.getQuestion() + " " + currentReply.getReplyId().toString());
            replyButton.setVisibility(View.GONE);
            replyView.setText(currentReply.getReply());
        } catch (IllegalArgumentException e) {
            replyButton.setOnClickListener(v -> dealingWithReply(pos));
        }
        ((TextView) (root.findViewById(R.id.main_question_display_question_view)))
                .setText(currentQuestion.getQuestion());

    }

    /**
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     *
     * @param questionId : The unique id that each question contains
     */
    private void update(UUID questionId) {
        Log.d("UPDATE", "Screen info updated");
        currentQuestion = mainActivity.questionManager.getQuestion(questionId);
        questionView.setText(currentQuestion.getQuestion());
        Reply currentReply;
        try {
            currentReply = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
            Log.d("replyID", currentQuestion.getQuestion() + " " + currentReply.getReplyId().toString());
            replyButton.setVisibility(View.GONE);
            replyView.setText(currentReply.getReply());
        } catch (IllegalArgumentException e) {}
    }


    /**
     *
     * Caller function that sets up the views when an update to the data happens.
     *
     */
    public void updateScreen() {
        update(currentQuestion.getQuestionId());
    }


    private void dealingWithReply(int position)
    {
        Fragment replyFragment = AddQuestionFragment.newInstance("", false,
                currentExperimentQuestions.get(position).getQuestionId());
        mainActivity.getSupportFragmentManager().beginTransaction().add(replyFragment, "Reply").commit();
    }
}
