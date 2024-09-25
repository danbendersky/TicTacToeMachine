public class MoveTranslator {
    public static int translateToRow(int input){
        return (input - 1) / 3;
    }

    public static int translateToColumn(int input){
        return (input - 1) % 3;
    }
}