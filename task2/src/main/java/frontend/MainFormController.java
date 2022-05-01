package frontend;

import backend.NeuralNetwork;
import backend.Teacher;
import backend.dao.FileNeuralNetworkDao;
import backend.dao.FileOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Neural Network";

    private double[][] learnInputs;
    private double[][] learnOutputs;
    private double[][] testInputs;

    private NeuralNetwork nn = null;

    @FXML
    TextField numOfInputs;
    @FXML
    TextField numOfOutputs;
    @FXML
    TextField numOfHiddenLayers;
    @FXML
    TextField numOfNeuronsInHiddenLayers;
    @FXML
    CheckBox withBias;
    @FXML
    TextField learningRate;
    @FXML
    TextField momentumRate;
    @FXML
    TextField numOfEras;
    @FXML
    TextField learningDataFilePath;
    @FXML
    TextField learningOutputFilePath;
    @FXML
    TextField dataFilePath;
    @FXML
    TextArea consoleArea;
    @FXML
    RadioButton learnRadioButton;
    @FXML
    RadioButton workRadioButton;
    @FXML
    Button calculateButton;
    @FXML
    Button learnButton;
    @FXML
    Button loadTestDataButton;
    @FXML
    Button loadLearningOutputDataButton;
    @FXML
    Button loadLearningDataButton;
    @FXML
    Button saveButton;
    @FXML
    Button loadButton;

    public void startCalculating(ActionEvent actionEvent) {
        for (int i = 1; i <= testInputs.length; i++) {
            consoleArea.appendText("Data " + i + Arrays.toString(nn.calculateOutput(testInputs[i - 1])) + "\n");
        }
    }

    public void startLearning(ActionEvent actionEvent) {
        nn = new NeuralNetwork(Integer.parseInt(numOfInputs.getText()),
                Integer.parseInt(numOfOutputs.getText()),
                Integer.parseInt(numOfHiddenLayers.getText()),
                Integer.parseInt(numOfNeuronsInHiddenLayers.getText()),
                withBias.isSelected());
        consoleArea.appendText("[NEURAL NETWORK INITIALIZED]: " +
                Integer.parseInt(numOfInputs.getText()) + " inputs, " +
                Integer.parseInt(numOfOutputs.getText()) + " outputs, " +
                Integer.parseInt(numOfHiddenLayers.getText()) + " hidden layers, " +
                Integer.parseInt(numOfNeuronsInHiddenLayers.getText()) + " neurons in hidden layers, bias: " +
                withBias.isSelected() + "\n");
        Teacher t = new Teacher(nn, Double.parseDouble(learningRate.getText()), Double.parseDouble(momentumRate.getText()));
        consoleArea.appendText("[TEACHER INITIALIZED]: " +
                Double.parseDouble(learningRate.getText()) + " learning rate, " +
                Double.parseDouble(momentumRate.getText()) + " momentum rate \n");
        for (int i = 0; i < Integer.parseInt(numOfEras.getText()) - 1; i += 10) {
            consoleArea.appendText("Error after " + i + "eras: " + t.calculateError(learnInputs, learnOutputs) + "\n");
            t.changeWeightWithBackpropagation(10, learnInputs, learnOutputs);
        }
        if (Integer.parseInt(numOfEras.getText()) % 10 == 0) {
            t.changeWeightWithBackpropagation(10, learnInputs, learnOutputs);
        } else {
            t.changeWeightWithBackpropagation(Integer.parseInt(numOfEras.getText()) % 10, learnInputs, learnOutputs);
        }
        consoleArea.appendText("Error after " + numOfEras.getText() + "eras: " + t.calculateError(learnInputs, learnOutputs) + "\n");
    }

    public void loadLearningData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open learning data file ", actionEvent);
        if (!stringPath.isBlank()) {
            learningDataFilePath.setText(stringPath);
            learnInputs = FileOperator.readData(Paths.get(stringPath));
            consoleArea.appendText("Learning data opened from: " + stringPath + "\n");
        }
    }

    public void loadLearningOutput(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open learning output data file ", actionEvent);
        if (!stringPath.isBlank()) {
            learningOutputFilePath.setText(stringPath);
            learnOutputs = FileOperator.readData(Paths.get(stringPath));
            consoleArea.appendText("Learning output data opened from: " + stringPath + "\n");
        }
    }

    public void loadTestData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open test data file ", actionEvent);
        if (!stringPath.isBlank()) {
            dataFilePath.setText(stringPath);
            testInputs = FileOperator.readData(Paths.get(stringPath));
            consoleArea.appendText("Test data opened from: " + stringPath + "\n");
        }
    }

    public void loadNeuralNetwork(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open file with neural network ", actionEvent);
        if (!stringPath.isBlank()) {
            FileNeuralNetworkDao fnnd = new FileNeuralNetworkDao(Paths.get(stringPath).toAbsolutePath().toString());
            nn = fnnd.read();
            consoleArea.appendText("Neural network read from: " + stringPath + "\n");
            numOfInputs.setText(String.valueOf(nn.getNumberOfInputs()));
            numOfOutputs.setText(String.valueOf(nn.getNumberOfOutputs()));
            numOfHiddenLayers.setText(String.valueOf(nn.getNumberOfHiddenLayers()));
            numOfNeuronsInHiddenLayers.setText(String.valueOf(nn.getNumberOfNeuronsInHiddenLayer()));
            withBias.setSelected(nn.isWithBias());
        }
    }

    public void saveNeuralNetwork(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.saveChooser("Save neural network to file ", actionEvent);
        if (!stringPath.isBlank()) {
            FileNeuralNetworkDao fnnd = new FileNeuralNetworkDao(Paths.get(stringPath).toAbsolutePath().toString());
            fnnd.write(nn);
            consoleArea.appendText("Neural network saved to: " + stringPath + "\n");
        }
    }

    public void saveLogs(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.saveChooser("Save logs to file ", actionEvent);
        if (!stringPath.isBlank()) {
            Files.write(Paths.get(stringPath), consoleArea.getText().getBytes());
            consoleArea.setText("");
            consoleArea.appendText("Logs saved saved to: " + stringPath + "\n");
        }
    }

    public void changeMode(ActionEvent actionEvent) {
        changeDisableMode(learnRadioButton.isSelected());
        if (learnRadioButton.isSelected()) {
            consoleArea.appendText("Learning mode selected \n");
        } else {
            consoleArea.appendText("Working mode selected \n");
        }
    }

    public void changeDisableMode(boolean learningMode) {
        calculateButton.setDisable(learningMode);
        loadTestDataButton.setDisable(learningMode);
        loadButton.setDisable(learningMode);

        learnButton.setDisable(!learningMode);
        numOfOutputs.setDisable(!learningMode);
        numOfInputs.setDisable(!learningMode);
        numOfHiddenLayers.setDisable(!learningMode);
        numOfNeuronsInHiddenLayers.setDisable(!learningMode);
        withBias.setDisable(!learningMode);
        learningRate.setDisable(!learningMode);
        momentumRate.setDisable(!learningMode);
        numOfEras.setDisable(!learningMode);
        loadLearningOutputDataButton.setDisable(!learningMode);
        loadLearningDataButton.setDisable(!learningMode);
    }
}