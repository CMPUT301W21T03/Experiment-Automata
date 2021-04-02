package com.example.experiment_automata.backend.questions;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Role/Pattern:
 *
 *       The goal of this class is to maintain a questions replies
 *
 * Known Issue:
 *
 *      1. None
 */
public class Reply implements Serializable {
    private String reply;
    private UUID experimenter;
    private UUID replyId;

    public Reply(String reply, UUID experimenter) {
        this.reply = reply;
        this.experimenter = experimenter;
        this.replyId = UUID.randomUUID();
        postReplyToFirestore();
    }

    /**
     *  Post the current question to firestore
     */

    private void postReplyToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> questionData = new HashMap<>();
        String replyIdString = this.replyId.toString();

        questionData.put("reply-text",this.reply);
        questionData.put("user-id", this.experimenter.toString());

        db.collection("replies").document(replyIdString)
                .set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public String getReply() {
        return reply;
    }

    public UUID getReplyId() {
        return replyId;
    }

    public UUID getUser() {
        return experimenter;
    }

    /**
     * giving us more control as to how the replies are displayed
     * @return
     *  text of the reply
     */
    @Override
    public String toString() {
        return reply;
    }
}
