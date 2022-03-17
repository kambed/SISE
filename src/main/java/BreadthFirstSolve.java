import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSolve {
    private FifteenPuzzle currentBoard;
    private Queue<FifteenPuzzle> boardsToCheck = new LinkedList<>();
    private char chars[] = new char[4];
    private Stats stats;
    private int rows = 0;
    private int columns = 0;

    public BreadthFirstSolve(FifteenPuzzle currentBoard, String order, int rows, int columns,
                             Path solutionPath, Path statsPath) {
        stats = new Stats(solutionPath, statsPath);
        stats.incrementProcessed();
        this.rows = rows;
        this.columns = columns;
        this.currentBoard = currentBoard;
        try {
            for (int i = 0; i < 4; i++) {
                chars[i] = order.charAt(i);
            }
            this.solve();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void solve() throws CloneNotSupportedException {
        do {
            if (stats.checkAndSaveStats(this.currentBoard)) return;
            for (int i = 0; i < 4; i++) {
                move(chars[i]);
            }
            this.currentBoard = boardsToCheck.poll();
        } while (this.boardsToCheck.size() != 0);
    }

    private void move(char c) throws CloneNotSupportedException {
        switch (c) {
            case 'L':
                //move left
                if (!currentBoard.getLastMove().equals("R") && currentBoard.getEmptyY() != 0) {
                    FifteenPuzzle moveL = currentBoard.clone();
                    moveL.moveEmptyL();
                    boardsToCheck.add(moveL);
                    stats.incrementProcessed();
                }
                break;
            case 'R':
                //move right
                if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != columns - 1) {
                    FifteenPuzzle moveR = currentBoard.clone();
                    moveR.moveEmptyR();
                    boardsToCheck.add(moveR);
                    stats.incrementProcessed();
                }
                break;
            case 'U':
                //move up
                if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0) {
                    FifteenPuzzle moveU = currentBoard.clone();
                    moveU.moveEmptyU();
                    boardsToCheck.add(moveU);
                    stats.incrementProcessed();
                }
                break;
            case 'D':
                //move down
                if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != rows - 1) {
                    FifteenPuzzle moveD = currentBoard.clone();
                    moveD.moveEmptyD();
                    boardsToCheck.add(moveD);
                    stats.incrementProcessed();
                }
                break;
        }
    }
}
