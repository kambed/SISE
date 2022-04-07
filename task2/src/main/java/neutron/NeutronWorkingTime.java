package neutron;

public class NeutronWorkingTime extends Neutron {
    public NeutronWorkingTime(NeuronLearningTime neutron) {
        super(neutron.numberOfInputs, neutron.activationFunction);
        this.weights = neutron.weights;
    }
}
