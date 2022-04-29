package frontend;

import backend.NeuralNetwork;
import backend.Teacher;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork(4, 3, 2);
        Teacher t = new Teacher(nn, 0.1);

        double[][] data = new double[][]{
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {0, 0, 0, 0}
        };
        double[][] outputs = new double[][]{
                {0, 0, 1},
                {1, 0, 0},
                {0, 1, 0}
        };
        t.changeWeightWithBackpropagation(1000, data, outputs);
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{1, 1, 1, 1})));
    }
}
