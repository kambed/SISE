public class Main {
    public static void main(String[] args){
        String val = ArgsValidator.validate(args);
        if (val != null) {
            System.out.println(val);
            return;
        }
        int[][] start = {
                {5,3,7,1},
                {15,13,9,11},
                {2,8,6,10},
                {4,0,12,14}
        };
        int[][] solved = {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,0}
        };
        FifteenPuzzle fp = new FifteenPuzzle(start);
        //new depthFirstSolve(fp);
        new BreadthFirstSolve(fp);
        System.out.println("Finished");
    }
}
