import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSolve {
    FifteenPuzzle currentBoard;
    Queue<FifteenPuzzle> boardsToCheck = new LinkedList<>();

    public BreadthFirstSolve(FifteenPuzzle currentBoard) {
        this.currentBoard = currentBoard;
        try {
            this.solve();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void solve() throws CloneNotSupportedException {
        if (currentBoard.check()) {
            return;
        }
        //move left
        if (!currentBoard.getLastMove().equals("R") && currentBoard.getEmptyY() != 0
                && currentBoard.getIterations() < 20) {
            FifteenPuzzle moveL = currentBoard.clone();
            moveL.moveEmptyL();
            boardsToCheck.add(moveL);
        }

        //move right
        if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != 3
                && currentBoard.getIterations() < 20) {
            FifteenPuzzle moveR = currentBoard.clone();
            moveR.moveEmptyR();
            boardsToCheck.add(moveR);
        }

        //move up
        if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0
                && currentBoard.getIterations() < 20) {
            FifteenPuzzle moveU = currentBoard.clone();
            moveU.moveEmptyU();
            boardsToCheck.add(moveU);
        }

        //move down
        if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != 3
                && currentBoard.getIterations() < 20) {
            FifteenPuzzle moveD = currentBoard.clone();
            moveD.moveEmptyD();
            boardsToCheck.add(moveD);
        }
        this.currentBoard = boardsToCheck.poll();
        if (this.boardsToCheck.size() != 0) {
            this.solve();
        }
    }
}
