package backend;

public class MnistCalculator {
    public static int calculateNumber(byte[] bytes, int index) {
        return ((bytes[index] < 0 ? bytes[index] + 256 : bytes[index]) << 24) +
                ((bytes[index + 1] < 0 ? bytes[index + 1] + 256 : bytes[index + 1]) << 16) +
                ((bytes[index + 2] < 0 ? bytes[index + 2] + 256 : bytes[index + 2]) << 8) +
                (bytes[index + 3] < 0 ? bytes[index + 3] + 256 : bytes[index + 3]);
    }
}
