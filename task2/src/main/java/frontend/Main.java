package frontend;

import backend.NeuralNetwork;
import backend.Teacher;
import backend.dao.FileNeuralNetworkDao;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Wybierz tryb pracy: Uczenie/Testowanie ");
        String mode = new Scanner(System.in).nextLine();
        NeuralNetwork nn = null;
        boolean learned = false;
        switch (mode) {
            case "Uczenie":
                System.out.println("TRYB UCZENIA");
                System.out.println("============");
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
                nn = new NeuralNetwork(numOfInputs, numOfOutputs, numOfHiddenLayers, numOfNeuronsInHiddenLayers, withBais);

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
                learned = true;

                System.out.print("Czy chcesz zapisać sieć: ");
                if (new Scanner(System.in).nextBoolean()) {
                    System.out.print("Podaj ścieżkę do zapisu: ");
                    FileNeuralNetworkDao fnnd = new FileNeuralNetworkDao(Paths.get(new Scanner(System.in).nextLine()).toAbsolutePath().toString());
                    try {
                        fnnd.write(nn);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case "Testowanie":
                System.out.println("TRYB TESTOWANIA");
                System.out.println("===============");
                if (!learned) {
                    System.out.print("Podaj ścieżkę pliku z siecią: ");
                    FileNeuralNetworkDao fnnd = new FileNeuralNetworkDao(Paths.get(new Scanner(System.in).nextLine()).toAbsolutePath().toString());
                    try {
                        nn = fnnd.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Arrays.toString(nn.calculateOutput(new double[]{1, 0, 0, 0})));
                System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 1, 0, 0})));
                System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 0, 1, 0})));
                System.out.println(Arrays.toString(nn.calculateOutput(new double[]{0, 0, 0, 1})));
                break;
            default:
                System.out.println("Wybrano nie prawidłową opcje");
                return;
        }


        




    }
}
