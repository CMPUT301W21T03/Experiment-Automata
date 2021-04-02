package com.example.experiment_automata.backend.questions;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Role/Pattern:
 *      Class made to maintain questions and replies that the users will make.
 *      This class is the main Model for keeping the questions
 *      in their place while taking the work away from other classes.
 *
 *  Known Issue:
 *
 *      1. Could be broken into something for question and something for reply
 *      2. An owner technically should not be creating questions, but we need this to test for now
 */
public class QuestionManager {
    private static  HashMap<UUID, ArrayList<Question>> questions;
    private static HashMap<UUID, Question> questionFromId;
    private static  HashMap<UUID, ArrayList<Reply>> replies;
    private static QuestionManager questionManager;


    /**
     * Initializes the question manager.
     */
    private QuestionManager()
    {
        questions = new HashMap<>();
        replies = new HashMap<>();
        questionFromId = new HashMap<>();
    }

    public static QuestionManager getInstance()
    {
        if (questionManager == null)
            questionManager =  new QuestionManager();

        return questionManager;
    }


    /**
     * Adds the given question that the user class/caller gives to this class
     * @param experimentId
     *  id corresponding to the experiment
     * @param question
     *  question to add to the manager
     */
    public void addQuestion(UUID experimentId, Question question)
    {
        ArrayList<Question> returnQuestions = new ArrayList<>();
        returnQuestions.add(question);

        if(questions.containsKey(experimentId))
            returnQuestions.addAll(questions.get(experimentId));

        questions.put(experimentId, returnQuestions);
        Log.d("question", "" + question.getQuestionId().toString());
        questionFromId.put(question.getQuestionId(), question);
    }

    /**
     * Adds the given replies that the user class/caller gives to this class.
     * @param id
     *  id corresponding to the question
     * @param reply
     *  reply to add to the manager
     */
    public void addReply(UUID questionId, Reply reply)
    {
        ArrayList<Reply> reps = replies.get(questionId);
        ArrayList<Reply> allReplies = new ArrayList<>();
        allReplies.add(reply);
        if(reps != null)
            allReplies.addAll(reps);

        replies.put(questionId, allReplies);
        Question questionToUpdate = getQuestion(questionId);
        questionToUpdate.setReply(reply.getReplyId());
        questionToUpdate.postQuestionToFirestore();
    }

    /**
     * Gets a question from the database with it's UUID
     * @param questionId
     *   the ID of the question you want
     * @return
     *   the question you asked for
     * @throws IllegalArgumentException
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
    public int getTotalQuestions(UUID experimentId)
    {
        int count;
        if (!questions.containsKey(experimentId))
            count = -1;
        else
            count = questions.get(experimentId).size();

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
        return questions.get(experimentId);
    }

    /**
     * Get a reply for a question if it exists
     * @param questionId
     *  The question id for the reply we want
     * @return
     *  A list of all replies given to that question
     */
    public ArrayList<Reply> getQuestionReply(UUID questionId)
    {
        if (!replies.containsKey(questionId)) {
            return new ArrayList<Reply>();
        }
        return replies.get(questionId);
    }

    /**
     * Gets all questions without keys
     * @return
     *  return all questions
     */
    public Collection<ArrayList<Question>> getAllQuestions()
    {
        return questions.values();
    }
}
