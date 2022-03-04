public class Main {
    public static void main(String[] args){
        String val = ArgsValidator.validate(args);
        if (val != null) {
            System.out.println(val);
            return;
        }
    }
}
