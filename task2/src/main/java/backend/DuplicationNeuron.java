package backend;

public class DuplicationNeuron extends Neuron{
    public DuplicationNeuron() {
        super(1, x -> x, false);
    }

    @Override
    public Double getTotalNeuronExcitation(double[] input) {
        double totalNeuronExcitation = 0;

        for (double v : input) {
            totalNeuronExcitation += v;
        }

        return totalNeuronExcitation;
    }
}
