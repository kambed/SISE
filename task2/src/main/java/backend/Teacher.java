package backend;

import backend.layer.Layer;

import java.lang.*;

public class Teacher {
    private NeuralNetwork neuralNetwork;
    private double learingRate;

    public Teacher(NeuralNetwork neuralNetwork, double learingRate) {
        this.neuralNetwork = neuralNetwork;
        this.learingRate = learingRate;
    }

    public void changeWeightWithBackpropagation(int numOfEras, double[][] inputs, double[][] expected_outputs) {
        this.neuralNetwork.switchToLearningTime();
        for (int i = 0; i < numOfEras; i++) {
            for (int j = 0; j < inputs.length; j++) {
                try {
                    backpropagationAlgorithm(inputs[j], expected_outputs[j]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        this.neuralNetwork.switchToWorkingTime();
    }

    private void backpropagationAlgorithm(double[] inputs, double[] expected_outputs) throws IllegalAccessException {
        double[] finalOutputs = neuralNetwork.calculateOutput(inputs);
        double[] outputs = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() + 1];

        double totalError = 0;
        for (int i = 0; i < outputs.length; i++) {
            totalError += ((expected_outputs[i] - outputs[i]) * (expected_outputs[i] - outputs[i])) / 2;
        }

        double[] totalNeuronOutputErrorDerivativesOfOutputLayer = new double[neuralNetwork.getNumberOfOutputs()];
        //edit output layer weights
        int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()].length;
        int numOfConnectionsInLayer = numOfNeuronsInPreviousLayer * neuralNetwork.getNumberOfOutputs();
        double[] totalErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronOutputErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] neuronErrorDerivatives = new double[numOfConnectionsInLayer];
        double[] weightError = new double[numOfConnectionsInLayer];
        for (int i = 0; i < numOfConnectionsInLayer; i++) {
            totalErrorDerivatives[i] = -(expected_outputs[i / numOfNeuronsInPreviousLayer] - outputs[i / numOfNeuronsInPreviousLayer]);
            neuronOutputErrorDerivatives[i] = outputs[i / numOfNeuronsInPreviousLayer] * (1 - outputs[i / numOfNeuronsInPreviousLayer]);
            neuronErrorDerivatives[i] = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers()][i % numOfNeuronsInPreviousLayer];
            weightError[i] = totalErrorDerivatives[i] * neuronOutputErrorDerivatives[i] * neuronErrorDerivatives[i];
        }
        for (int i = 0; i < neuralNetwork.getNumberOfOutputs(); i++) {
            totalNeuronOutputErrorDerivativesOfOutputLayer[i] = totalErrorDerivatives[numOfNeuronsInPreviousLayer * i] * neuronOutputErrorDerivatives[numOfNeuronsInPreviousLayer * i];
        }
        editWeights(weightError, neuralNetwork.getOutputLayer());

        //edit hidden layers weights
        for (int hLayer = 0; hLayer < neuralNetwork.getNumberOfHiddenLayers(); hLayer++) {
            outputs = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - hLayer];
            numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - (hLayer + 1)].length;
            numOfConnectionsInLayer = numOfNeuronsInPreviousLayer * outputs.length;
            totalErrorDerivatives = new double[numOfConnectionsInLayer];
            neuronOutputErrorDerivatives = new double[numOfConnectionsInLayer];
            neuronErrorDerivatives = new double[numOfConnectionsInLayer];
            weightError = new double[numOfConnectionsInLayer];
            for (int i = 0; i < numOfConnectionsInLayer; i++) {
                for (int j = 0; j < finalOutputs.length; j++) {
                    totalErrorDerivatives[j] += totalNeuronOutputErrorDerivativesOfOutputLayer[j] *
                            neuralNetwork.getOutputLayer().getNeuron(j).getWeights()[i % neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - hLayer].length];
                }
                neuronOutputErrorDerivatives[i] = outputs[i / numOfNeuronsInPreviousLayer] *
                        (1 - outputs[i / numOfNeuronsInPreviousLayer]);
                neuronErrorDerivatives[i] = neuralNetwork.getLayersResult()[neuralNetwork.getNumberOfHiddenLayers() - (hLayer + 1)][i % numOfNeuronsInPreviousLayer];
                weightError[i] = totalErrorDerivatives[i] * neuronOutputErrorDerivatives[i] * neuronErrorDerivatives[i];
            }
            editWeights(weightError, neuralNetwork.getHiddenLayers()[neuralNetwork.getHiddenLayers().length - 1 - hLayer]);
        }

        System.out.println(totalError);
    }

    private void editWeights(double[] weightsError, Layer layer) throws IllegalAccessException {
        for (int i = 0; i < layer.getNeurons().length; i++) {
            for (int j = 0; j < layer.getNeuron(i).getWeights().length; j++) {
                layer.getNeuron(i).setWeight(j, layer.getNeuron(i).getWeights()[j] - (weightsError[i * layer.getNeuron(i).getWeights().length + j] * learingRate));
            }
        }
    }
}
