public class FifteenPuzzle implements Cloneable{
    int[][] board;
    int emptyX;
    int emptyY;

    public FifteenPuzzle(int[][] board) {
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

    public void moveEmptyR() {
        this.board[emptyX][emptyY] = this.board[emptyX+1][emptyY];
        this.board[emptyX+1][emptyY] = 0;
    }

    public void moveEmptyL() {
        this.board[emptyX][emptyY] = this.board[emptyX-1][emptyY];
        this.board[emptyX-1][emptyY] = 0;
    }

    public void moveEmptyU() {
        this.board[emptyX][emptyY] = this.board[emptyX][emptyY-1];
        this.board[emptyX][emptyY-1] = 0;
    }

    public void moveEmptyD() {
        this.board[emptyX][emptyY] = this.board[emptyX][emptyY+1];
        this.board[emptyX][emptyY+1] = 0;
    }

    public boolean check() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != i+j*4+1) return false;
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
        clone.setBoard(board);
        return clone;
    }
}
