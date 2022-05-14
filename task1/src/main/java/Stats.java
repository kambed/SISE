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
                StringBuilder sb = new StringBuilder();
                for (String move: currentBoard.getHistoryOfMoves()) {
                    sb.append(move);
                }
                FileOperator.saveSolution(solutionPath, sb.toString(), currentBoard.getIterations());
                FileOperator.saveStats(statsPath,currentBoard.getHistoryOfMoves().size(),boardsVisited,boardsProcessed,maxRecursionLevel,endTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void initStats() throws IOException {
        FileOperator.saveSolution(solutionPath, "", -1);
        FileOperator.saveStats(statsPath,-1,0,0,0,0);
    }

    public int getBoardsProcessed() {
        return boardsProcessed;
    }

    public int getBoardsVisited() {
        return boardsVisited;
    }

    public void incrementProcessed() {
        boardsProcessed++;
    }
}
