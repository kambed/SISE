import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReaderClass {
    public static int[][] readBoard(Path p) throws IOException {
        if (Files.exists(p)) {
            List<String> lines = Files.readAllLines(p);
            List<Integer> args = new ArrayList<>();
            for (String arg : lines.remove(0).split("\\s+")) {
                args.add(Integer.parseInt(arg));
            }
            int[][] board = new int[args.get(0)][args.get(1)];
            int i = 0;
            int j = 0;
            for (String s : lines) {
                for (String arg : s.split("\\s+")) {
                    board[i][j] = Integer.parseInt(arg);
                    j++;
                }
                j = 0;
                i++;
            }
            return board;
        }
        return null;
    }
}
