package backend.layer;

import backend.Neuron;

public class SigmoidalNeuronLayer extends Layer {
    public SigmoidalNeuronLayer(int numberOfInputs, int numberOfNeurons) {
        super(numberOfInputs, numberOfNeurons);
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(numberOfInputs, x -> (1 / (1 + Math.exp(-x))) );
        }
    }

    public double[] getOutputArray(double[] input) {
        double[] output = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            output[i] = neurons[i].getTotalNeuronExcitation(input);
        }
        return output;
    }
}
