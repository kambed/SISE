package backend.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperator {
    public static byte[] readData(Path p) throws IOException {
        if (Files.exists(p)) {
            return Files.readAllBytes(p);
        }
        return null;
    }
}
