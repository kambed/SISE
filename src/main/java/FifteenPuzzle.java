import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FifteenPuzzle implements Cloneable {
    private byte[][] board;
    private byte emptyX;
    private byte emptyY;
    private String lastMove;
    private byte iterations = 0;
    private List<String> historyOfMoves = new ArrayList<>();

    public FifteenPuzzle() {
    }

    public FifteenPuzzle(byte[][] board) {
        this.lastMove = "X";
        this.board = board;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    this.emptyX = (byte) i;
                    this.emptyY = (byte) j;
                }
            }
        }
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

    public byte getIterations() {
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

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    if (board[i][j] != 0) hamming++;
                    return hamming;
                }
                if (board[i][j] != j + i * 4 + 1) {
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    desiredX = 3;
                    desiredY = 3;
                } else {
                    desiredX = (board[i][j] - 1) / 4;
                    desiredY = (board[i][j] - 1) % 4;
                }
                manhattan = manhattan + Math.abs(i - desiredX) + Math.abs(j - desiredY);
            }
        }
        return manhattan;
    }

    public FifteenPuzzle clone() throws CloneNotSupportedException {
        FifteenPuzzle clone = (FifteenPuzzle) super.clone();
        byte[][] board = new byte[4][4];
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
