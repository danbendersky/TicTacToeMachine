import java.lang.Math;

public class MachineControl {

    //This is a very important control: it adjusts whether you see how the machine trains, test by test.
    //By default, it is set to off for a better training experience
    public static final boolean seeTraining = false;

    //Another important control is the number of game tests we want to run
    public static final int maxTestCount = 1000000;
    
    //Keeps track of how many times the algorithm has run
        private static int testCount = 0;
        private static boolean isMachineGame = true;
        
        public static void addTest(){
            testCount++;
        }
        
        public static int getCount(){
            return testCount;
        }

        
    //Move picking algorithm: the further in the machine's development (more tests),
    // the less random the moves become and the more accurate they are for picking the strongest developed move
    //You can adjust these intervals to make the machine train for longer, or adopt slightly different methods
        public static int movePicker(){
            int output;
            if (testCount > maxTestCount){
                isMachineGame = false;
                output = 0;
            } else if (testCount > (maxTestCount * 19 / 20)){
                output = random(0, 1);
            } else if (testCount > (maxTestCount * 9 / 10)){
                output = random(0, 2);
            } else if (testCount > (maxTestCount * 7 / 10)){
                output = random(0, 3);
            } else if (testCount > (maxTestCount * 13 / 20)){
                output = random(0, 4);
            } else if (testCount > (maxTestCount * 3 / 5)){
                output = random(0, 5);
            } else if (testCount > (maxTestCount * 11 / 20)){
                output = random(0, 6);
            } else if (testCount > (maxTestCount / 2)){
                output = random(0, 7);
            } else {
                output = random(0, 8);
            }
            return output;
        }
        
    //Random int with range generator
        public static int random(int min, int max){
            int range = (max - min) + 1;     
            return (int)(Math.random() * range) + min;
        }

        public static boolean isMachineGame(){
            return isMachineGame;
        }
}