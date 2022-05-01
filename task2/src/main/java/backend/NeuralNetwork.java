package backend;

import backend.layer.DuplicationNeuronLayer;
import backend.layer.SigmoidalNeuronLayer;

import java.io.Serializable;

public class NeuralNetwork implements Serializable {
    DuplicationNeuronLayer inputLayer;
    SigmoidalNeuronLayer[] hiddenLayers;
    SigmoidalNeuronLayer outputLayer;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int numberOfHiddenLayers = 1;
    private int numberOfNeuronsInHiddenLayer;
    private boolean withBais;
    private double[][] layersResult;
    private boolean learningTime = true;

    public NeuralNetwork(int numberOfInputs, int numberOfOutputs, int numberOfNeuronsInHiddenLayer, boolean withBais) {
        this(numberOfInputs, numberOfOutputs, 1, numberOfNeuronsInHiddenLayer, withBais);
    }

    public NeuralNetwork(
            int numberOfInputs,
            int numberOfOutputs,
            int numberOfHiddenLayers,
            int numberOfNeuronsInHiddenLayer,
            boolean withBais
    ) {
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfNeuronsInHiddenLayer = numberOfNeuronsInHiddenLayer;
        this.withBais = withBais;

        layersResult = new double[numberOfHiddenLayers + 2][];

        inputLayer = new DuplicationNeuronLayer(numberOfInputs);
        hiddenLayers = new SigmoidalNeuronLayer[numberOfHiddenLayers];

        int layerNumberOfInputs = numberOfInputs;
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            hiddenLayers[i] = new SigmoidalNeuronLayer(layerNumberOfInputs, numberOfNeuronsInHiddenLayer, withBais);
            layerNumberOfInputs = numberOfNeuronsInHiddenLayer;
        }

        outputLayer = new SigmoidalNeuronLayer(numberOfNeuronsInHiddenLayer, numberOfOutputs, withBais);
    }

    public double[] calculateOutput(double[] input) {
        layersResult[0] = inputLayer.getOutputArray(input);
        for (int i = 1; i <= hiddenLayers.length; i++) {
            layersResult[i] = hiddenLayers[i - 1].getOutputArray(layersResult[i - 1]);
        }
        layersResult[hiddenLayers.length + 1] = outputLayer.getOutputArray(layersResult[hiddenLayers.length]);
        return layersResult[hiddenLayers.length + 1];
    }

    public SigmoidalNeuronLayer[] getHiddenLayers() {
        return hiddenLayers;
    }

    public SigmoidalNeuronLayer getOutputLayer() {
        return outputLayer;
    }

    public int getNumberOfHiddenLayers() {
        return numberOfHiddenLayers;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public int getNumberOfNeuronsInHiddenLayer() {
        return numberOfNeuronsInHiddenLayer;
    }

    public boolean isWithBais() {
        return withBais;
    }

    public double[][] getLayersResult() {
        return layersResult;
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
