package backend.layer;

import backend.Neuron;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

abstract class Layer {
    protected int numberOfInputs;
    protected Neuron[] neurons;

    public Layer(int numberOfInputs, int numberOfNeurons) {
        this.numberOfInputs = numberOfInputs;
        neurons = new Neuron[numberOfNeurons];
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

    public abstract double[] getOutputArray(double[] input);
}
