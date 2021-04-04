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
public class Question implements Serializable, Comparable
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
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