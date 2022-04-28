import backend.Neuron;
import backend.layer.DuplicationNeuronLayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicationLayerTest {
    DuplicationNeuronLayer duplicationLayer = new DuplicationNeuronLayer(2);
    @Test
    @DisplayName("Create duplication Layer test")
    void createDuplicationLayerTest() {
        assertEquals(2, duplicationLayer.getNeurons().length);
    }
    @Test
    @DisplayName("Switching neuron tribes test")
    void switchingNeuronTribesTest() {
        assertTrue(duplicationLayer.getNeuron(0).isLearningTime());
        assertTrue(duplicationLayer.getNeuron(1).isLearningTime());
        duplicationLayer.switchToWorkingTime();
        assertFalse(duplicationLayer.getNeuron(0).isLearningTime());
        assertFalse(duplicationLayer.getNeuron(1).isLearningTime());
        duplicationLayer.switchToLearningTime();
        assertTrue(duplicationLayer.getNeuron(0).isLearningTime());
        assertTrue(duplicationLayer.getNeuron(1).isLearningTime());
    }

    @Test
    @DisplayName("Get Neurons Weights test")
    void getNeuronsWeightsTest() throws IllegalAccessException {
        assertDoesNotThrow(duplicationLayer::getNeuronsWeights);
        Double[] expectedWeights = getNeuronsWeights(duplicationLayer.getNeurons());
        Double[] receivedWeights = duplicationLayer.getNeuronsWeights();
        assertEquals(expectedWeights.length, receivedWeights.length);
        for (int i = 0; i < expectedWeights.length; i++) {
            assertEquals(expectedWeights[i], receivedWeights[i]);
        }
    }

    @Test
    @DisplayName("Get output test")
    void getOutputTest() {
        double[] input = new double[]{1, 2};
        double[] expected = calculateOutput(duplicationLayer.getNeurons(), input);
        double[] received = duplicationLayer.getOutputArray(input);
        assertEquals(expected.length, received.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], received[i]);
        }
    }

    public Double[] getNeuronsWeights(Neuron[] neurons) throws IllegalAccessException {
        Double[] result = neurons[0].getWeights();
        for (int i = 1; i < neurons.length; i++) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(neurons[i].getWeights()))
                    .toArray(Double[]::new);
        }
        return result;
    }

    public double[] calculateOutput(Neuron[] neurons, double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = neurons[i].getTotalNeuronExcitation(new double[]{input[i]});
        }
        return output;
    }
}
