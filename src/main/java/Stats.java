import java.io.IOException;
import java.nio.file.Path;

public class Stats {
    private int boardsVisited = 0;
    private int boardsProcessed = 0;
    private int maxRecursionLevel = 0;
    private long start = System.nanoTime();
    private Path solutionPath;
    private Path statsPath;

    public Stats(Path solutionPath, Path statsPath) {
        this.solutionPath = solutionPath;
        this.statsPath = statsPath;
    }

    public boolean checkAndSaveStats(FifteenPuzzle currentBoard) {
        boardsVisited++;
        if (maxRecursionLevel < currentBoard.getIterations()) {
            maxRecursionLevel = currentBoard.getIterations();
        }
        if (currentBoard.check()) {
            double endTime = Math.round((System.nanoTime() - start)/1000.0)/1000.0;
            try {
                FileOperator.saveSolution(solutionPath, currentBoard.getHistoryOfMoves().toString(), currentBoard.getIterations());
                FileOperator.saveStats(statsPath,currentBoard.getIterations(),boardsVisited,boardsProcessed,maxRecursionLevel,endTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Time: " + endTime + "ms");
            System.out.println("Solution length: " + currentBoard.getIterations());
            System.out.println("States visited: " + boardsVisited);
            System.out.println("States processed: " + boardsProcessed);
            System.out.println("Max recursion level: " + maxRecursionLevel);
            System.out.println("SOLUTION: " + currentBoard.getHistoryOfMoves().toString());
            return true;
        }
        return false;
    }

    public void incrementProcessed() {
        boardsProcessed++;
    }
}
