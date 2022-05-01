import backend.NeuralNetwork;
import backend.dao.FileNeuralNetworkDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNeuralNetworkDaoTest {
    NeuralNetwork nn = new NeuralNetwork(4, 3, 2, true);

    @Test
    @DisplayName("NeuralNetwork serialize test")
    void neuralNetworkSerializeTest() {
        try (FileNeuralNetworkDao fnnd = new FileNeuralNetworkDao("testSave")) {
            fnnd.write(nn);
            NeuralNetwork nnSer = fnnd.read();
            assertNotSame(nn, nnSer);
            assertEquals(nn.isLearningTime(), nnSer.isLearningTime());
            double[] inputs = new double[]{1, 2, 3, 4};
            assertEquals(nn.calculateOutput(inputs)[0], nnSer.calculateOutput(inputs)[0]);
            assertEquals(nn.calculateOutput(inputs)[1], nnSer.calculateOutput(inputs)[1]);
            assertEquals(nn.calculateOutput(inputs)[2], nnSer.calculateOutput(inputs)[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}