import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileNeuronDao implements Dao<Neuron> {
    private String fileName;

    /**
     * Constructor.
     * @param fileName name of the file
     */
    public FileNeuronDao(String fileName) {
        this.fileName = fileName;
        if (!fileName.endsWith(".bin")) {
            this.fileName += ".bin";
        }
    }

    /**
     * Method loading written Neuron object.
     * @return Neuron
     */
    @Override
    public Neuron read() throws IOException {
        Neuron neuron;
        try (
                ObjectInputStream reader = new ObjectInputStream(
                        new FileInputStream(fileName)
                )
        ) {
            neuron = (Neuron) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Neuron read from file error", e);
        }
        return neuron;
    }

    /**
     * Save Neuron object to file.
     * @param obj object type Neuron which should be saved to file
     */
    @Override
    public void write(Neuron obj) throws IOException {
        try (
                ObjectOutputStream writer = new ObjectOutputStream(
                        new FileOutputStream(fileName)
                )
        ) {
            writer.writeObject(obj);
        } catch (IOException e) {
            throw new IOException("Neuron write to file error", e);
        }
    }

    @Override
    public void close() {
    }
}
