import backend.Neuron;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeuronTest {
    Neuron neuron = new Neuron(1, x -> x, true);

    @Test
    @DisplayName("Create Neuron test")
    void createNeuronTest() throws IllegalAccessException {
        assertTrue(neuron.isLearningTime());
        assertDoesNotThrow(neuron::getWeights);
        for (double weight : neuron.getWeights()) {
            assertTrue(weight < 1.0);
            assertTrue(weight > -1.0);
        }
        assertTrue(neuron.getFreeExpression() <= 1.0);
        assertTrue(neuron.getFreeExpression() >= -1.0);
    }

    @Test
    @DisplayName("Working time blockade test")
    void workingTimeBlockadeTest() {
        neuron.switchToWorkingTime();

        assertThrows(IllegalAccessException.class, neuron::getWeights);
        assertThrows(IllegalAccessException.class, neuron::getFreeExpression);
        assertThrows(IllegalAccessException.class, () -> neuron.setWeights(new Double[]{1.0}));
        assertThrows(IllegalAccessException.class, () -> neuron.setWeight(0, 1.0));
        assertThrows(IllegalAccessException.class, () -> neuron.setFreeExpression(1.0));

        neuron.switchToLearningTime();

        assertDoesNotThrow(neuron::getWeights);
        assertDoesNotThrow(neuron::getFreeExpression);
        assertDoesNotThrow(() -> neuron.setWeights(new Double[]{1.0}));
        assertDoesNotThrow(() -> neuron.setWeight(0, 1.0));
        assertDoesNotThrow(() -> neuron.setFreeExpression(1.0));
    }

    @Test
    @DisplayName("Total Neuron Excitation calculation test")
    void totalNeuronExcitationCalculationTest() throws IllegalAccessException {
        Double weight = neuron.getWeights()[0];
        double freeExpression = neuron.getFreeExpression();
        assertEquals(
                weight * 3.5 + freeExpression,
                neuron.getTotalNeuronExcitation(new double[]{3.5})
        );
    }
    
    @Test
    @DisplayName("Total Neuron Excitation bad number of inputs")
    void totalNeuronExcitationBadNumberOfInputs() {
        assertThrows(IllegalArgumentException.class, () -> neuron.getTotalNeuronExcitation(new double[]{3.5, 4.0}));
    }
}