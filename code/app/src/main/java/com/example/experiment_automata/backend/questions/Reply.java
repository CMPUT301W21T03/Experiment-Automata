package com.example.experiment_automata.backend.questions;

import androidx.annotation.NonNull;

import com.example.experiment_automata.backend.DataBaseConfiguration.DataBase;
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
     *  Post the current question to firestore
     */

    private void postReplyToFirestore() {
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
        return reply.toLowerCase().compareTo(((Reply)o).reply);
    }
}
