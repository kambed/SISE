package frontend;

import backend.NeuralNetwork;
import backend.Teacher;

public class Main {
    public static void main(String[] args){
        NeuralNetwork nn = new NeuralNetwork(4,3,2);
        Teacher t = new Teacher(nn, 0.1);
        t.changeWeightWithBackpropagation(3000);
        System.out.print("Hello SISE");
    }
}
