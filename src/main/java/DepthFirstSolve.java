import java.nio.file.Path;
import java.util.Collection;
import java.util.Stack;

public class DepthFirstSolve {
    private FifteenPuzzle currentBoard;
    private Stack<FifteenPuzzle> boardsToCheck = new Stack<>();
    private char chars[] = new char[4];
    private Stats stats;
    private final int ITERATIONS = 20;

    public DepthFirstSolve(FifteenPuzzle currentBoard, String order, Path solutionPath, Path statsPath) {
        stats = new Stats(solutionPath, statsPath);
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
        if (stats.checkAndDisplayStats(this.currentBoard)) return;
        for (int i = 3; i >= 0; i--) {
            move(chars[i]);
        }
        this.currentBoard = boardsToCheck.pop();
        if (!this.boardsToCheck.empty()) {
            this.solve();
        }
    }

    private void move(char c) throws CloneNotSupportedException {
        switch (c) {
            case 'L':
                //move left
                if (!currentBoard.getLastMove().equals("R") && currentBoard.getEmptyY() != 0
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveL = currentBoard.clone();
                    moveL.moveEmptyL();
                    boardsToCheck.push(moveL);
                    stats.incrementProcessed();
                }
                break;
            case 'R':
                //move right
                if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != 3
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveR = currentBoard.clone();
                    moveR.moveEmptyR();
                    boardsToCheck.push(moveR);
                    stats.incrementProcessed();
                }
                break;
            case 'U':
                //move up
                if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveU = currentBoard.clone();
                    moveU.moveEmptyU();
                    boardsToCheck.push(moveU);
                    stats.incrementProcessed();
                }
                break;
            case 'D':
                //move down
                if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != 3
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveD = currentBoard.clone();
                    moveD.moveEmptyD();
                    boardsToCheck.push(moveD);
                    stats.incrementProcessed();
                }
                break;
        }
    }
}
