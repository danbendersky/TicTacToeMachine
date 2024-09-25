import java.util.Arrays;

public class Machine {

        private String[][] emptyBoard = new String[3][3];
        private GameState[] brain = new GameState[5478];
        private int indexMarker;
        public void printSavedBoards(){
            /*
            System.out.println("\nPrinting all saved boards:\n");
            for (int i=0; i<=indexMarker; i++){
                TicTacToe.printBoard(brain[i].getBoard());
                System.out.println("");
            }
            System.out.println("Done printing all saved boards\n");
             */
        }

        public Machine(){
            indexMarker = 0;
            if (MachineControl.seeTraining) System.out.println("indexMarker reset");

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    emptyBoard[row][col] = "-";
                }
            }

            for (int i = 0; i < 5478; i++){
                brain[i] = new GameState(emptyBoard);
            }
        }
        
        private int getIndexMarker(){
            // System.out.println("Index marker retrieved. Value = " + indexMarker);
            return indexMarker;
        }
        
        private void increaseIndexMarker(){
            this.indexMarker = indexMarker + 1;
            if (MachineControl.seeTraining) System.out.println("indexMarker increase checkpoint: " + indexMarker);
        }
        
        private int laststate;
        
        public Move getMove(String[][] board){
            int index = -1;
            //Search for matching board in brain
            printSavedBoards();
            for (int i=0; i<=this.getIndexMarker(); i++){
                //Check match
                //System.out.println("Checking match between target board");
                //TicTacToe.printBoard(board);
                //System.out.println("And board at index " + i + " :");
                //TicTacToe.printBoard(brain[i].getBoard());
                if (Arrays.deepEquals(board, brain[i].getBoard())){
                    //If matches, set index = index of match
                    index = i;
                    //System.out.println("Board search found match at index " + i);
                    //Break out of search
                    break;
                }
            }
            //If board isn't found in saved boards, create a new board and save its index
            if (index == -1){
                this.increaseIndexMarker();
                if (MachineControl.seeTraining) System.out.println("Increased index marker to " + this.getIndexMarker());
                if (MachineControl.seeTraining) System.out.println("Adding new board ");
                if (MachineControl.seeTraining) TicTacToe.printBoard(board);
                if (MachineControl.seeTraining) System.out.println("at index " + this.getIndexMarker());

                //Deep copying the board to avoid pass reference errors
                String[][] tempBoard = new String[3][3];
                for (int i = 0; i < board.length; i++){
                    for (int j = 0; j < board[i].length; j++){
                        tempBoard[i][j] = board[i][j];
                    }
                }
                brain[this.getIndexMarker()].setBoard(tempBoard);

                index = this.getIndexMarker();
                printSavedBoards();
            }
            //Save the index of the board for outside purposes
            laststate = index;
            
            //Return a picked move from the board
            return brain[index].pickMove();
        }
        
        //A storage space for the last gamestate and move played in case its invalid
        
        public void invalidate(Move move){
            for (int i=0; i<9; i++){
                if (brain[laststate].getMove(i).equals(move)){
                    move.setNotValid();
                    brain[laststate].setMove(i, move);
                    if (MachineControl.seeTraining) System.out.println("Move at index " + i + " now has strength " + brain[laststate].getMove(i).getStrength());
                    if (MachineControl.seeTraining) System.out.println("Move invalidated at brain [" + laststate + "] with move index " + i);

                }
            }
            //Reset laststate
            laststate = -1;
        }
        
        
        
        //An array of game states and moves played this game ([index, GameState index, moverow, movecolumn])
        private int[] gameStates = new int[5];
        private int[] storedMoves = new int[5];
        private int storageMarker = 0;
        
        public void addStored(Move move){
            //printSavedBoards();
            gameStates[storageMarker] = laststate;
            //printSavedBoards();

            //Deep copying the move to avoid pass reference errors
            Move tempMove;
            tempMove = new Move(move.getRow(), move.getCol(), move.getRawStrength(), move.getTimesPlayed(), move.getIsValid());

            storedMoves[storageMarker] = brain[laststate].getMoveIndex(tempMove);
            //printSavedBoards();
            if (MachineControl.seeTraining) System.out.println("Added move to storage with board brain index " + laststate + " and move index " + storedMoves[storageMarker] + " at storage index " +  storageMarker);
            laststate = 0;
            storageMarker ++;
        }
        
        public void resetStored(){
            if (MachineControl.seeTraining) System.out.println("reset stored move indexes");
            for(int i=0; i<5; i++){
                gameStates[i] = -1;
            }
            storageMarker = 0;
        }
        
        public void decreaseStored(){
            for (int i = 0; i<5; i++){
                if (gameStates[i] >= 0){
                    //Temporarily store the move
                    Move tempStorage = brain[gameStates[i]].getMove(storedMoves[i]);
                    
                    //Decrease its strength
                    tempStorage.redStrength();
                    
                    //Replace the storage location of the move with the new value
                    brain[gameStates[i]].setMove(storedMoves[i], tempStorage);
                    
                    if (MachineControl.seeTraining) System.out.println("Reduced move in brain[" + gameStates[i] + "] with move index " + storedMoves[i]);
                }
            }
        }
        
        public void increaseStored(){
            for (int i = 0; i<5; i++){
                if (gameStates[i] >= 0){
                    //Temporarily store the move
                    Move tempStorage = brain[gameStates[i]].getMove(storedMoves[i]);
                    
                    //Increase its strength
                    tempStorage.incStrength();
                    
                    //Replace the storage location of the move with the new value
                    brain[gameStates[i]].setMove(storedMoves[i], tempStorage);
                    
                    if (MachineControl.seeTraining) System.out.println("Increased move in brain[" + gameStates[i] + "] with move index " + storedMoves[i]);
                }
            }
        }

    public void logDrawGame(){
        for (int i = 0; i<5; i++){
            if (gameStates[i] >= 0){
                //Temporarily store the move
                Move tempStorage = brain[gameStates[i]].getMove(storedMoves[i]);

                //Increase its strength
                tempStorage.incTimesPlayed();

                //Replace the storage location of the move with the new value
                brain[gameStates[i]].setMove(storedMoves[i], tempStorage);

                if (MachineControl.seeTraining) System.out.println("Logged move in brain[" + gameStates[i] + "] with move index " + storedMoves[i]);
            }
        }
    }
        
        public void printOutThis2d(String[][] input){
            for (int row = 0; row < input.length; row++){
                for (int col = 0; col < input[row].length; col++){
                    System.out.println("At index " + row + ", " + col + " value is " + input[row][col]);
                }
            }
        }
}