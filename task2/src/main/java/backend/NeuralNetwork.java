package backend;

import backend.layer.DuplicationNeuronLayer;
import backend.layer.SigmoidalNeuronLayer;

import java.io.Serializable;
import java.util.Arrays;

public class NeuralNetwork implements Serializable {
    DuplicationNeuronLayer inputLayer;
    SigmoidalNeuronLayer[] hiddenLayers;
    SigmoidalNeuronLayer outputLayer;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int numberOfHiddenLayers = 1;
    private int numberOfNeuronsInHiddenLayer;
    private boolean learningTime = true;

    public NeuralNetwork(int numberOfInputs, int numberOfOutputs, int numberOfNeuronsInHiddenLayer) {
        this(numberOfInputs, numberOfOutputs, 1, numberOfNeuronsInHiddenLayer);
    }

    public NeuralNetwork(
            int numberOfInputs,
            int numberOfOutputs,
            int numberOfHiddenLayers,
            int numberOfNeuronsInHiddenLayer
    ) {
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfNeuronsInHiddenLayer = numberOfNeuronsInHiddenLayer;

        inputLayer = new DuplicationNeuronLayer(numberOfInputs);
        hiddenLayers = new SigmoidalNeuronLayer[numberOfHiddenLayers];

        int layerNumberOfInputs = numberOfInputs;
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hiddenLayers[i] = new SigmoidalNeuronLayer(layerNumberOfInputs, numberOfNeuronsInHiddenLayer);
            layerNumberOfInputs = numberOfNeuronsInHiddenLayer;
        }

        outputLayer = new SigmoidalNeuronLayer(numberOfNeuronsInHiddenLayer, numberOfOutputs);
    }

    public double[] calculateOutput(double[] input) {
        double[] previousLayerResults = inputLayer.getOutputArray(input);
        for (SigmoidalNeuronLayer hiddenLayer : hiddenLayers) {
            previousLayerResults = hiddenLayer.getOutputArray(previousLayerResults);
        }
        return outputLayer.getOutputArray(previousLayerResults);
    }

    public boolean isLearningTime() {
        return learningTime;
    }

    public void switchToLearningTime() {
        this.learningTime = true;
        inputLayer.switchToLearningTime();
        for (SigmoidalNeuronLayer layer : hiddenLayers) {
            layer.switchToLearningTime();
        }
        outputLayer.switchToLearningTime();
    }

    public void switchToWorkingTime() {
        this.learningTime = false;
        inputLayer.switchToWorkingTime();
        for (SigmoidalNeuronLayer layer : hiddenLayers) {
            layer.switchToWorkingTime();
        }
        outputLayer.switchToWorkingTime();
    }

}
