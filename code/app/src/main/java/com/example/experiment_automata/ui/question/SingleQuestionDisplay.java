package com.example.experiment_automata.ui.question;

import android.annotation.SuppressLint;
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
import com.example.experiment_automata.backend.users.UserManager;
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
 *  Citation:
 *      1. Inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List", 2021-02-04, Public Domain,
 *      https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
 *
 */
public class SingleQuestionDisplay extends ArrayAdapter<Question> {
    private final ArrayList<Question> currentExperimentQuestions;
    private final Context context;
    private ImageButton replyButton;
    private TextView questionView;
    private LinkView questionUser;
    private final NavigationActivity mainActivity;
    private Question currentQuestion;
    private ReplyArrayAdapter replyArrayAdapter;
    private final UserManager userManager;

    /**
     * Constructor takes in an array list of questions and a context to set the attributes properly
     * @param context
     *   Context needed to create the view if needed
     * @param currentExperimentQuestions
     *   All of the questions to be shown
     * @param mainActivity
     *   MainActivity to access the questionManager
     */
    public SingleQuestionDisplay(Context context, ArrayList<Question> currentExperimentQuestions, Activity mainActivity) {
        super(context, 0, currentExperimentQuestions);
        this.context = context;
        this.currentExperimentQuestions = currentExperimentQuestions;
        this.mainActivity = (NavigationActivity) mainActivity;
        this.userManager = UserManager.getInstance(true);
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
        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.main_question_display, parent, false);
            update();
        }
        questionView = root.findViewById(R.id.main_question_display_question_view);
        replyButton = root.findViewById(R.id.main_question_display_reply_button);
        questionUser = root.findViewById(R.id.main_question_display_user);
        ListView replyListView = root.findViewById(R.id.main_question_display_list_view);

        ArrayList<Reply> currentReplies = mainActivity.questionManager.getQuestionReply(currentQuestion.getQuestionId());
        replyArrayAdapter = new ReplyArrayAdapter(getContext(), currentReplies);
        replyListView.setAdapter(replyArrayAdapter);

        setView(position);
        return root;
    }

    /**
     * Sets up the display
     * @param pos
     *   which position within the array adapter
     */
    @SuppressLint("SetTextI18n")
    private void setView(int pos) {
        // try and find a reply for the current question
        replyButton.setOnClickListener(v -> dealingWithReply(pos));
        questionView.setText(currentQuestion.getQuestion());
        User user = userManager.getSpecificUser(currentQuestion.getUser());
        questionUser.setOnClickListener(v -> {
            NavigationActivity parentActivity = (NavigationActivity) context;
            Bundle args = new Bundle();
            args.putSerializable(ProfileFragment.userKey, user);
            NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_profile, args);
        });
        if (user.getInfo() != null)
            questionUser.setText(user.getInfo().getName());
        else
            questionUser.setText("BAD-DATA");
        update();
    }

    /**
     * Updates the views as they are needed so as not clutter the main function and
     * to not violet DRY.
     *
     */
    private void update() {
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
