import java.util.Arrays;

public class TicTacToe
{
   private String[][] board = new String[3][3];
   private int turn;

    public TicTacToe(){
        for (String[] strings : board) {
            Arrays.fill(strings, "-");
        }
        turn = 0;
    }
    
    public void reset(){
        for (String[] strings : board) {
            Arrays.fill(strings, "-");
        }
        turn = 0;
    }
    
    public String[][] getCurrentBoard(){
        return board;
    }
    
   
   //this method returns the current turn
   public int getTurn()
   {
        return turn;
   }
   
   /*This method prints out the board array on to the console
   */
   public static void printBoard(String[][] board)
   {
       System.out.println("  0 1 2");
       
       System.out.print("0 ");
       for (String spot : board[0]){
           System.out.print(spot + " ");
       }
       System.out.println();
       
       System.out.print("1 ");
       for (String spot : board[1]){
           System.out.print(spot + " ");
       }
       System.out.println();
       
       System.out.print("2 ");
       for (String spot : board[2]){
           System.out.print(spot + " ");
       }
       System.out.println();
   }

    public static void printBoardForPlayer(String[][] board)
    {
        int counter = 1;
        System.out.print(" ");
        for (String spot : board[0]){
            if (spot.equals("-")){
                System.out.print(counter + " ");
            } else {
                System.out.print(spot + " ");
            }
            counter ++;
        }
        System.out.println();

        System.out.print(" ");
        for (String spot : board[1]){
            if (spot.equals("-")){
                System.out.print(counter + " ");
            } else {
                System.out.print(spot + " ");
            }
            counter ++;
        }
        System.out.println();

        System.out.print(" ");
        for (String spot : board[2]){
            if (spot.equals("-")){
                System.out.print(counter + " ");
            } else {
                System.out.print(spot + " ");
            }
            counter ++;
        }
        System.out.println();
    }
   
   //This method returns true if space row, col is a valid space
   public boolean pickLocation(int row, int col)
   {
       if ((row < 0 || row >= board.length) || (col < 0 || col >= board[0].length)){
           return false;
       }
       return ((!board[row][col].equals("X")) && (!board[row][col].equals("O")));
   }
   
   //This method places an X or O at location row,col based on the int turn
   public void takeTurn(int row, int col)
   {
       if (turn%2 == 0){
           board[row][col] = "X";
       } else {
           board[row][col] = "O";
       }
       turn++;
   }
   
   //This method returns a boolean that returns true if a row has three X or O's in a row
   public boolean checkRow()
   {
       boolean output = false;
       for (String[] strings : board) {
           int x = 0;
           int y = 0;
           for (String spot : strings) {
               if (spot.equals("X")) {
                   x++;
               }
               if (spot.equals("Y")) {
                   y++;
               }
           }
           if ((x >= 3) || (y >= 3)) {
               output = true;
               break;
           }
       }
       return output;
   }
   
    //This method returns a boolean that returns true if a col has three X or O's
   public boolean checkCol()
   {
       boolean output = false;
       for (int col=0; col<board[0].length; col++){
           int x = 0;
           int y = 0;
           for (String[] strings : board) {
               if (strings[col].equals("X")) {
                   x++;
               }
               if (strings[col].equals("Y")) {
                   y++;
               }
           }
           if ((x>=3) || (y>=3)){
               output = true;
               break;
           }
       }
       return output;
   }
   
    //This method returns a boolean that returns true if either diagonal has three X or O's
   public boolean checkDiag()
   {
       boolean output = false;
       int x = 0;
       int y = 0;
       for (int i=0; i<board.length; i++){
           if (board[i][i].equals("X")){
               x++;
           }
           if (board[i][i].equals("Y")){
               y++;
           }
           if (x>=3 || y>=3){
               output = true;
           }
       }
       x=0;
       y=0;
       for (int i=0; i<board.length; i++){
           if (board[2-i][i].equals("X")){
               x++;
           }
           if (board[2-i][i].equals("Y")){
               y++;
           }
           if (x>=3 || y>=3){
               output = true;
           }
       }
       
       return output;
   }
   
   //This method returns a boolean that checks if someone has won the game
   public boolean checkWin()
   {
       return checkDiag() || checkCol() || checkRow();
   }
}