package com.example.experiment_automata.QuestionsModel;


import com.example.experiment_automata.Experiments.ExperimentModel.Experiment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private static HashMap<UUID, ArrayList<Question>> questions;
    private static HashMap<UUID, Reply> replies;

    /**
     * Initializes the question manager.
     */
    public QuestionManager()
    {
        // the uuid used here should be an experiment's uuid
        questions = new HashMap<UUID, ArrayList<Question>>();
        // the uuid a reply uses here is the question's uuid
        replies = new HashMap<UUID, Reply>();
    }

    /**
     * Adds the given question that the user class/caller gives to this class
     * @param experimentId
     *  id corresponding to the experiment
     * @param question
     *  question to add to the manager
     */
    public void addQuestion(UUID experimentId, Question question) {
        ArrayList<Question> returnQuestions;
        if(questions.containsKey(experimentId))
            returnQuestions = questions.get(experimentId);
        else {
            returnQuestions = new ArrayList<Question>();
        }
        returnQuestions.add(question);
        questions.put(experimentId, returnQuestions);
    }

    /**
     * Adds the given reply that the user class/caller gives to this class
     * @param id
     *  id corresponding to the question
     * @param reply
     *  reply to add to the manager
     * @throws IllegalArgumentException
     *  the id is already associated to an experiment
     */
    public void addReply(UUID id, Reply reply) throws IllegalArgumentException {
        if(replies.containsKey(id))
            throw new IllegalArgumentException();
        else {
            replies.put(id, reply);
        }
    }

    /**
     * gets the number of questions for an experiment
     * @param experimentId
     *   the experiment you want to check
     * @return
     *  the number of questions that experiment has
     * @throws IllegalArgumentException
     *  the that experiment does not exist
     */
    public int getTotalQuestions(UUID experimentId) throws IllegalArgumentException {
        if (!questions.containsKey(experimentId)) {
            throw new IllegalArgumentException();
        }
        return questions.get(experimentId).size();
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
     *  A reply if one exists
     */
    public Reply getQuestionReply(UUID questionId) {
        if (!replies.containsKey(questionId)) {
            throw new IllegalArgumentException();
        }
        return replies.get(questionId);
    }

}
