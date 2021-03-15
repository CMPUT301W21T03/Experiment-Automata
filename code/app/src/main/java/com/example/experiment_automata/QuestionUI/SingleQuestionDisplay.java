package com.example.experiment_automata.QuestionUI;

import android.app.Activity;
import android.content.Context;
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
    private TextView replyView;
    private NavigationActivity mainActivity;


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

        if(root == null)
        {
            root = LayoutInflater.from(context).inflate(R.layout.main_question_display, parent, false);
        }

        if(currentExperimentQuestions != null) {
            setView(root, position);
        }
        return root;
    }


    private void setView(View root, int position)
    {
        Question currentQuestion = currentExperimentQuestions.get(position);
        replyButton = root.findViewById(R.id.main_question_display_reply_button);
        replyView = root.findViewById(R.id.main_question_display_reply);
        replyButton.setOnClickListener(v -> dealingWithReply());
        ((TextView) (root.findViewById(R.id.main_question_display_question_view)))
                .setText(currentQuestion.getQuestion());

        setReplyView(currentQuestion);
    }

    private void setReplyView(Question currentQuestion)
    {
        try {
            Reply currentReply = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
            replyView.setText(currentReply.getReply());
        }
        catch (Exception e)
        {
            // TODO: deal with this in some fancy way
        }

    }

    private void dealingWithReply()
    {
        Log.d(CHECKING_REPLY_CLK, "Reply Button Pressed");
    }
}
