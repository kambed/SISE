import backend.NeuralNetwork;
import backend.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {
    @Test
    @DisplayName("Teacher test")
    void teacherTest() throws IllegalAccessException {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 2);
        nn.getOutputLayer().getNeurons()[0].setWeights(new Double[]{0.40, 0.45});
        nn.getOutputLayer().getNeurons()[1].setWeights(new Double[]{0.50, 0.55});

        nn.getOutputLayer().getNeurons()[0].setFreeExpression(0.6);
        nn.getOutputLayer().getNeurons()[1].setFreeExpression(0.6);

        nn.getHiddenLayers()[0].getNeurons()[0].setWeights(new Double[]{0.15, 0.20});
        nn.getHiddenLayers()[0].getNeurons()[1].setWeights(new Double[]{0.25, 0.30});

        nn.getHiddenLayers()[0].getNeurons()[0].setFreeExpression(0.35);
        nn.getHiddenLayers()[0].getNeurons()[1].setFreeExpression(0.35);

        double[] results = nn.calculateOutput(new double[]{0.05, 0.1});
        assertEquals(0.75, results[0], 0.01);
        assertEquals(0.77, results[1], 0.01);

        Teacher t = new Teacher(nn, 0.9, 0.6);
        t.changeWeightWithBackpropagation(1, new double[][]{{0.05, 0.1}}, new double[][]{{0.01, 0.99}});

        results = nn.calculateOutput(new double[]{0.05, 0.1});
        assertEquals(0.73, results[0], 0.01);
        assertEquals(0.76, results[1], 0.01);
    }
}