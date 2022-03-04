import java.util.Arrays;
import java.util.List;

public class ArgsValidator {
    public static String validate(String[] args) {
        String result = null;
        if (args.length != 5) return "Invalid number of arguments";
        List<String> strategy = Arrays.asList("bfs","dfs","astr");
        if (!strategy.contains(args[0])) return "Invalid strategy parameter (bfs,dfs,astr)";
        if (args[0].equals("astr")) {
            List<String> heuristic = Arrays.asList("hamm","manh");
            if (!heuristic.contains(args[1])) return "Invalid heuristic for astr (manh,hamm)";
        }
        if (args[0].equals("bfs") || args[0].equals("dfs")) {
            List<String> order = Arrays.asList("LRUD","RLUD","ULRD","LURD","RULD","URLD",
                    "URDL","RUDL","DURL","UDRL","RDUL","DRUL",
                    "DLUR","LDUR","UDLR","DULR","LUDR","ULDR",
                    "RLDU","LRDU","DRLU","RDLU","LDRU","DLRU");
            if (!order.contains(args[1])) return "Invalid order (permutation of LRUD)";
        }
        if (!args[3].endsWith(".txt")) return "Third argument is not txt file";
        if (!args[4].endsWith(".txt")) return "Forth argument is not txt file";
        if (!args[5].endsWith(".txt")) return "Fifth argument is not txt file";
        return result;
    }
}
