import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FifteenPuzzle implements Cloneable {
    private int[][] board;
    private int emptyX;
    private int emptyY;
    private String lastMove;
    private int iterations = 0;
    private List<String> historyOfMoves = new ArrayList<>();

    public FifteenPuzzle(int[][] board) {
        this.lastMove = "FIRST";
        this.board = board;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    this.emptyX = i;
                    this.emptyY = j;
                }
            }
        }
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setHistoryOfMoves(List<String> historyOfMoves) {
        this.historyOfMoves = historyOfMoves;
    }

    public List<String> getHistoryOfMoves() {
        return historyOfMoves;
    }

    public int getEmptyX() {
        return emptyX;
    }

    public int getEmptyY() {
        return emptyY;
    }

    public String getLastMove() {
        return lastMove;
    }

    public int getIterations() {
        return iterations;
    }

    public void moveEmptyR() {
        this.board[emptyX][emptyY] = this.board[emptyX][emptyY + 1];
        this.board[emptyX][emptyY + 1] = 0;
        this.emptyY = emptyY + 1;
        iterations++;
        this.lastMove = "R";
        this.historyOfMoves.add("R");
    }

    public void moveEmptyL() {
        this.board[emptyX][emptyY] = this.board[emptyX][emptyY - 1];
        this.board[emptyX][emptyY - 1] = 0;
        this.emptyY = emptyY - 1;
        iterations++;
        this.lastMove = "L";
        this.historyOfMoves.add("L");
    }

    public void moveEmptyU() {
        this.board[emptyX][emptyY] = this.board[emptyX - 1][emptyY];
        this.board[emptyX - 1][emptyY] = 0;
        this.emptyX = emptyX - 1;
        iterations++;
        this.lastMove = "U";
        this.historyOfMoves.add("U");
    }

    public void moveEmptyD() {
        this.board[emptyX][emptyY] = this.board[emptyX + 1][emptyY];
        this.board[emptyX + 1][emptyY] = 0;
        this.emptyX = emptyX + 1;
        iterations++;
        this.lastMove = "D";
        this.historyOfMoves.add("D");
    }

    public boolean check() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    return true;
                }
                if (board[i][j] != j + i * 4 + 1) return false;
            }
        }
        return true;
    }

    public FifteenPuzzle clone() throws CloneNotSupportedException {
        FifteenPuzzle clone = (FifteenPuzzle) super.clone();
        int[][] board = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = this.board[i][j];
            }
        }
        List<String> moves = new ArrayList<>(getHistoryOfMoves());
        clone.setHistoryOfMoves(moves);
        clone.setBoard(board);
        return clone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] > 9) {
                    sb.append("[" + board[i][j] + "]");
                }
                if (board[i][j] <= 9) {
                    sb.append("[ " + board[i][j] + "]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
