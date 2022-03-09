public class Stats {
    private int boardsVisited = 0;
    private int boardsProcessed = 0;
    private int maxRecursionLevel = 0;
    private long start = System.nanoTime();

    public boolean checkAndDisplayStats(FifteenPuzzle currentBoard) {
        boardsVisited++;
        if (maxRecursionLevel < currentBoard.getIterations()) {
            maxRecursionLevel = currentBoard.getIterations();
        }
        if (currentBoard.check()) {
            System.out.println("Time: " + Math.round((System.nanoTime() - start)/1000.0)/1000.0 + "ms");
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
