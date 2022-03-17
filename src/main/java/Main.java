import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String val = ArgsValidator.validate(args);
        if (val != null) {
            System.out.println(val);
            return;
        }
        Path startPuzzle = Paths.get(args[2]);
        byte[][] fileImport = FileOperator.readBoard(startPuzzle);
        int rows = FileOperator.getRows();
        int columns = FileOperator.getColumns();
        FifteenPuzzle fp = new FifteenPuzzle(fileImport, rows, columns);
        Path solutionPath = Paths.get(args[3]);
        Path statsPath = Paths.get(args[4]);
        Stats stats = new Stats(solutionPath, statsPath);
        stats.initStats();
        switch (args[0]) {
            case "bfs" -> new BreadthFirstSolve(fp, args[1], rows, columns, solutionPath, statsPath);
            case "dfs" -> new DepthFirstSolve(fp, args[1], rows, columns, solutionPath, statsPath);
            case "astr" -> new Astar(fp, args[1], rows, columns, solutionPath, statsPath);
        }
    }
}
