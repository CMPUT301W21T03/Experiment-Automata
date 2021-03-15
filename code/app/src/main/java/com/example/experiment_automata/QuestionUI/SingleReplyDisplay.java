package com.example.experiment_automata.QuestionUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.experiment_automata.QuestionsModel.Reply;
import com.example.experiment_automata.R;

import java.util.ArrayList;

public class SingleReplyDisplay extends ArrayAdapter
{
    private Context context;
    private  ArrayList<Reply> replies;

    public SingleReplyDisplay(Context context, ArrayList<Reply> replies)
    {
        super(context, 0, replies);
        this.context = context;
        this.replies = replies;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View root = convertView;

        if(root == null)
        {
            root = LayoutInflater.from(context).inflate(R.layout.main_replies_view, parent, false);
        }

        Reply currentReply = replies.get(position);
        ((TextView)root.findViewById(R.id.main_replaies_view_reply)).setText(currentReply.getReply());

        return root;
    }
}
