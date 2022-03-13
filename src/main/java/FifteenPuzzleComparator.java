import java.util.Comparator;

public class FifteenPuzzleComparator implements Comparator<FifteenPuzzle> {
    @Override
    public int compare(FifteenPuzzle o1, FifteenPuzzle o2) {
        return o1.manhattan() - o2.manhattan();
    }
}
