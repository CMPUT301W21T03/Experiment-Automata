package com.example.experiment_automata;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    private Experiment experiment=new Experiment("This is a test");
    private Experiment experiment2=new Experiment("Testing 2");
    private User=new User();

    @Test
    void testConstructor(){
        private Subscription subscription=new Subscription(User, experiment2);
        private Collection<Experiment> answer=subscription.getSubscriptions();
        assertEquals(1, answer.size());
    }
}