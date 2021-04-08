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
 *       The goal of this class is to maintain and hold the question data.
 *
 * Known Issue:
 *
 *      1. None
 */
public class Question implements Serializable, Comparable
{
    private String question;
    private UUID user;
    private UUID  reply;
    private UUID experimentId;
    private UUID questionId;
    private boolean testMode;

    /**
     * Regular questions constructor
     * @param question the asked user question
     * @param user the user id
     * @param experimentId the experiment id
     */
    public Question(String question, UUID user, UUID experimentId)
    {
        this.question = question;
        this.user = user;
        this.experimentId = experimentId;
        this.questionId = UUID.randomUUID();
        this.testMode = false;
        postQuestionToFirestore();
    }

    /**
     * Test constructor so that the class does not talk to firebase
     * @param question the asked question
     * @param user the user id
     * @param experimentId the experiment id
     * @param testMode the mode of the class
     */
    public Question(String question, UUID user, UUID experimentId, boolean testMode)
    {
        this.question = question;
        this.user = user;
        this.experimentId = experimentId;
        this.questionId = UUID.randomUUID();
        this.testMode = true;
    }

    /**
     * Constructor for Question when all values are received from firestore.
     * Note that reply is not an argument because replies will be added by the
     * Question manager later.
     * @param questionText
     * @param userId
     * @param experimentId
     * @param questionId
     */
    public Question(String questionText, UUID userId, UUID experimentId, UUID questionId) {
        this.question = questionText;
        this.user = userId;
        this.experimentId = experimentId;
        this.questionId = questionId;
        this.testMode = false;
    }

    /**
     *  Post the current question to firestore
     */
    protected void postQuestionToFirestore() {

        if(testMode)
            return;

        DataBase dataBase = DataBase.getInstanceTesting();
        FirebaseFirestore db = dataBase.getFireStore();
        Map<String,Object> questionData = new HashMap<>();
        String questionIdString = this.questionId.toString();

        questionData.put("question-text",this.question);
        questionData.put("user-id", this.user.toString());
        questionData.put("experiment-id", this.experimentId.toString());
        if (this.reply != null) {
            questionData.put("reply-id", this.reply.toString());
        }

        try {
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
        }catch (Exception e)
        {}
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
        Question question = (Question) o;

        return question.getQuestion().compareTo(this.question);
    }
}