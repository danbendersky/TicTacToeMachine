public class GameState {
    
        private String[][] board = new String[3][3];
        
        //create moveSet
        private Move[] moveSet = new Move[9];
        
        /* Move index corresponds to board in the following way:
        0 1 2
        3 4 5
        6 7 8
        */
        
        public GameState(){
            //Populate moveSet with all possible moves
            int i = 0;
            for (int row=0; row<=2; row++){
                for (int col=0; col<=2; col++){
                    moveSet[i] = new Move(row, col);
                    i++;
                }
            }
        }
        
        public GameState(String[][] boardInput){
            //Populate moveSet with all possible moves
            int i = 0;
            for (int row=0; row<=2; row++){
                for (int col=0; col<=2; col++){
                    moveSet[i] = new Move(row, col);
                    i++;
                }
            }
            
            //Set board
            this.board = boardInput;
        }
        
        public void setBoard(String[][] boardInput){
            this.board = boardInput;
        }
        
        public String[][] getBoard(){
            //System.out.println("Getting Board: ");
            //TicTacToe.printBoard(board);
            return board;
        }
        
        public Move getMove(int id){
            return moveSet[id];
        }
        
        public void setMove(int id, Move move){
            moveSet[id] = move;
        }
        
        public int getMoveIndex(Move move){
            for (int i = 0; i<moveSet.length; i++){
                if ((moveSet[i].getCol() == move.getCol()) && (moveSet[i].getRow() == move.getRow())){
                    return i;
                }
            }
            return -1;
        }
        
        //Pick a move based on move strength and our machine algorithm
        public Move pickMove(){
            Move[] sortedMoves = sortMoves(moveSet);
            int attemptCounter = 0;
            while (attemptCounter < 200){
                Move output = sortedMoves[MachineControl.movePicker()];
                if (output.getIsValid()){
                    return output;
                }
                attemptCounter++;
            }
            for (int i = sortedMoves.length - 1; i >= 0; i--) {
                Move output = sortedMoves[i];
                if (output.getIsValid()) {
                    return output;
                }
            }

            return null;
        }
        
        //Sort moveSet by decreasing move strength
        public Move[] sortMoves(Move[] moveSet){
            for (int i=0; i<moveSet.length; i++){
                float max = moveSet[i].getStrength();
                Move temp = moveSet[i];
                int id = i;
                for (int x=i+1; x<moveSet.length; x++){
                    if (moveSet[x].getStrength() > max){
                        max = moveSet[x].getStrength();
                        id = x;
                        temp = moveSet[x];
                    }
                }
                moveSet[id] = moveSet[i];
                moveSet[i] = temp;
            }
            return moveSet;
        }
}