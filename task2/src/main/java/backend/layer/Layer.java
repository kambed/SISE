package backend.layer;

import backend.Neuron;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

abstract public class Layer implements Serializable {
    protected int numberOfInputs;
    protected Neuron[] neurons;
    protected double[] lastWeightChange;

    public Layer(int numberOfInputs, int numberOfNeurons) {
        this.numberOfInputs = numberOfInputs;
        neurons = new Neuron[numberOfNeurons];
        lastWeightChange = new double[numberOfNeurons * numberOfInputs];
    }

    public double[] getLastWeightChange() {
        return lastWeightChange;
    }

    public void setLastWeightChange(int index, double lastWeightChange) {
        this.lastWeightChange[index] = lastWeightChange;
    }

    public void switchToLearningTime() {
        for (Neuron neuron : neurons) {
            neuron.switchToLearningTime();
        }
    }

    public void switchToWorkingTime() {
        for (Neuron neuron : neurons) {
            neuron.switchToWorkingTime();
        }
    }

    public Double[] getNeuronsWeights() throws IllegalAccessException {
        Double[] result = neurons[0].getWeights();
        for (int i = 1; i < neurons.length; i++) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(neurons[i].getWeights()))
                    .toArray(Double[]::new);
        }
        return result;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public Neuron getNeuron(int i) {
        return neurons[i];
    }

    public double getNeuronWeight(int neuronIndex, int weightIndex) throws IllegalAccessException {
        return neurons[neuronIndex].getWeights()[weightIndex];
    }

    public void updateNeuronWeight(int neuronIndex, int weightIndex, double correction) throws IllegalAccessException {
        neurons[neuronIndex].setWeight(weightIndex, getNeuronWeight(neuronIndex, weightIndex) + correction);
    }

    public abstract double[] getOutputArray(double[] input);
}
