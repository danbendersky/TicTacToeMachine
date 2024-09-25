import javax.sound.midi.SysexMessage;
import java.util.Scanner;

public class TicTacToeMachine
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Move currentMove = null;
        
        //Important note: X always go first, and O second.
        Machine x = new Machine();
        Machine o = new Machine();
        
        
        //Generate clean board
        TicTacToe gameBoard = new TicTacToe();
        
        //Main game loop runs until we choose that a machine turns off, time is an estimation of number of tests
        System.out.println("Please wait, machine is currently training. This should take around " + (MachineControl.maxTestCount / 20000) + " seconds.");

        //Start runtime stopwatch
        final long startTime = System.currentTimeMillis();

        while (MachineControl.isMachineGame()){
        //Reset board
        gameBoard.reset();
        System.out.print("\rThe current runtime is " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds with " + MachineControl.getCount() + " tests run so far");
        
        //Run a full game (repeat turns until win or draw)
        while ((!gameBoard.checkWin()) && (gameBoard.getTurn() < 9)){
            
            //Print gameboard
            if (MachineControl.seeTraining) System.out.println("\nCurrent gameboard:");
            if (MachineControl.seeTraining) TicTacToe.printBoard(gameBoard.getCurrentBoard());
            
            if (gameBoard.getTurn() % 2 == 0){
                if (MachineControl.seeTraining) System.out.println("\nPicking move for x");
                currentMove = x.getMove(gameBoard.getCurrentBoard());
            } else {
                if (MachineControl.seeTraining) System.out.println("\nPicking move for o");
                currentMove = o.getMove(gameBoard.getCurrentBoard());
            }
            
        //If location choice is valid
            if (gameBoard.pickLocation(currentMove.getRow(), currentMove.getCol())){
                
                //Add the move to an array of moves played this game for X or for O
                if (gameBoard.getTurn() % 2 == 0){
                    x.addStored(currentMove);
                } else {
                    o.addStored(currentMove);
                }
                
                //Make the move on the gameboard, increase turn count
                gameBoard.takeTurn(currentMove.getRow(), currentMove.getCol());
                
            } else {
                //Decrease the strength of that move so much that the algorithm will skip it in the future (not a possible move, wasted computing)
                if (gameBoard.getTurn() % 2 == 0){
                    x.invalidate(currentMove);
                } else {
                    o.invalidate(currentMove);
                }
            }
        }
        
        
        //If someone won the game:
        if (gameBoard.checkWin()) {

            //If it's X's turn, O went last and won
            if (gameBoard.getTurn() % 2 == 0) {
                if (MachineControl.seeTraining) System.out.println("O wins!");
                //Increase the strengths of O's moves:
                o.increaseStored();
                //And decrease the strength of X's moves:
                x.increaseStored();

                //If it's O's turn, X went last and won
            } else {
                if (MachineControl.seeTraining) System.out.println("X wins!");
                //Increase the strengths of X's moves:
                x.increaseStored();
                //And decrease the strengths of O's moves:
                o.decreaseStored();
            }
        } else {
            //If there's a draw, nothing happens, other than moves being logged! Yay!
            x.logDrawGame();
            o.logDrawGame();
        }
            //Then reset both storages
            if (MachineControl.seeTraining) System.out.println("Reset stored");
            x.resetStored();
            o.resetStored();

        //Count the game test when over
            if (MachineControl.seeTraining) System.out.println("Add test count");
        MachineControl.addTest();
    }

        //When machine is done training, start the actual game (player vs machine)
        int playerMoveChoice = 1;
        System.out.println("Machine is done training now.");
        System.out.println("\nWelcome to Tic-Tac-Toe!");
        System.out.println("To quit the entire game, enter -1 as a move choice when prompted");
        //Run games until player decides to terminate program
        gameLoop: while (true) {
            gameBoard.reset();
            String playerChoice = "";
            System.out.println("Would you like to play as X or O?");
            System.out.println("X always goes first.");
            while (!(playerChoice.equals("o") || (playerChoice.equals("x")))) {
                System.out.print("Type 'x' for X, and 'o' for o: ");
                playerChoice = input.nextLine();
                if (!(playerChoice.equals("o") || (playerChoice.equals("x")))) {
                    System.out.println("Please enter a valid choice.");
                }
            }
            System.out.println(" You will play against a bot as " + playerChoice);

            //Run a full game (repeat turns until win or draw)
            turnLoop: while ((!gameBoard.checkWin()) && (gameBoard.getTurn() < 9)){

                //Print gameboard
                System.out.println("\nCurrent game board:");
                TicTacToe.printBoardForPlayer(gameBoard.getCurrentBoard());

                //If even turn
                if (gameBoard.getTurn() % 2 == 0){
                    if (playerChoice.equals("o")){

                        //Make the move on the gameboard, increase turn count
                        System.out.println("\nPicking move for x");
                        currentMove = x.getMove(gameBoard.getCurrentBoard());
                        gameBoard.takeTurn(currentMove.getRow(), currentMove.getCol());
                    } else {
                        inputLoop: while (true){
                        System.out.print("Pick a move for x by entering a number in the grid shown above: ");
                        playerMoveChoice = input.nextInt();
                        input.nextLine();
                        if (playerMoveChoice == -1) {
                            break gameLoop;
                        } else if (!gameBoard.pickLocation(MoveTranslator.translateToRow(playerMoveChoice), MoveTranslator.translateToColumn(playerMoveChoice))){
                            System.out.println("That's not a valid index. Please enter a valid index.");
                        } else {
                            gameBoard.takeTurn(MoveTranslator.translateToRow(playerMoveChoice), MoveTranslator.translateToColumn(playerMoveChoice));
                            if (gameBoard.checkWin()){
                                break turnLoop;
                            }
                            break inputLoop;
                        }
                        }
                    }

                //If odd turn
                } else {
                    if (playerChoice.equals("x")){
                        System.out.println("\nPicking move for o");

                        //Make the move on the gameboard, increase turn count
                        currentMove = o.getMove(gameBoard.getCurrentBoard());
                        gameBoard.takeTurn(currentMove.getRow(), currentMove.getCol());
                    } else {
                        inputLoop: while (true) {
                            System.out.print("Pick a move for o by entering a number in the grid shown above: ");
                            playerMoveChoice = input.nextInt();
                            input.nextLine();
                            if (playerMoveChoice == -1) {
                                break gameLoop;
                            } else if (!gameBoard.pickLocation(MoveTranslator.translateToRow(playerMoveChoice), MoveTranslator.translateToColumn(playerMoveChoice))){
                                System.out.println("That's not a valid index. Please enter a valid index.");
                            } else {
                                gameBoard.takeTurn(MoveTranslator.translateToRow(playerMoveChoice), MoveTranslator.translateToColumn(playerMoveChoice));
                                if (gameBoard.checkWin()){
                                    break turnLoop;
                                }
                                break inputLoop;
                            }
                        }
                    }
                }
            }

            TicTacToe.printBoardForPlayer(gameBoard.getCurrentBoard());
            //If someone won the game:
            if (gameBoard.checkWin()) {

                //If it's X's turn, O went last and won
                if (gameBoard.getTurn() % 2 == 0) {
                    System.out.println("O wins!");

                    //If it's O's turn, X went last and won
                } else {
                    System.out.println("X wins!");
                }
            } else {
                //If there's a draw, nothing happens! Yay!
                System.out.println("This game was a draw.");
            }
        }

        //Game over
        System.out.println("Goodbye, thanks for playing!");
    }
}