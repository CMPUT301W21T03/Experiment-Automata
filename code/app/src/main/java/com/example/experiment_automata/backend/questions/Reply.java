package com.example.experiment_automata.backend.questions;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.DataBase;
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
public class Reply implements Serializable, Comparable {
    private String reply;
    private UUID experimenter;
    private UUID replyId;
    private UUID questionId;
    private boolean testMode;

    /**
     * main constructor for reply class
     * @param reply the string reply
     * @param questionId the parent question id
     * @param experimenter the id of the experimenter
     */
    public Reply(String reply, UUID questionId, UUID experimenter) {
        this.reply = reply;
        this.experimenter = experimenter;
        this.replyId = UUID.randomUUID();
        this.questionId = questionId;
        postReplyToFirestore();
    }

    /**
     * Constructor for replies generated from firestore data
     * @param reply
     * @param questionId
     * @param experimenter
     * @param replyId
     */
    public Reply(String reply, UUID questionId, UUID experimenter, UUID replyId) {
        this.reply = reply;
        this.experimenter = experimenter;
        this.replyId = replyId;
        this.questionId = questionId;
    }

    /**
     * the test constructor for the given class
     * @param reply the string reply
     * @param questionId the parent question id
     * @param experimenter the experimenter id
     * @param testMode the boolean mode of the class
     */
    public Reply(String reply, UUID questionId, UUID experimenter, boolean testMode)
    {
        this.testMode = testMode;
        this.reply = reply;
        this.experimenter = experimenter;
        this.replyId = UUID.randomUUID();
        this.questionId = questionId;
    }

    /**
     *  Post the current question to firestore
     */

    private void postReplyToFirestore() {
        if(testMode)
            return;

        DataBase dataBase = DataBase.getInstanceTesting();
        FirebaseFirestore db = dataBase.getFireStore();
        Map<String,Object> questionData = new HashMap<>();
        String replyIdString = this.replyId.toString();

        questionData.put("reply-text",this.reply);
        questionData.put("user-id", this.experimenter.toString());
        questionData.put("question-id", this.questionId.toString());

        try {
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
        }catch (Exception e)
        {}
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        return reply.toLowerCase().compareTo(((Reply)o).reply);
    }
}
