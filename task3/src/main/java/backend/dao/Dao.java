package backend.dao;

import java.io.IOException;

public interface Dao<T> extends AutoCloseable {
    /**
     * Read T object from file.
     *
     * @return T
     */
    T read() throws IOException;

    /**
     * Write (save) T object to file.
     *
     * @param obj object type T which should be saved to file
     */
    void write(T obj) throws IOException;
}
