import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileOperator {
    public static byte[][] readBoard(Path p) throws IOException {
        if (Files.exists(p)) {
            List<String> lines = Files.readAllLines(p);
            List<Integer> args = new ArrayList<>();
            for (String arg : lines.remove(0).split("\\s+")) {
                args.add(Integer.parseInt(arg));
            }
            byte[][] board = new byte[args.get(0)][args.get(1)];
            int i = 0;
            int j = 0;
            for (String s : lines) {
                for (String arg : s.split("\\s+")) {
                    board[i][j] = Byte.parseByte(arg);
                    j++;
                }
                j = 0;
                i++;
            }
            return board;
        }
        return null;
    }

    public static void saveSolution(Path p, String solution, int solutionLength) throws IOException {
        List<String> lines = Arrays.asList(Integer.toString(solutionLength), solution);
        Files.write(p, lines, StandardCharsets.UTF_8);
    }

    public static void saveStats(Path p, int solutionLength, int visited, int processed, int maxRecursionLever,
                                 double time) throws IOException {
        List<String> lines = Arrays.asList(Integer.toString(solutionLength), Integer.toString(visited),
                Integer.toString(processed), Integer.toString(maxRecursionLever), Double.toString(time));
        Files.write(p, lines, StandardCharsets.UTF_8);
    }
}
