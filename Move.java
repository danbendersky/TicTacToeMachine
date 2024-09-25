public class Move {
        private int row;
        private int col;
        private int strength;
        private int timesPlayed;
        private boolean isValid;

        public Move(int row, int col){
            this.strength = 0;
            this.row = row;
            this.col = col;
            timesPlayed = 0;
            isValid = true;
        }

        public Move(int row, int col, int strength, int timesPlayed, boolean isValid){
            this.row = row;
            this.col = col;
            this.strength = strength;
            this.timesPlayed = timesPlayed;
            this.isValid = isValid;
        }
        
        public int getRow(){
            return row;
        }
        
        public int getCol(){
            return col;
        }
        
        public float getStrength(){
            if (timesPlayed == 0){
                return 0;
            }
            return (float) ((strength * strength) / timesPlayed);
        }
        
        public void incStrength(){
            strength ++;
            timesPlayed ++;
        }
        
        public void redStrength() {
            strength --;
            timesPlayed ++;
        }

        public int getTimesPlayed(){
            return timesPlayed;
        }

        public void incTimesPlayed(){
            timesPlayed++;
        }

        public int getRawStrength(){
            return strength;
        }
        
        public void setStrength(int strength){
            this.strength = strength;
        }

        public boolean getIsValid(){
            return isValid;
        }

        public void setNotValid(){
            isValid = false;
        }
        
}