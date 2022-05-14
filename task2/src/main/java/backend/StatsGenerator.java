package backend;

public class StatsGenerator {
    private static int numOfCorrect;
    private static int numOfIncorrect;
    private static int[] numOfCorrectPerClass;
    private static int[] numOfIncorrectPerClass;
    private static int[] fp, fn, tp, tn;
    private static double[] precision, recall, fMeasure;

    public static void validateResults(double[][] outputs, double[][] expectedOutputs) {
        numOfCorrect = 0;
        numOfIncorrect = 0;
        numOfCorrectPerClass = new int[expectedOutputs[0].length];
        numOfIncorrectPerClass = new int[expectedOutputs[0].length];
        int numOfObjects = outputs.length;
        int numOfClasses = outputs[0].length;
        fp = new int[numOfClasses];
        fn = new int[numOfClasses];
        tp = new int[numOfClasses];
        tn = new int[numOfClasses];
        for (int i = 0; i < numOfObjects; i++) {
            int expectedMaxIndex = 0;
            int maxIndex = 0;
            double expectedMaxValue = expectedOutputs[i][0];
            double maxValue = outputs[i][0];
            for (int j = 1; j < numOfClasses; j++) {
                if (expectedMaxValue < expectedOutputs[i][j]) {
                    expectedMaxValue = expectedOutputs[i][j];
                    expectedMaxIndex = j;
                }
                if (maxValue < outputs[i][j]) {
                    maxValue = outputs[i][j];
                    maxIndex = j;
                }
            }
            for (int j = 0; j < numOfClasses; j++) {
                if (expectedMaxIndex != j && maxIndex == j) {
                    fp[j]++;
                } else if (expectedMaxIndex == j && maxIndex != j) {
                    fn[j]++;
                } else if (expectedMaxIndex != j) {
                    tn[j]++;
                } else {
                    tp[j]++;
                }
            }
        }
        precision = new double[numOfClasses];
        recall = new double[numOfClasses];
        fMeasure = new double[numOfClasses];
        for (int i = 0; i < numOfClasses; i++) {
            numOfCorrect += tp[i];
            numOfIncorrect += fp[i];
            numOfCorrectPerClass[i] = tp[i];
            numOfIncorrectPerClass[i] = fp[i];
            precision[i] = (tp[i] * 1.0) / (tp[i] + fp[i]);
            recall[i] = (tp[i] * 1.0) / (tp[i] + fn[i]);
            fMeasure[i] = 2 * (precision[i] * recall[i]) / (precision[i] + recall[i]);
        }
    }

    public static String getConfusionMatrix(int classNum) {
        return "Confusion matrix: \n" +
                "isCor/res | Positive | Negative \n" +
                "true | " + tp[classNum] + " | " + tn[classNum] + "\n" +
                "false | " + fp[classNum] + " | " + fn[classNum] + "\n";
    }

    public static int getNumOfCorrect() {
        return numOfCorrect;
    }

    public static int getNumOfIncorrect() {
        return numOfIncorrect;
    }

    public static int[] getNumOfCorrectPerClass() {
        return numOfCorrectPerClass;
    }

    public static int[] getNumOfIncorrectPerClass() {
        return numOfIncorrectPerClass;
    }

    public static double getPrecision(int i) {
        return precision[i];
    }

    public static double getRecall(int i) {
        return recall[i];
    }

    public static double getfMeasure(int i) {
        return fMeasure[i];
    }
}
