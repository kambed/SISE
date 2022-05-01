package backend;


import backend.layer.Layer;

import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Teacher {
    private NeuralNetwork neuralNetwork;
    private double learingRate;
    private double momentumRate;
    double[] totalNeuronOutputErrorDerivativesOfOutputLayer;

    public Teacher(NeuralNetwork neuralNetwork, double learingRate, double momentumRate) {
        this.neuralNetwork = neuralNetwork;
        this.learingRate = learingRate;
        this.momentumRate = momentumRate;
    }

    public void changeWeightWithBackpropagation(int numOfEras, double[][] inputs, double[][] expectedOutputs) {
        this.neuralNetwork.switchToLearningTime();
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        for (int i = 0; i < numOfEras; i++) {
            for (int j = 0; j < inputs.length; j++) {
                try {
                    backpropagationAlgorithm(inputs[order.get(j)], expectedOutputs[order.get(j)]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        this.neuralNetwork.switchToWorkingTime();
    }

    private void backpropagationAlgorithm(double[] inputs, double[] expectedOutputs) throws IllegalAccessException {
        double[] finalOutputs = neuralNetwork.calculateOutput(inputs);
        double[] outputs = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() + 1];

        double totalError = calculateTotalError(outputs, expectedOutputs);

        double[] weightError = calculateErrorInOutputLayer(outputs, expectedOutputs);

        double[][] weightErrorHidden = calculateErrorInHiddenLayers(finalOutputs);

        editWeights(weightError, neuralNetwork.getOutputLayer());
        for (int i = 0; i < neuralNetwork.getNumberOfHiddenLayers(); i++) {
            editWeights(weightErrorHidden[i], neuralNetwork.getHiddenLayers()[neuralNetwork.getHiddenLayers().length - 1 - i]);
        }
    }

    private void editWeights(double[] weightsError, Layer layer) throws IllegalAccessException {
        for (int i = 0; i < layer.getNeurons().length; i++) {
            int numOfWeightInNeuron = layer.getNeuron(i).getWeights().length;
            for (int j = 0; j < numOfWeightInNeuron; j++) {
                layer.getNeuron(i).setWeight(j, layer.getNeuron(i).getWeights()[j] - ((weightsError[i * layer.getNeuron(i).getWeights().length + j] * learingRate) + (momentumRate * layer.getLastWeightChange()[j])));
                layer.setLastWeightChange(j, (weightsError[i * layer.getNeuron(i).getWeights().length + j] * learingRate) + (momentumRate * layer.getLastWeightChange()[j]));
            }
        }
    }

    private double calculateTotalError(double[] outputs, double[] expectedOutputs) {
        double totalError = 0;
        for (int i = 0; i < outputs.length; i++) {
            totalError += ((expectedOutputs[i] - outputs[i]) * (expectedOutputs[i] - outputs[i])) / 2;
        }
        return totalError;
    }

    private double[] calculateErrorInOutputLayer(double[] outputs, double[] expectedOutputs) throws IllegalAccessException {
        totalNeuronOutputErrorDerivativesOfOutputLayer = new double[neuralNetwork.getNumberOfOutputs()];
        //edit output layer weights
        int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()].length;
        int numOfConnectionsInLayer = numOfNeuronsInPreviousLayer * neuralNetwork.getNumberOfOutputs();
        double[] totalErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronOutputErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] weightError = new double[numOfConnectionsInLayer];
        for (int i = 0; i < numOfConnectionsInLayer; i++) {
            int outputNum = i / numOfNeuronsInPreviousLayer;
            totalErrorDerivatives[i] = -(expectedOutputs[outputNum] - outputs[outputNum]);
            neuronOutputErrorDerivatives[i] = outputs[outputNum] * (1 - outputs[outputNum]);
            neuronErrorDerivatives[i] = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()][i % numOfNeuronsInPreviousLayer];
            weightError[i] = totalErrorDerivatives[i] * neuronOutputErrorDerivatives[i] * neuronErrorDerivatives[i];
        }
        for (int i = 0; i < neuralNetwork.getNumberOfOutputs(); i++) {
            int outputNum = numOfNeuronsInPreviousLayer * i;
            totalNeuronOutputErrorDerivativesOfOutputLayer[i] = totalErrorDerivatives[outputNum] * neuronOutputErrorDerivatives[outputNum];
        }
        return weightError;
    }

    private double[][] calculateErrorInHiddenLayers(double[] finalOutputs) throws IllegalAccessException {
        double[][] weightErrorHidden = new double[neuralNetwork.getNumberOfHiddenLayers()][64];
        for (int hLayer = 0; hLayer < neuralNetwork.getNumberOfHiddenLayers(); hLayer++) {
            double[] outputs = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - hLayer];
            int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - (hLayer + 1)].length;
            int numOfConnectionsInLayer = numOfNeuronsInPreviousLayer * outputs.length;
            double[] totalErrorDerivatives = new double[numOfConnectionsInLayer];
            double[] neuronOutputErrorDerivatives = new double[numOfConnectionsInLayer];
            double[] neuronErrorDerivatives = new double[numOfConnectionsInLayer];
            for (int i = 0; i < numOfConnectionsInLayer; i++) {
                if (hLayer == 0) {
                    for (int j = 0; j < finalOutputs.length; j++) {
                        double weight = neuralNetwork.getOutputLayer().getNeuron(j).getWeights()[i / numOfNeuronsInPreviousLayer];
                        totalErrorDerivatives[i] += totalNeuronOutputErrorDerivativesOfOutputLayer[j] * weight;
                    }
                } else {
                    for (int j = 0; j < neuralNetwork.getHiddenLayers()[neuralNetwork.getHiddenLayers().length - hLayer].getNeurons().length; j++) {
                        totalErrorDerivatives[i] += totalNeuronOutputErrorDerivativesOfOutputLayer[j] *
                                neuralNetwork.getHiddenLayers()[neuralNetwork.getHiddenLayers().length - hLayer].getNeuron(j).getWeights()[i / neuralNetwork.getOutputLayer().getNeurons().length];
                    }
                }
                neuronOutputErrorDerivatives[i] = outputs[i / numOfNeuronsInPreviousLayer] *
                        (1 - outputs[i / numOfNeuronsInPreviousLayer]);
                neuronErrorDerivatives[i] = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - (hLayer + 1)][i % numOfNeuronsInPreviousLayer];
                weightErrorHidden[hLayer][i] = totalErrorDerivatives[i] * neuronOutputErrorDerivatives[i] * neuronErrorDerivatives[i];
            }
        }
        return weightErrorHidden;
    }

    public double calculateError(double[][] inputs, double[][] expectedOutputs) {
        double totalError = 0;
        double[][] outputs = new double[expectedOutputs.length][expectedOutputs[0].length];
        for (int i = 0; i < expectedOutputs.length; i++) {
            outputs[i] = neuralNetwork.calculateOutput(inputs[i]);
            for (int j = 0; j < outputs[i].length; j++) {
                totalError += ((expectedOutputs[i][j] - outputs[i][j]) * (expectedOutputs[i][j] - outputs[i][j])) / 2;
            }
        }
        return totalError / expectedOutputs.length;
    }
}
