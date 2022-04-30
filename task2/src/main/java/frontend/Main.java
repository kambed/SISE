package frontend;

import backend.NeuralNetwork;
import backend.Teacher;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        System.out.print("Podaj liczbe wejść: ");
        int numOfInputs = new Scanner(System.in).nextInt();
        System.out.print("Podaj liczbe wyjść: ");
        int numOfOutputs = new Scanner(System.in).nextInt();
        System.out.print("Podaj liczbe warstw ukrytych: ");
        int numOfHiddenLayers = new Scanner(System.in).nextInt();
        System.out.print("Podaj liczbe neuronów w warswie ukrytej: ");
        int numOfNeuronsInHiddenLayers = new Scanner(System.in).nextInt();
        System.out.print("Czy uwzgledniać wejście obciążające (bais): ");
        boolean withBais = new Scanner(System.in).nextBoolean();
        NeuralNetwork nn = new NeuralNetwork(numOfInputs, numOfOutputs, numOfHiddenLayers, numOfNeuronsInHiddenLayers, withBais);

        System.out.print("Podaj wspolczynnik nauki: ");
        double learningRate = new Scanner(System.in).nextDouble();
        System.out.print("Podaj wspolczynnik momentum: ");
        double momentumRate = new Scanner(System.in).nextDouble();
        Teacher t = new Teacher(nn, learningRate, momentumRate);

        System.out.print("Podaj ilosc er: ");
        int eras = new Scanner(System.in).nextInt();
        
        double[][] data = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        double[][] outputs = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        t.changeWeightWithBackpropagation(eras, data, outputs);
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{1, 0, 0, 0})));
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 1, 0, 0})));
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 0, 1, 0})));
        System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 0, 0, 1})));
    }
}
