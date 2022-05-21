package backend.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileOperator {
    public static double[][] readData(Path p) throws IOException {
        if (Files.exists(p)) {
            List<String> lines = Files.readAllLines(p);
            double[][] data = new double[lines.size()][(int)lines.get(0).chars().filter(ch -> ch == ',').count() + 1];
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                for (int j = 0; j < line.length; j++) {
                    data[i][j] = Double.parseDouble(line[j]);
                }
            }
            return data;
        }
        return null;
    }
}
