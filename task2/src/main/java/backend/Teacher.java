package backend;


import backend.layer.Layer;

import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Teacher {
    private final NeuralNetwork neuralNetwork;
    private final double learningRate;
    private final double momentumRate;
    private double[][] errorDerivative;
    private double[][] biasErrorHidden;
    private final int numOfHiddenLayers;
    private final int numOfOutputs;

    public Teacher(NeuralNetwork neuralNetwork, double learningRate, double momentumRate) {
        this.neuralNetwork = neuralNetwork;
        this.numOfHiddenLayers = neuralNetwork.getNumberOfHiddenLayers();
        this.numOfOutputs = neuralNetwork.getNumberOfOutputs();
        this.learningRate = learningRate;
        this.momentumRate = momentumRate;
        this.errorDerivative = new double[numOfHiddenLayers + 2][];
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
        neuralNetwork.calculateOutput(inputs);
        double[] outputs = neuralNetwork.getLayersResult()[numOfHiddenLayers + 1];

        double[] weightError = calculateErrorInOutputLayer(outputs, expectedOutputs);

        double[][] weightErrorHidden = calculateErrorInHiddenLayers();

        editWeights(weightError, neuralNetwork.getOutputLayer());
        for (int i = 0; i < numOfHiddenLayers; i++) {
            editWeights(weightErrorHidden[i], neuralNetwork.getHiddenLayers()[numOfHiddenLayers - 1 - i]);
        }
        if (neuralNetwork.isWithBias()) {
            editBiases(errorDerivative[0], neuralNetwork.getOutputLayer());
            for (int i = 0; i < numOfHiddenLayers; i++) {
                editBiases(biasErrorHidden[i], neuralNetwork.getHiddenLayers()[numOfHiddenLayers - 1 - i]);
            }
        }
    }

    private void editWeights(double[] weightsError, Layer layer) throws IllegalAccessException {
        for (int i = 0; i < layer.getNeurons().length; i++) {
            int numOfWeightInNeuron = layer.getNeuron(i).getWeights().length;
            for (int j = 0; j < numOfWeightInNeuron; j++) {
                double weightError = (weightsError[i * layer.getNeuron(i).getWeights().length + j] * learningRate)
                        + (momentumRate * layer.getLastWeightChange()[j]);
                layer.updateNeuronWeight(i, j, -weightError);
                layer.setLastWeightChange(j, weightError);
            }
        }
    }

    private void editBiases(double[] biasErrors, Layer layer) throws IllegalAccessException {
        for (int i = 0; i < layer.getNeurons().length; i++) {
            double biasError = (biasErrors[i] * learningRate)
                    + (momentumRate * layer.getLastWeightChange()[i]);
            layer.updateNeuronBias(i, -biasError);
            layer.setLastBiasChange(i, biasError);
        }
    }

    private double[] calculateErrorInOutputLayer(
            double[] outputs, double[] expectedOutputs
    ) throws IllegalAccessException {
        errorDerivative[0] = new double[numOfOutputs];
        //edit output layer weights
        int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[numOfHiddenLayers].length;
        double[] weightError = new double[numOfNeuronsInPreviousLayer * numOfOutputs];
        for (int i = 0; i < numOfOutputs; i++) {
            double totalErrorDerivatives = -(expectedOutputs[i] - outputs[i]);
            double neuronOutputErrorDerivatives = outputs[i] * (1 - outputs[i]);
            for (int j = 0; j < numOfNeuronsInPreviousLayer; j++) {
                double neuronErrorDerivative = neuralNetwork.getLayersResult()[numOfHiddenLayers][j];
                weightError[i * numOfNeuronsInPreviousLayer + j] =
                        totalErrorDerivatives * neuronOutputErrorDerivatives * neuronErrorDerivative;
            }
            errorDerivative[0][i] = totalErrorDerivatives * neuronOutputErrorDerivatives;
        }
        return weightError;
    }

    private double[][] calculateErrorInHiddenLayers() throws IllegalAccessException {
        int maxConnections = 0;
        for (int i = 0; i < neuralNetwork.getLayersResult().length; i++) {
            int layerNeurons = neuralNetwork.getLayersResult()[i].length;
            if (maxConnections < layerNeurons) {
                maxConnections = layerNeurons;
            }
        }
        maxConnections *= maxConnections;
        double[][] weightErrorHidden = new double[numOfHiddenLayers][maxConnections];
        if (neuralNetwork.isWithBias()) {
            biasErrorHidden = new double[numOfHiddenLayers][maxConnections];
        }
        int numOfConnectionsInAllPreviousLayers = 0;
        for (int hLayer = 0; hLayer < numOfHiddenLayers; hLayer++) {
            int hiddenLayerNum = numOfHiddenLayers - hLayer;
            double[] outputs = neuralNetwork.getLayersResult()[hiddenLayerNum];
            int numOfNeuronsInPreviousLayer = neuralNetwork.getLayersResult()[hiddenLayerNum - 1].length;

            int numOfNeuronsInNextLayer = 0;
            if (hLayer == 0) {
                numOfConnectionsInAllPreviousLayers = numOfOutputs;
            } else {
                numOfNeuronsInNextLayer = neuralNetwork.getLayersResult()[hiddenLayerNum + 1].length;
            }
            errorDerivative[hLayer + 1] = new double[numOfConnectionsInAllPreviousLayers * outputs.length];


            for (int i = 0; i < outputs.length; i++) {
                double totalErrorDerivative = 0;
                double neuronOutputErrorDerivative = outputs[i] * (1 - outputs[i]);
                if (hLayer == 0) {
                    for (int j = 0; j < numOfOutputs; j++) {
                        double weight = neuralNetwork.getOutputLayer().getNeuronWeight(j, i);
                        errorDerivative[hLayer + 1][j + i * numOfOutputs] = errorDerivative[0][j] * weight;
                        totalErrorDerivative += errorDerivative[hLayer + 1][j + i * numOfOutputs];
                    }
                    for (int j = 0; j < numOfOutputs; j++) {
                        errorDerivative[hLayer + 1][j + i * numOfOutputs] *= neuronOutputErrorDerivative;
                    }
                } else {
                    for (int j = 0; j < numOfNeuronsInNextLayer; j++) {
                        double weight = neuralNetwork.getHiddenLayers()[hiddenLayerNum].getNeuronWeight(
                                j, i
                        );
                        for (int k = 0; k < numOfConnectionsInAllPreviousLayers / numOfNeuronsInNextLayer; k++) {
                            int numOfDerivative = k + j * (numOfConnectionsInAllPreviousLayers / numOfNeuronsInNextLayer)
                                    + i * numOfConnectionsInAllPreviousLayers;
                            int numOfPreviousDerivative = j * (numOfConnectionsInAllPreviousLayers / numOfNeuronsInNextLayer) + k;
                            errorDerivative[hLayer + 1][numOfDerivative] = weight *
                                    errorDerivative[hLayer][numOfPreviousDerivative];
                            totalErrorDerivative += errorDerivative[hLayer + 1][numOfDerivative];
                        }
                    }
                    for (int j = 0; j < numOfConnectionsInAllPreviousLayers; j++) {
                        errorDerivative[hLayer + 1][j + i * numOfConnectionsInAllPreviousLayers] *= neuronOutputErrorDerivative;
                    }
                }

                for (int j = 0; j < numOfNeuronsInPreviousLayer; j++) {
                    double neuronErrorDerivative = neuralNetwork.getLayersResult()[hiddenLayerNum - 1][j];
                    weightErrorHidden[hLayer][i * numOfNeuronsInPreviousLayer + j] =
                            totalErrorDerivative * neuronOutputErrorDerivative * neuronErrorDerivative;
                }
                if (neuralNetwork.isWithBias()) {
                    biasErrorHidden[hLayer][i] = totalErrorDerivative * neuronOutputErrorDerivative;
                }
            }
            numOfConnectionsInAllPreviousLayers *= outputs.length;
        }
        return weightErrorHidden;
    }

    public double calculateError(double[][] inputs, double[][] expectedOutputs) {
        double totalError = 0;
        int expectedOutputsLength = expectedOutputs.length;
        double[][] outputs = new double[expectedOutputsLength][expectedOutputs[0].length];
        for (int i = 0; i < expectedOutputsLength; i++) {
            outputs[i] = neuralNetwork.calculateOutput(inputs[i]);
            for (int j = 0; j < outputs[i].length; j++) {
                totalError += ((expectedOutputs[i][j] - outputs[i][j]) * (expectedOutputs[i][j] - outputs[i][j]));
            }
        }
        return totalError / expectedOutputsLength;
    }
}
