package backend;

import java.io.Serializable;
import java.util.Random;
import java.util.function.Function;

public class Neuron implements Serializable {
    private final int numberOfInputs;
    private Double[] weights;
    private double freeExpression;
    private final SerializableFunction activationFunction;
    private boolean learningTime = true;

    public Neuron(int numberOfInputs, SerializableFunction activationFunction) {
        this.numberOfInputs = numberOfInputs;
        this.weights = new Double[numberOfInputs];
        this.activationFunction = activationFunction;
        for (int i = 0; i < weights.length; i++) {
            do {
                weights[i] = (new Random()).nextDouble(-1.0, 1.0);
            } while (weights[i] == 0);
        }
        do {
            freeExpression = (new Random()).nextDouble(-1.0, 1.0);
        } while (freeExpression == 0);
    }

    public Double[] getWeights() throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        return weights;
    }

    public void setWeights(Double[] weights) throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        this.weights = weights;
    }

    public void setWeight(int index, Double weight) throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        weights[index] = weight;
    }

    public double getFreeExpression() throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        return freeExpression;
    }

    public void setFreeExpression(double freeExpression) throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        this.freeExpression = freeExpression;
    }

    public Double getTotalNeuronExcitation(double[] input) {
        if (input.length != numberOfInputs) {
            throw new IllegalArgumentException(
                    "This neuron should get " + numberOfInputs + "inputs, " + input.length + " given."
            );
        }

        double totalNeuronExcitation = 0;

        for (int i = 0; i < input.length; i++) {
            totalNeuronExcitation += input[i] * weights[i];
        }

        return activationFunction.apply(totalNeuronExcitation + freeExpression);
    }

    public boolean isLearningTime() {
        return learningTime;
    }

    public void switchToLearningTime() {
        this.learningTime = true;
    }

    public void switchToWorkingTime() {
        this.learningTime = false;
    }
}
