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
public class Question implements Serializable, Comparable {
    private String question;
    private UUID user; // this makes more sense to just store a user ID
    private UUID reply;// we will likely want the reply UUID since we have to query and see if a question has a reply
    private UUID experimentId; // I think we need this so we know what to query with each experiment
    private UUID questionId;

    /**
     * Constructor used when creating experiment from dialog
     * @param question
     *   The question asked by a user
     * @param user
     *   The user who asked the question
     * @param experimentId
     *   The ID of the experiment associated with the question
     */
    public Question(String question, UUID user, UUID experimentId) {
        this.question = question;
        this.user = user;
        this.experimentId = experimentId;
        this.questionId = UUID.randomUUID();
        postQuestionToFirestore();
    }

    /**
     * Copy constructor for questions
     * @param question
     *   The question passed in from Firestore
     */
    public Question(Question question) {
        this.question = question.getQuestion();
        this.user = question.getUser();
        this.reply = question.getReply();
        this.experimentId = question.getExperimentId();
        this.questionId = question.getQuestionId();
    }

    /**
     * Constructor for Question when all values are received from Firestore.
     * Note that reply is not an argument because replies will be added by the
     * Question manager later.
     * @param questionText
     *   The string the question will hold
     * @param userId
     *   The UUID of the user
     * @param experimentId
     *   The UUID of the associated experiment
     * @param questionId
     *   The UUID of the question being created
     */
    public Question(String questionText, UUID userId, UUID experimentId, UUID questionId) {
        this.question = questionText;
        this.user = userId;
        this.experimentId = experimentId;
        this.questionId = questionId;
    }

    /**
     *  Post the current question to Firestore
     */
    protected void postQuestionToFirestore() {
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
        } catch (Exception e) {}
    }

    /**
     * Get the question
     * @return
     *   The question as a string
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the string of a question
     * @param question
     *   The new question it should hold
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Get the UUID of the owner
     * @return
     *   The UUID of the owner
     */
    public UUID getUser() {
        return user;
    }

    /**
     * Set the UUID of the owner
     * @param user
     *   The new owner's UUID
     */
    public void setUser(UUID user) {
        this.user = user;
    }

    /**
     * Get the experiment ID associated with the question
     * @return
     *   The UUID of the experiment associated with the question
     */
    public UUID getExperimentId() {return experimentId;}

    /**
     * Get the UUID of the reply associated with a question
     * @return
     *   The UUID of the associated reply
     */
    public UUID getReply() {
        return reply;
    }

    /**
     * Set the reply associated with a question
     * @param reply
     *   The UUID you want to add
     */
    public void setReply(UUID reply) {
        this.reply = reply;
    }

    /**
     * Get the UUID of the question
     * @return
     *   The UUID of the question
     */
    public UUID getQuestionId() {
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
