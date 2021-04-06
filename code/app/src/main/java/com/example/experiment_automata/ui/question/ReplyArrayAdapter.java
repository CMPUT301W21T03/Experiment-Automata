package com.example.experiment_automata.ui.question;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.questions.Reply;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.LinkView;
import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Role/Pattern:
 *
 * This maintains the list for the android system to display the individual inflated views.
 *
 * Known Issue:
 *
 *      1. None
 */
// Syntax inspired by Abdul Ali Bangash, "Lab 3 Instructions - Custom List",
// 2021-02-04, Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class ReplyArrayAdapter extends ArrayAdapter<Reply> {
    private ArrayList<Reply> replies;
    private Context context;

    /**
     * Constructor takes in an array list of replies and a context to set the attributes properly
     * @param context
     *  Context of the array adapter
     * @param replyList
     *  the list of the replies to display
     */
    public ReplyArrayAdapter(Context context, ArrayList<Reply> replyList){
        super(context, 0, replyList);
        this.replies = replyList;
        this.context = context;
    }

    /**
     * This method sets the contents of the experiment to match up with the correct XML file
     * @param position Index of the experiment
     * @param convertView The XML we are trying to connect it with
     * @param parent Parent view of the current file
     * @return View with all the attributes set properly
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.reply_layout_list, parent, false);
        }

        Reply reply = replies.get(position);
        User user = User.getInstance(reply.getUser());

        TextView replyTextView = (TextView) view.findViewById(R.id.reply_text);
        LinkView replyUserView = (LinkView) view.findViewById(R.id.reply_user);

        replyTextView.setText(reply.getReply());
        if(user.getInfo() != null) {
            replyUserView.setText(user.getInfo().getName());
            replyUserView.setOnClickListener(v -> {
                NavigationActivity parentActivity = (NavigationActivity) context;
                Bundle args = new Bundle();
                args.putSerializable(ProfileFragment.userKey, user);
                NavController navController = Navigation.findNavController(parentActivity, R.id.nav_host_fragment);
                navController.navigate(R.id.nav_profile, args);
            });
        }
        else
            replyUserView.setText("BAD=DATA");

        return view;
    }
}
