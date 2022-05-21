package backend.layer;

import backend.DuplicationNeuron;

public class DuplicationNeuronLayer extends Layer {
    public DuplicationNeuronLayer(int numberOfInputs) {
        super(numberOfInputs, numberOfInputs);
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new DuplicationNeuron();
        }
    }

    public double[] getOutputArray(double[] input) {
        double[] output = new double[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            output[i] = neurons[i].getTotalNeuronExcitation(new double[]{input[i]});
        }
        return output;
    }
}
