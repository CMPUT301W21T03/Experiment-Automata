package com.example.experiment_automata.backend.questions;

import android.util.Log;

import com.example.experiment_automata.backend.DataBase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Class made to maintain questions and replies that the users will make.
 *      This class is the main Model for keeping the questions
 *      in their place while taking the work away from other classes.
 */
public class QuestionManager {
    private static  HashMap<UUID, ArrayList<Question>> questions;
    private static HashMap<UUID, Question> questionFromId;
    private static  HashMap<UUID, ArrayList<Reply>> replies;
    private static QuestionManager questionManager;
    private static HashMap<UUID,Boolean> repliesFromId;
    private static boolean TEST_MODE = false;

    /**
     * Initializes the question manager.
     */
    private QuestionManager() {
        questions = new HashMap<>();
        replies = new HashMap<>();
        repliesFromId = new HashMap<>();
        questionFromId = new HashMap<>();
        if (!TEST_MODE) {
            getQuestionsFromFirestore();
        }
    }

    public static QuestionManager getInstance() {
        if (questionManager == null)
            questionManager =  new QuestionManager();
        return questionManager;
    }

    public static void resetInstance() {
        questionManager = null;
    }

    /**
     * Adds the given question that the user class/caller gives to this class
     * @param experimentId
     *  id corresponding to the experiment
     * @param question
     *  question to add to the manager
     */
    public void addQuestion(UUID experimentId, Question question) {
        if (!questionFromId.containsKey(question.getQuestionId())) {
            ArrayList<Question> returnQuestions = new ArrayList<>();
            returnQuestions.add(question);

            if (questions.containsKey(experimentId))
                returnQuestions.addAll(Objects.requireNonNull(questions.get(experimentId)));

            questions.put(experimentId, returnQuestions);
            questionFromId.put(question.getQuestionId(), question);
        }
    }

    /**
     * Adds the given replies that the user class/caller gives to this class.
     * @param questionId
     *  id corresponding to the question
     * @param reply
     *  reply to add to the manager
     */
    public void addReply(UUID questionId, Reply reply) {
        if (!repliesFromId.containsKey(reply.getReplyId())) {
            ArrayList<Reply> reps = replies.get(questionId);
            ArrayList<Reply> allReplies = new ArrayList<>();
            allReplies.add(reply);
            if (reps != null)
                allReplies.addAll(reps);

            replies.put(questionId, allReplies);
            repliesFromId.put(reply.getReplyId(), true);
            Question questionToUpdate = getQuestion(questionId);
            questionToUpdate.setReply(reply.getReplyId());
            if (!TEST_MODE) {
                questionToUpdate.postQuestionToFirestore();
            }
        }
    }

    /**
     * Gets a question from the database with it's UUID
     * @param questionId
     *   the ID of the question you want
     * @return
     *   the question you asked for
     * @throws IllegalArgumentException thrown if the question id does not exist
     */
    public Question getQuestion(UUID questionId) throws IllegalArgumentException {
        if (!questionFromId.containsKey(questionId)) {
            throw new IllegalArgumentException();
        }
        return questionFromId.get(questionId);
    }

    /**
     * gets the number of questions for an experiment
     * @param experimentId
     *   the experiment you want to check
     * @return
     *  the number of questions that experiment has
     *  -1 If the experiment is not contained
     */
    public int getTotalQuestions(UUID experimentId) {
        int count;
        if (!questions.containsKey(experimentId))
            count = -1;
        else
            count = Objects.requireNonNull(questions.get(experimentId)).size();

        return count;
    }


    /**
     * Get list of questions for a certain experiment.
     * @param experimentId
     *  The experiment id for the questions we want
     * @return
     *  A list of all the experiments owned by the experiment specified
     * @throws IllegalArgumentException
     *  the that experiment does not exist
     */
    public ArrayList<Question> getExperimentQuestions(UUID experimentId) throws IllegalArgumentException {
        if (!questions.containsKey(experimentId)) {
            throw new IllegalArgumentException();
        }
        ArrayList<Question> sortedQuestions = new ArrayList<>(Objects.requireNonNull(questions.get(experimentId)));
        Collections.sort(sortedQuestions);
        return sortedQuestions;
    }

    /**
     * Get a reply for a question if it exists
     * @param questionId
     *  The question id for the reply we want
     * @return
     *  A list of all replies given to that question
     */
    public ArrayList<Reply> getQuestionReply(UUID questionId) {
        if (!replies.containsKey(questionId)) {
            return new ArrayList<>();
        }

        ArrayList<Reply> sortedReplies = new ArrayList<>(Objects.requireNonNull(replies.get(questionId)));
        Collections.sort(sortedReplies);
        return sortedReplies;
    }

    /**
     * Gets all questions without keys
     * @return
     *  return all questions
     */
    public Collection<ArrayList<Question>> getAllQuestions() {
        return questions.values();
    }

    /**
     * Gets all questions from firestore
     */
    public void getQuestionsFromFirestore() {

        DataBase dataBase = DataBase.getInstance();
        FirebaseFirestore db = dataBase.getFireStore();
        CollectionReference questionsCollection = db.collection("questions");
        questionsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document :  task.getResult()) {
                    UUID experimentId =  UUID.fromString((String) document.get("experiment-id"));
                            Question currentQuestion = new Question(
                            (String) document.get("question-text"),
                            UUID.fromString((String) document.get("user-id")),
                            experimentId,
                            UUID.fromString(document.getId())
                    );
                    addQuestion(experimentId, currentQuestion);
                }
                getRepliesFromFirestore();
            }
        });
    }

    /**
     * Gets all replies from firestore
     */
    private void getRepliesFromFirestore() {
        if (TEST_MODE) return;
        DataBase dataBase = DataBase.getInstance();
        FirebaseFirestore db = dataBase.getFireStore();
        CollectionReference questionsCollection = db.collection("replies");
        questionsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document :  task.getResult()) {
                    UUID questionId = UUID.fromString((String) document.get("question-id"));
                    Reply currentReply = new Reply(
                            (String) document.get("reply-text"),
                            questionId,
                            UUID.fromString((String) document.get("user-id")),
                            UUID.fromString(document.getId())
                    );
                    addReply(questionId, currentReply);
                }
            }
        });
    }

    /**
     * enables test mode (Does not talk to firebase)
     */
    public static void enableTestMode() {
        TEST_MODE = true;
    }

    /**
     * disables test mode (Talks to firebase)
     */
    public static void disableTestMode() {
        TEST_MODE = false;
    }
}
