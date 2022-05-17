package frontend;

import backend.NeuralNetwork;
import backend.StatsGenerator;
import backend.Teacher;
import backend.dao.FileNeuralNetworkDao;
import backend.dao.FileOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jfree.chart.ChartUtilities;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static backend.MnistCalculator.calculateNumber;

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
    @FXML
    ImageView chart;
    @FXML
    CheckBox generateStats;

    public void startCalculating(ActionEvent actionEvent) {
        if (generateStats.isSelected()) {
            double[][] outputs = new double[learnOutputs.length][learnOutputs[0].length];
            for (int i = 0; i < testInputs.length; i++) {
                double[] inputsDouble = new double[testInputs[i].length];
                for (int j = 0; j < testInputs[i].length; j++) {
                    inputsDouble[j] = testInputs[i][j];
                }
                consoleArea.appendText("Data " + (i + 1) + Arrays.toString(nn.calculateOutput(inputsDouble)) + "\n");
                outputs[i] = nn.calculateOutput(inputsDouble);
            }
            StatsGenerator.validateResults(outputs, learnOutputs);
            consoleArea.appendText("Correct global: [" + StatsGenerator.getNumOfCorrect() + "]\n");
            consoleArea.appendText("Incorrect global: [" + StatsGenerator.getNumOfIncorrect() + "]\n");
            for (int i = 0; i < learnOutputs[0].length; i++) {
                consoleArea.appendText("===== CLASS " + (i + 1) + "===== \n");
                consoleArea.appendText("Correct: [" +
                        StatsGenerator.getNumOfCorrectPerClass()[i] + "]\n");
                consoleArea.appendText("Incorrect: [" +
                        StatsGenerator.getNumOfIncorrectPerClass()[i] + "]\n");
                consoleArea.appendText(StatsGenerator.getConfusionMatrix(i));
                consoleArea.appendText("Precision: [" + StatsGenerator.getPrecision(i) + "]\n");
                consoleArea.appendText("Recall: [" + StatsGenerator.getRecall(i) + "]\n");
                consoleArea.appendText("F-Measure: [" + StatsGenerator.getfMeasure(i) + "]\n");
            }
        } else {
            for (int i = 0; i < testInputs.length; i++) {
                consoleArea.appendText("Data " + (i + 1) + Arrays.toString(nn.calculateOutput(testInputs[i])) + "\n");
            }
        }
    }

    public void changeStats(ActionEvent actionEvent) {
        loadLearningOutputDataButton.setDisable(!generateStats.isSelected());
    }

    public void startLearning(ActionEvent actionEvent) throws FileNotFoundException {
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

        int numOfErasInt = Integer.parseInt(numOfEras.getText());
        double[] eras = new double[numOfErasInt + 1];
        double[] errors = new double[numOfErasInt + 1];
        for (int i = 0; i < numOfErasInt; i++) {
            eras[i] = i;
            errors[i] = t.calculateError(learnInputs, learnOutputs);
            t.changeWeightWithBackpropagation(1, learnInputs, learnOutputs);
        }
        eras[numOfErasInt] = Integer.parseInt(numOfEras.getText());
        errors[numOfErasInt] = t.calculateError(learnInputs, learnOutputs);

        try {
            ChartUtilities.saveChartAsPNG(
                    new File("chart.png"),
                    ChartGenerator.generatePlot(eras, errors),
                    400, 220
            );
        } catch (IOException e) {
            consoleArea.appendText("Wystapił problem przy generowaniu wykresu. \n");
        }
        FileInputStream input = new FileInputStream("chart.png");
        chart.setImage(new Image(input));
    }

    public void loadLearningData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open learning data file ", actionEvent);
        if (!stringPath.isBlank()) {
            learningDataFilePath.setText(stringPath);
            byte[] bytes = FileOperator.readData(Paths.get(stringPath));
            int numOfObjects = calculateNumber(bytes,4);
            int rows = calculateNumber(bytes,8);
            int columns = calculateNumber(bytes,12);
            int inputs = rows * columns;
            learnInputs = new double[numOfObjects][inputs];
            for (int i = 0; i < numOfObjects; i++) {
                for (int j = 0; j < inputs; j++) {
                    learnInputs[i][j] = bytes[16 + j + i * inputs] < 0 ?
                            bytes[16 + j + i * inputs] + 256 : bytes[16 + j + i * inputs];
                }
            }
            consoleArea.appendText("Learning data opened from: " + stringPath + "\n");
        }
    }

    public void loadLearningOutput(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open learning output data file ", actionEvent);
        if (!stringPath.isBlank()) {
            learningOutputFilePath.setText(stringPath);
            byte[] bytes = FileOperator.readData(Paths.get(stringPath));
            int numOfObjects = calculateNumber(bytes,4);
            learnOutputs = new double[numOfObjects][10];
            for (int i = 0; i < numOfObjects; i++) {
                switch (bytes[i + 8]) {
                    case 0 -> learnOutputs[i] = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                    case 1 -> learnOutputs[i] = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                    case 2 -> learnOutputs[i] = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                    case 3 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                    case 4 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                    case 5 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0};
                    case 6 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
                    case 7 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
                    case 8 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0};
                    case 9 -> learnOutputs[i] = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
                }
            }
            consoleArea.appendText("Learning output data opened from: " + stringPath + "\n");
        }
    }

    public void loadTestData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open test data file ", actionEvent);
        if (!stringPath.isBlank()) {
            dataFilePath.setText(stringPath);
            byte[] bytes = FileOperator.readData(Paths.get(stringPath));
            int numOfObjects = calculateNumber(bytes,4);
            int rows = calculateNumber(bytes,8);
            int columns = calculateNumber(bytes,12);
            int inputs = rows * columns;
            testInputs = new double[numOfObjects][inputs];
            for (int i = 0; i < numOfObjects; i++) {
                for (int j = 0; j < inputs; j++) {
                    testInputs[i][j] = bytes[16 + j + i * inputs] < 0 ?
                            bytes[16 + j + i * inputs] + 256 : bytes[16 + j + i * inputs];
                }
            }
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
        generateStats.setDisable(learningMode);

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