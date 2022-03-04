import java.util.LinkedList;
import java.util.Stack;

public class DepthFirstSolve {
    private FifteenPuzzle currentBoard;
    private Stack<FifteenPuzzle> boardsToCheck = new Stack<>();
    private final int ITERATIONS = 20;

    public DepthFirstSolve(FifteenPuzzle currentBoard) {
        this.currentBoard = currentBoard;
        try {
            this.solve();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void solve() throws CloneNotSupportedException {
        System.out.println(currentBoard.toString());
        if (currentBoard.check()) {
            return;
        }

        //move left
        if (!currentBoard.getLastMove().equals("R") && currentBoard.getEmptyY() != 0
                && currentBoard.getIterations() < ITERATIONS) {
            FifteenPuzzle moveL = currentBoard.clone();
            moveL.moveEmptyL();
            boardsToCheck.push(moveL);
        }

        //move right
        if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != 3
                && currentBoard.getIterations() < ITERATIONS) {
            FifteenPuzzle moveR = currentBoard.clone();
            moveR.moveEmptyR();
            boardsToCheck.push(moveR);
        }

        //move up
        if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0
                && currentBoard.getIterations() < ITERATIONS) {
            FifteenPuzzle moveU = currentBoard.clone();
            moveU.moveEmptyU();
            boardsToCheck.push(moveU);
        }

        //move down
        if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != 3
                && currentBoard.getIterations() < ITERATIONS) {
            FifteenPuzzle moveD = currentBoard.clone();
            moveD.moveEmptyD();
            boardsToCheck.push(moveD);
        }
        this.currentBoard = boardsToCheck.pop();
        if (!this.boardsToCheck.empty()) {
            this.solve();
        }
    }
}
