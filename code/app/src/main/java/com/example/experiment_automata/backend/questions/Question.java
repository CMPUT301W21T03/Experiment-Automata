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
 *       The goal of this class is to maintain and hold the question data.
 *
 * Known Issue:
 *
 *      1. None
 */
public class Question implements Serializable
{
    private String question;
    private UUID user; // this makes more sense to just store a user ID
    private UUID  reply;// we will likely want the reply UUID since we have to query and see if a question has a reply
    private UUID experimentId; // I think we need this so we know what to query with each experiment
    private UUID questionId;

    public Question(String question, UUID user, UUID experimentId)
    {
        this.question = question;
        this.user = user;
        this.experimentId = experimentId;
        this.questionId = UUID.randomUUID();
        postQuestionToFirestore();
    }

    public Question(Question question) {
        this.question = question.getQuestion();
        this.user = question.getUser();
        this.reply = question.getReply();
        this.experimentId = question.getExperimentId();
        this.questionId = question.getQuestionId();
    }

    /**
     *  Post the current question to firestore
     */

    protected void postQuestionToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> questionData = new HashMap<>();
        String questionIdString = this.questionId.toString();

        questionData.put("question-text",this.question);
        questionData.put("user-id", this.user.toString());
        questionData.put("experiment-id", this.experimentId.toString());
        if (this.reply != null) {
            questionData.put("reply-id", this.reply.toString());
        }

        db.collection("questions").document(questionIdString)
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

    public String getQuestion() {
        return question;
    }

    // can users edit a question?
    public void setQuestion(String question) {
        this.question = question;
    }

    public UUID getUser() {
        return user;
    }

    // will we want to set a new userID for questions?
    public void setUser(UUID user) {
        this.user = user;
    }

    public UUID getExperimentId() {return experimentId;}

    // todo: a question won't always have a reply so we have to account for that
    public UUID getReply() {
        return reply;
    }

    // todo: this should only set a reply if one does not currently exist, possibly return bool for pass/fail
    public void setReply(UUID reply) {
        this.reply = reply;
    }

    public UUID getQuestionId()
    {
        return this.questionId;
    }

}