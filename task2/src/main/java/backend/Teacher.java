package backend;

import backend.layer.Layer;

import java.lang.*;
import java.util.Arrays;

public class Teacher {
    private NeuralNetwork neuralNetwork;
    private double learingRate;

    public Teacher(NeuralNetwork neuralNetwork, double learingRate) {
        this.neuralNetwork = neuralNetwork;
        this.learingRate = learingRate;
    }

    public void changeWeightWithBackpropagation(int numOfEras) {
        this.neuralNetwork.switchToLearningTime();
        for (int i = 0; i < numOfEras; i++) {
            backpropagationAlgorithm();
        }
        this.neuralNetwork.switchToWorkingTime();
    }

    private void backpropagationAlgorithm() {
        double[] inputs = new double[]{1, 2, 3, 4};
        double[] expected_outputs = new double[]{0, 0, 1};
        double[] outputs = neuralNetwork.calculateOutput(inputs);
        double totalError = 0;
        for (int i = 0; i < outputs.length; i++) {
            totalError += ((expected_outputs[i] - outputs[i]) * (expected_outputs[i] - outputs[i])) / 2;
        }
        int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()].length;
        int numOfConnectionsInLayer = numOfNeuronsInPreviousLayer * neuralNetwork.getNumberOfOutputs();
        double[] totalErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronOutputErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] weightError = new double[numOfConnectionsInLayer];
        for (int i = 0; i < numOfConnectionsInLayer; i++) {
            totalErrorDerivatives[i] = -(expected_outputs[i / numOfNeuronsInPreviousLayer] - outputs[i / numOfNeuronsInPreviousLayer]);
            neuronOutputErrorDerivatives[i] = outputs[i / numOfNeuronsInPreviousLayer] * (1 - outputs[i / numOfNeuronsInPreviousLayer]);
            neuronErrorDerivatives[i] = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()][i % 2];
            weightError[i] = totalErrorDerivatives[i] * neuronOutputErrorDerivatives[i] * neuronErrorDerivatives[i];
        }
        try {
            editWeights(weightError, neuralNetwork.getOutputLayer());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(weightError));
    }

    private void editWeights(double[] weightsError, Layer layer) throws IllegalAccessException {
        for (int i = 0; i < layer.getNeurons().length; i++) {
            for (int j = 0; j < layer.getNeuron(i).getWeights().length; j++) {
                layer.getNeuron(i).setWeight(j, layer.getNeuron(i).getWeights()[j] - (weightsError[i * layer.getNeuron(i).getWeights().length + j] * learingRate));
            }
        }
    }
}
