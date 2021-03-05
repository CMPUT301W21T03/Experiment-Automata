package com.example.experiment_automata;

public class ExperimentMaker
{
    private String description;


    public Experiment madeExperiment(ExperimentType type, String description) throws IlleagalExperimentException {
        switch (type)
        {
            case NatrualCount:
                return new NatrualCountExperiment(description);
            case Binomial:
                return new BinnomialExperiment(description);
            case Count:
                return new CountExperiment(description);
            case Measurement:
                return new MeasurementExperiment(description);
            default:
                throw new IlleagalExperimentException("Bad Experiment Type: " + type.toString());
        }
    }
}
