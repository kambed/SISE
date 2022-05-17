package backend.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileOperatorTest {
    @Test
    @DisplayName("File import test")
    void fileImportTest() throws IOException {
        FileOperator.readData(Paths.get(""));
    }
}