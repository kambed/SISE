import backend.Neuron;
import backend.layer.SigmoidalNeuronLayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SigmoidalNeuronLayerTest {
    SigmoidalNeuronLayer sigmoidalLayer = new SigmoidalNeuronLayer(2, 2, true);
    @Test
    @DisplayName("Create sigmoidal neuron layer test")
    void createSigmoidalNeuronLayerTest() {
        assertEquals(2, sigmoidalLayer.getNeurons().length);
    }

    @Test
    @DisplayName("Get output test")
    void getOutputTest() {
        double[] input = new double[]{1,2};
        double[] expected = calculateOutput(sigmoidalLayer.getNeurons(), input);
        double[] received = sigmoidalLayer.getOutputArray(input);
        assertEquals(expected.length, received.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], received[i]);
        }
    }
    public double[] calculateOutput(Neuron[] neurons, double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = neurons[i].getTotalNeuronExcitation(input);
        }
        return output;
    }
}
