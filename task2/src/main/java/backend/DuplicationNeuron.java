package backend;

public class DuplicationNeuron extends Neuron {

    public DuplicationNeuron() {
        super(1, x -> x, false);
    }

    @Override
    public Double getTotalNeuronExcitation(double[] input) {
        return input[0];
    }
}
