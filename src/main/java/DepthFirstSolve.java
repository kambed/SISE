import java.util.Stack;

public class DepthFirstSolve {
    private FifteenPuzzle currentBoard;
    private Stack<FifteenPuzzle> boardsToCheck = new Stack<>();
    private final int ITERATIONS = 20;

    public DepthFirstSolve(FifteenPuzzle currentBoard, String order) {
        this.currentBoard = currentBoard;
        try {
            this.solve(order);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void solve(String order) throws CloneNotSupportedException {
        if (currentBoard.check()) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            move(order.charAt(i));
        }
        this.currentBoard = boardsToCheck.pop();
        if (!this.boardsToCheck.empty()) {
            this.solve(order);
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
                }
                break;
            case 'R':
                //move right
                if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != 3
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveR = currentBoard.clone();
                    moveR.moveEmptyR();
                    boardsToCheck.push(moveR);
                }
                break;
            case 'U':
                //move up
                if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveU = currentBoard.clone();
                    moveU.moveEmptyU();
                    boardsToCheck.push(moveU);
                }
                break;
            case 'D':
                //move down
                if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != 3
                        && currentBoard.getIterations() < ITERATIONS) {
                    FifteenPuzzle moveD = currentBoard.clone();
                    moveD.moveEmptyD();
                    boardsToCheck.push(moveD);
                }
                break;
        }
    }
}
