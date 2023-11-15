import java.util.Arrays;
public class RunSimulation{

	/** You should probably keep these booleans to true for one game,
	 *  but if you are simulating hundreds or thousands of games, you probably want
	 *  them set to false
	 ******************************************************************  
	 *  To set the guesser to be your guessing code, see lines 50-58  *
	 ******************************************************************
	 */
	static boolean showAll = true;
	static boolean showInitialShips = showAll; // whether or not to see initial placement of ships
	static boolean showBoardEveryMove = showAll; // whether or not to show the guess board after every move
	static boolean showAllGuessesAtEnd = showAll; // whether or not show all the guesses at the end of simulation
	static int numIterations = 1; // number of games to simulate

	public static void main (String[] args) {
		double sum = 0;

		System.out.println("Performing " + numIterations + " simulations...");
		for (int i =0; i<numIterations; i++) {
			sum += play();
		}
		System.out.println("Average number of turns was: " + (sum/numIterations));
	}

	public static int play(){
		final int DIMENSIONS = 10;
		char[][] s1Guesses = new char[DIMENSIONS][DIMENSIONS];
		String[] history = new String[DIMENSIONS * DIMENSIONS];

		for(int row = 0; row < DIMENSIONS; row++){
			for(int col = 0; col < DIMENSIONS; col++){
				s1Guesses[row][col] = '.';
			}
		}

		int[][] gameBoard = new int[DIMENSIONS][DIMENSIONS];
		BattleShipTools.randomBoard(gameBoard);
		//BattleShipTools.problemBoard(gameBoard);

		// Prints the board
		if (showInitialShips) {
			BattleShipTools.printBoard(gameBoard);
		}
		int moves;

		for(moves = 1; moves < DIMENSIONS*DIMENSIONS+1; moves++){
			//copy array before passing to students
			char[][] s1Copy = Arrays.copyOf(s1Guesses, s1Guesses.length);

			/**********************************************
			 * CHANGE "RandomGuesser" to your class name. *
			 * Your class must have a 'makeGuess' method  *
			 * 		that returns a String				  * 
			 **********************************************/
			String guess1 = Ghost.makeGuess(s1Copy);
			history[moves-1] = guess1;

			boolean p1 = BattleShipTools.updateGuess(s1Guesses,guess1,gameBoard);
			//break if the player won
			if(p1){
				break;
			}
			if (showBoardEveryMove) {
				BattleShipTools.printBoard(s1Guesses);
				System.out.println();
			}
		}

		if(moves == DIMENSIONS * DIMENSIONS + 1){

			System.out.println("Out of Moves");

			for(int i = 0; i < history.length; i++){
				if(i % 16 == 0)
					System.out.println();
				if(history[i] != null){
					System.out.print(history[i] + ", ");
				}

			}
			//BattleShipTools.printBoard(s1Guesses);
		}


		if (showAllGuessesAtEnd) {

			for(int i = 0; i < history.length; i++){
				if(i % 16 == 0)
					System.out.println();
				if(history[i] != null){
					System.out.print(history[i] + ", ");
				}

			}

			System.out.println();
		}

		// Final Guess board
		//BattleShipTools.printBoard(s1Guesses);

		// instead of outputting, return the int
		//System.out.println("Completed in " + moves + " moves");
		return moves;



	}

}