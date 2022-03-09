import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSolve {
    private FifteenPuzzle currentBoard;
    private Queue<FifteenPuzzle> boardsToCheck = new LinkedList<>();
    private int boardsVisited = 0;
    private int boardsProcessed = 0;
    private int maxRecursionLevel = 0;
    private long start = System.currentTimeMillis();

    public BreadthFirstSolve(FifteenPuzzle currentBoard, String order) {
        this.currentBoard = currentBoard;
        try {
            this.solve(order);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void solve(String order) throws CloneNotSupportedException {
        boardsVisited++;
        if (maxRecursionLevel < this.currentBoard.getIterations()) {
            maxRecursionLevel = this.currentBoard.getIterations();
        }
        if (currentBoard.check()) {
            System.out.println("Solution length: " + currentBoard.getIterations());
            System.out.println("States visited: " + this.boardsVisited);
            System.out.println("States processed: " + this.boardsProcessed);
            System.out.println("Max recursion level: " + this.maxRecursionLevel);
            System.out.println("Time: " + (System.currentTimeMillis() - this.start) + "ms");
            System.out.println("SOLUTION: " + currentBoard.getHistoryOfMoves().toString());
            return;
        }
        for (int i = 0; i < 4; i++) {
            move(order.charAt(i));
        }
        this.currentBoard = boardsToCheck.poll();
        if (this.boardsToCheck.size() != 0) {
            this.solve(order);
        }
    }

    private void move(char c) throws CloneNotSupportedException {
        switch (c) {
            case 'L':
                //move left
                if (!currentBoard.getLastMove().equals("R") && currentBoard.getEmptyY() != 0) {
                    FifteenPuzzle moveL = currentBoard.clone();
                    moveL.moveEmptyL();
                    boardsToCheck.add(moveL);
                    boardsProcessed++;
                }
                break;
            case 'R':
                //move right
                if (!currentBoard.getLastMove().equals("L") && currentBoard.getEmptyY() != 3) {
                    FifteenPuzzle moveR = currentBoard.clone();
                    moveR.moveEmptyR();
                    boardsToCheck.add(moveR);
                    boardsProcessed++;
                }
                break;
            case 'U':
                //move up
                if (!currentBoard.getLastMove().equals("D") && currentBoard.getEmptyX() != 0) {
                    FifteenPuzzle moveU = currentBoard.clone();
                    moveU.moveEmptyU();
                    boardsToCheck.add(moveU);
                    boardsProcessed++;
                }
                break;
            case 'D':
                //move down
                if (!currentBoard.getLastMove().equals("U") && currentBoard.getEmptyX() != 3) {
                    FifteenPuzzle moveD = currentBoard.clone();
                    moveD.moveEmptyD();
                    boardsToCheck.add(moveD);
                    boardsProcessed++;
                }
                break;
        }
    }
}
