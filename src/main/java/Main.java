import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        String val = ArgsValidator.validate(args);
        if (val != null) {
            System.out.println(val);
            return;
        }
        Path startPuzzle = Paths.get("../../../files/" + args[2]);
        int[][] fileImport = FileReaderClass.readBoard(startPuzzle);
        int[][] start = {
                {5, 3, 7, 1},
                {15, 13, 9, 11},
                {2, 8, 6, 10},
                {4, 0, 12, 14}
        };
        int[][] solved = {
                {1, 0, 3, 4},
                {5, 2, 6, 8},
                {13, 10, 7, 12},
                {14, 9, 11, 15}
        };
        int[][] solved2 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {0, 13, 14, 15}
        };
        int[][] test = {
                {0, 5, 1, 11},
                {2, 8, 10, 6},
                {15, 12, 7, 4},
                {14, 13, 8, 3}
        };
        FifteenPuzzle fp = new FifteenPuzzle(fileImport);
        switch (args[0]) {
            case "bfs" -> new BreadthFirstSolve(fp, args[1]);
            case "dfs" -> new DepthFirstSolve(fp, args[1]);
        }
    }
}
