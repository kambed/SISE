package neutron;

import java.util.Random;
import java.util.function.Function;

abstract class Neutron {
    protected final int numberOfInputs;
    protected double[] weights;
    protected final Function<Double, Double> activationFunction;

    public Neutron(int numberOfInputs, Function<Double, Double> activationFunction) {
        this.numberOfInputs = numberOfInputs;
        this.weights = new double[numberOfInputs];
        this.activationFunction = activationFunction;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (new Random()).nextDouble(-1.0, 1.0);
        }
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public void setWeight(int index, double weight) {
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
}
