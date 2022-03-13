import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Astar {
    private FifteenPuzzle currentBoard;
    private List<FifteenPuzzle> boardsToCheck = new ArrayList<>();
    private List<Integer> hashCodesProcessed = new ArrayList<>();
    private Stats stats;
    private String heuristic;
    private FifteenPuzzleComparator fpc = new FifteenPuzzleComparator();
    private int rows = 0;
    private int columns = 0;

    public Astar(FifteenPuzzle currentBoard, String heuristic, int rows, int columns,
                 Path solutionPath, Path statsPath) {
        stats = new Stats(solutionPath, statsPath);
        this.currentBoard = currentBoard;
        this.heuristic = heuristic;
        this.rows = rows;
        this.columns = columns;
        switch (heuristic) {
            case "hamm" -> solveHamm();
            case "manh" -> solveManh();
        }
    }

    public void solveHamm() {
        if (stats.checkAndSaveStats(this.currentBoard)) return;
        try {
            move();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.currentBoard = boardsToCheck.remove(0);
        if (this.boardsToCheck.size() != 0) {
            this.solveHamm();
        }
    }

    public void solveManh() {
        if (stats.checkAndSaveStats(this.currentBoard)) return;
        try {
            move();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.currentBoard = boardsToCheck.remove(0);
        if (this.boardsToCheck.size() != 0) {
            this.solveManh();
        }
    }

    private void move() throws CloneNotSupportedException {
        //move left
        if (!(currentBoard.getLastMove().equals("R")) && currentBoard.getEmptyY() != 0) {
            FifteenPuzzle moveL = currentBoard.clone();
            moveL.moveEmptyL();
            if (heuristic.equals("hamm")) {
                addToSortedListHamm(moveL);
            } else {
                addToSortedListManh(moveL);
            }
            stats.incrementProcessed();
        }
        //move right
        if (!(currentBoard.getLastMove().equals("L")) && currentBoard.getEmptyY() != columns - 1) {
            FifteenPuzzle moveR = currentBoard.clone();
            moveR.moveEmptyR();
            if (heuristic.equals("hamm")) {
                addToSortedListHamm(moveR);
            } else {
                addToSortedListManh(moveR);
            }
            stats.incrementProcessed();
        }
        //move down
        if (!(currentBoard.getLastMove().equals("U")) && currentBoard.getEmptyX() != rows - 1) {
            FifteenPuzzle moveD = currentBoard.clone();
            moveD.moveEmptyD();
            if (heuristic.equals("hamm")) {
                addToSortedListHamm(moveD);
            } else {
                addToSortedListManh(moveD);
            }
            stats.incrementProcessed();
        }
        //move up
        if (!(currentBoard.getLastMove().equals("D")) && currentBoard.getEmptyX() != 0) {
            FifteenPuzzle moveU = currentBoard.clone();
            moveU.moveEmptyU();
            if (heuristic.equals("hamm")) {
                addToSortedListHamm(moveU);
            } else {
                addToSortedListManh(moveU);
            }
            stats.incrementProcessed();
        }

    }

    private void addToSortedListHamm(FifteenPuzzle add) {
        boolean added = false;
        int size = boardsToCheck.size();
        add.setManhPlusIter(add.manhattan() + (add.getIterations() / 2));
        for (int i = 0; i < size; i++) {
            if (add.getManhPlusIter() < boardsToCheck.get(i).getManhPlusIter()) {
                boardsToCheck.add(i, add);
                added = true;
                break;
            }
        }
        if (!added) {
            boardsToCheck.add(add);
        }
    }

    private void addToSortedListManh(FifteenPuzzle add) {
        boolean added = false;
        int size = boardsToCheck.size();
        add.setManhPlusIter(add.manhattan() + (add.getIterations() / 2));
        for (int i = 0; i < size; i++) {
            if (add.getManhPlusIter() <= boardsToCheck.get(i).getManhPlusIter()) {
                boardsToCheck.add(i, add);
                added = true;
                break;
            }
        }
        if (!added) {
            boardsToCheck.add(add);
        }
    }
}
