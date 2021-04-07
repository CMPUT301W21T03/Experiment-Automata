package com.example.experiment_automata.ui.question;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.LinkView;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.backend.questions.Question;
import com.example.experiment_automata.backend.questions.Reply;
import com.example.experiment_automata.R;
import com.example.experiment_automata.ui.profile.ProfileFragment;

import java.util.ArrayList;

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
public class SingleQuestionDisplay extends ArrayAdapter {
    private ArrayList<Question> currentExperimentQuestions;
    private Context context;
    private ImageButton replyButton;
    private TextView questionView;
    private LinkView questionUser;
    private NavigationActivity mainActivity;
    private Question currentQuestion;
    private ListView replyListView;
    private ReplyArrayAdapter replyArrayAdapter;

    /**
     * Constructor takes in an array list of questions and a context to set the attributes properly
     * @param context
     *   Context needed to create the view if needed
     * @param currentExperimentQuestions
     *   All of the questions to be shown
     * @param mainActivity
     *   MainActivity to access the questionManager
     */
    public SingleQuestionDisplay(Context context, ArrayList currentExperimentQuestions, Activity mainActivity) {
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
        questionView = (TextView) root.findViewById(R.id.main_question_display_question_view);
        replyButton = root.findViewById(R.id.main_question_display_reply_button);
        questionUser = (LinkView) root.findViewById(R.id.main_question_display_user);
        replyListView = root.findViewById(R.id.main_question_display_list_view);

        ArrayList<Reply> currentReplies = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
        replyArrayAdapter = new ReplyArrayAdapter(getContext(), currentReplies);
        replyListView.setAdapter(replyArrayAdapter);

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
    private void setView(View root, int pos) {
        // try and find a reply for the current question
        replyButton.setOnClickListener(v -> dealingWithReply(pos));
        questionView.setText(currentQuestion.getQuestion());
        User user = User.getInstance(currentQuestion.getUser(), false);
        questionUser.setOnClickListener(v -> {
            NavigationActivity parentActivity = (NavigationActivity) context;
            Bundle args = new Bundle();
            args.putSerializable(ProfileFragment.userKey, user);
            NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_profile, args);
        });
        if(user.getInfo() != null)
            questionUser.setText(user.getInfo().getName());
        else
            questionUser.setText("BAD-DATA");
        update(root);
    }

    /**
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     *
     * @param root the current view that was made 
     */
    private void update(View root) {
        if (replyArrayAdapter != null) {
            replyArrayAdapter.notifyDataSetChanged();
        }
        notifyDataSetChanged();
    }

    /**
     * Prepares the reply fragment when creating a reply
     * @param position
     *   Position of the question the reply is being made for
     */
    private void dealingWithReply(int position) {
        Fragment replyFragment = AddQuestionFragment.newInstance("", false,
                currentExperimentQuestions.get(position).getQuestionId());
        mainActivity.getSupportFragmentManager().beginTransaction().add(replyFragment, "Reply").commit();
        notifyDataSetChanged();
    }

}
