package frontend;

import backend.NeuralNetwork;
import backend.Teacher;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        NeuralNetwork nn = new NeuralNetwork(4, 3, 2);
        Teacher t = new Teacher(nn, 0.9, 0.6);

        double[][] data = new double[][]{
                {2, 2, 2, 2},
                {0, 0, 0, 0},
                {4, 4, 4, 4}
        };
        double[][] outputs = new double[][]{
                {0, 0, 1},
                {0, 1, 0},
                {1, 0, 0}
        };

        t.changeWeightWithBackpropagation(10000, data, outputs);
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 0, 0, 0})));
    }
}
