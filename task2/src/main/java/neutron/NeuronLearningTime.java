package neutron;
import java.util.function.Function;

public class NeuronLearningTime extends Neutron {
    public NeuronLearningTime(int numberOfInputs, Function<Double, Double> activationFunction) {
        super(numberOfInputs, activationFunction);
    }
    public NeuronLearningTime(NeutronWorkingTime neutron) {
        super(neutron.numberOfInputs, neutron.activationFunction);
        weights = neutron.weights;
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
}
