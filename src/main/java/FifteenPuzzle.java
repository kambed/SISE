import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FifteenPuzzle implements Cloneable {
    private byte[][] board;
    private byte emptyX;
    private byte emptyY;
    private String lastMove;
    private int iterations = 0;
    private int manhPlusIter = 0;
    private List<String> historyOfMoves = new ArrayList<>();
    private int rows = 0;
    private int columns = 0;

    public FifteenPuzzle() {
    }

    public FifteenPuzzle(byte[][] board, int rows, int columns) {
        this.lastMove = "X";
        this.board = board;
        this.rows = rows;
        this.columns = columns;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == 0) {
                    this.emptyX = (byte) i;
                    this.emptyY = (byte) j;
                }
            }
        }
    }

    public int getManhPlusIter() {
        return manhPlusIter;
    }

    public void setManhPlusIter(int manhPlusIter) {
        this.manhPlusIter = manhPlusIter;
    }

    public void setBoard(byte[][] board) {
        this.board = board;
    }

    public void setHistoryOfMoves(List<String> historyOfMoves) {
        this.historyOfMoves = historyOfMoves;
    }

    public List<String> getHistoryOfMoves() {
        return historyOfMoves;
    }

    public byte getEmptyX() {
        return emptyX;
    }

    public byte getEmptyY() {
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
        this.emptyY = (byte) (emptyY + 1);
        iterations++;
        this.lastMove = "R";
        this.historyOfMoves.add("R");
    }

    public void moveEmptyL() {
        this.board[emptyX][emptyY] = this.board[emptyX][emptyY - 1];
        this.board[emptyX][emptyY - 1] = 0;
        this.emptyY = (byte) (emptyY - 1);
        iterations++;
        this.lastMove = "L";
        this.historyOfMoves.add("L");
    }

    public void moveEmptyU() {
        this.board[emptyX][emptyY] = this.board[emptyX - 1][emptyY];
        this.board[emptyX - 1][emptyY] = 0;
        this.emptyX = (byte) (emptyX - 1);
        iterations++;
        this.lastMove = "U";
        this.historyOfMoves.add("U");
    }

    public void moveEmptyD() {
        this.board[emptyX][emptyY] = this.board[emptyX + 1][emptyY];
        this.board[emptyX + 1][emptyY] = 0;
        this.emptyX = (byte) (emptyX + 1);
        iterations++;
        this.lastMove = "D";
        this.historyOfMoves.add("D");
    }

    public boolean check() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == rows - 1 && j == columns - 1) {
                    return true;
                }
                if (board[i][j] != j + i * columns + 1) return false;
            }
        }
        return true;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == rows - 1 && j == columns - 1) {
                    if (board[i][j] != 0) hamming++;
                    return hamming;
                }
                if (board[i][j] != j + i * columns + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        int desiredX;
        int desiredY;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == 0) {
                    desiredX = rows - 1;
                    desiredY = columns - 1;
                } else {
                    desiredX = (board[i][j] - 1) / columns;
                    desiredY = (board[i][j] - 1) % columns;
                }
                manhattan = manhattan + Math.abs(i - desiredX) + Math.abs(j - desiredY);
            }
        }
        return manhattan;
    }

    public FifteenPuzzle clone() throws CloneNotSupportedException {
        FifteenPuzzle clone = (FifteenPuzzle) super.clone();
        byte[][] board = new byte[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FifteenPuzzle that = (FifteenPuzzle) o;
        return Arrays.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
