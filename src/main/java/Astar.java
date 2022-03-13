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

    public Astar(FifteenPuzzle currentBoard, String heuristic, Path solutionPath, Path statsPath) {
        stats = new Stats(solutionPath, statsPath);
        this.currentBoard = currentBoard;
        this.heuristic = heuristic;
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

//        System.out.println(currentBoard);
//        System.out.println(currentBoard.manhattan() + currentBoard.getIterations()/4);
//        Scanner s = new Scanner(System.in);
//        int t = s.nextInt();

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
            //if (!hashCodesProcessed.contains(moveL.hashCode())) {
                if (heuristic.equals("hamm")) {
                    addToSortedListHamm(moveL);
                } else {
                    addToSortedListManh(moveL);
                }
//                hashCodesProcessed.add(moveL.hashCode());
//            }
            stats.incrementProcessed();
        }
        //move right
        if (!(currentBoard.getLastMove().equals("L")) && currentBoard.getEmptyY() != 3) {
            FifteenPuzzle moveR = currentBoard.clone();
            moveR.moveEmptyR();
            //if (!hashCodesProcessed.contains(moveR.hashCode())) {
                if (heuristic.equals("hamm")) {
                    addToSortedListHamm(moveR);
                } else {
                    addToSortedListManh(moveR);
                }
//                hashCodesProcessed.add(moveR.hashCode());
//            }
            stats.incrementProcessed();
        }
        //move down
        if (!(currentBoard.getLastMove().equals("U")) && currentBoard.getEmptyX() != 3) {
            FifteenPuzzle moveD = currentBoard.clone();
            moveD.moveEmptyD();
            //if (!hashCodesProcessed.contains(moveD.hashCode())) {
                if (heuristic.equals("hamm")) {
                    addToSortedListHamm(moveD);
                } else {
                    addToSortedListManh(moveD);
                }
//                hashCodesProcessed.add(moveD.hashCode());
//            }
            stats.incrementProcessed();
        }
        //move up
        if (!(currentBoard.getLastMove().equals("D")) && currentBoard.getEmptyX() != 0) {
            FifteenPuzzle moveU = currentBoard.clone();
            moveU.moveEmptyU();
            //if (!hashCodesProcessed.contains(moveU.hashCode())) {
                if (heuristic.equals("hamm")) {
                    addToSortedListHamm(moveU);
                } else {
                    addToSortedListManh(moveU);
                }
//                hashCodesProcessed.add(moveU.hashCode());
//            }
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
