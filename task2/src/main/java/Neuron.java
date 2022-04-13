import java.util.Random;
import java.util.function.Function;

public class Neuron {
    private final int numberOfInputs;
    private double[] weights;
    private final Function<Double, Double> activationFunction;
    private boolean learningTime = true;

    public Neuron(int numberOfInputs, Function<Double, Double> activationFunction) {
        this.numberOfInputs = numberOfInputs;
        this.weights = new double[numberOfInputs];
        this.activationFunction = activationFunction;
        for (int i = 0; i < weights.length; i++) {
            do {
                weights[i] = (new Random()).nextDouble(-1.0, 1.0);
            } while (weights[i] == 0);
        }
    }

    public double[] getWeights() throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        return weights;
    }

    public void setWeights(double[] weights) throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        this.weights = weights;
    }

    public void setWeight(int index, double weight) throws IllegalAccessException {
        if (!learningTime) {
            throw new IllegalAccessException("This neuron is in working time.");
        }
        weights[index] = weight;
    }

    public double getTotalNeuronExcitation(double[] input) {
        if (input.length != numberOfInputs) {
            throw new IllegalArgumentException(
                    "This neuron should get " + numberOfInputs + "inputs, " + input.length + " given."
            );
        }

        double totalNeuronExcitation = 0;

        for (int i = 0; i < input.length; i++) {
            totalNeuronExcitation += input[i] * weights[i];
        }

        return activationFunction.apply(totalNeuronExcitation);
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
