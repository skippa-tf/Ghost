public class Ghost {
    public static String makeGuess(char[][] guesses) {
        /**
         * Main method for running Ghost.
         * @param guesses 10x10 grid describing the current state of the game.
         *                '.' describes an unguessed spot.
         *                'X' describes a hit.
         *                'O' describes a miss.
         *                '1' - '5' describes a sunk boat. Each number describes a different boat.
         * @return A string of coordinates to describe where to guess next, in the format "A1" - "J10".
         */

        int[][] probabilityGrid = new int[guesses.length][guesses[0].length];
        int[] bestSquare = {0, 0};
        int[] boatsAlive = {0, 2, 3, 3, 4, 5}; // Keep track of boats. 0: sunken boat / placeholder. i = 1: patrol boat (size 2). i = 2: submarine (size 3). etc.
                                               // First value is a placeholder to make indexes easier.
        
        // First fill out the alive boats in order to calculate probability accurately while considering the entire board.
        for (int row = 0; row < probabilityGrid.length; row++) {
            for (int col = 0; col < probabilityGrid[row].length; col++) {
                char currentChar = guesses[row][col];
                if (currentChar != '.' && currentChar != 'X' && currentChar != 'O') {
                    for (int i = 1; i < boatsAlive.length; i++) {
                        if (i == currentChar - '0') {
                            boatsAlive[i] = 0;
                        }
                    }
                }
            }
        }
        
        // Fill out the entire probability grid to ensure our guess considers every square fairly.
        for (int row = 0; row < probabilityGrid.length; row++) {
            for (int col = 0; col < probabilityGrid[row].length; col++) {
                char currentChar = guesses[row][col];
                if (currentChar != '.'){ // Make sure we don't guess the same spot twice.
                    probabilityGrid[row][col] = -1000000;
                } else {                 // Calculate the probability of the current square.
                    probabilityGrid[row][col] += getProbability(row, col, guesses, boatsAlive);
                }
            }
        }

        // Decide which square to guess
        for (int row = 0; row < probabilityGrid.length; row++) {
            for (int col = 0; col < probabilityGrid[row].length; col++) {
                int[] currentCoords = {row, col};
                // Update the best square
                bestSquare = whichBetter(bestSquare, currentCoords, probabilityGrid, boatsAlive);
            }
        }

        boolean debugging = false; // Set to true to show the probability grid.
        if (debugging) {
            System.out.println("Printing probability grid: ");
            BattleShipTools.printBoard(probabilityGrid);
            System.out.println("-----");
            System.out.println("Boats alive: ");
            for (int i = 1; i < boatsAlive.length; i++) {
                System.out.print(boatsAlive[i] + " ");
            }
            System.out.println();
		}

        // Guess the highest probability
        return stringCoords(bestSquare[0], bestSquare[1]);
    }

    /**
     * This method decides the greatest probability square to guess.
     * @param currentBest   The coordinates of the current best guess.
     * @param challenger    The coordinates of the challenger (the current square that's being checked).
     * @param probabilityGrid   The probability grid to use to make the checks.
     * @param boatsAlive    The current ships alive.
     * @return The coordinates of the better square.
     */
    public static int[] whichBetter(int[] currentBest, int[] challenger, int[][] probabilityGrid, int[] boatsAlive) {
        int bestVal = probabilityGrid[currentBest[0]][currentBest[1]];
        int chalVal = probabilityGrid[challenger[0]][challenger[1]];
        if (bestVal > chalVal) {
            return currentBest;
        } else if (bestVal < chalVal) {
            return challenger;
        } else if (bestVal == chalVal) {
            // if the two are equal, hunt for the smallest boat alive.
            // This link helped with this concept: https://towardsdatascience.com/coding-an-intelligent-battleship-agent-bf0064a4b319
            for (int i = 1; i < boatsAlive.length; i++){
                int size = boatsAlive[i];
                if (size == 0) {
                    continue;
                }
                if (currentBest[0] + currentBest[1] % size == 0) {
                    return currentBest;
                } else if (challenger[0] + challenger[1] % size == 0) {
                    return challenger;
                }
            }
        }
        return currentBest;
    }

    /**
     * Method responsible for calculating the probability of the current square.
     * @param row The current square's row.
     * @param col The current square's col.
     * @param guesses Grid describing the state of the game.
     * @param boatsAlive Array of boats alive.
     * @return Probability of the current square.
     */
    public static int getProbability(int row, int col, char[][] guesses, int[] boatsAlive) {
		// Probability of the current square is affected by the number of X's on the row / column and 
        // how far those X's are from the current checked square
		int probability = 0;
		for (int i = 0; i < guesses[row].length; i++) {	// check the column
			if (guesses[row][i] == 'X'){
				probability += calcProbability(col, i);
			} 								
		}
		for (int i = 0; i < guesses.length; i++) { // check the row
			if (guesses[i][col] == 'X'){
				probability += calcProbability(row, i);
			} 								
		}
        // Devalue squares with adjacent misses.
        if (col - 1 >= 0 && guesses[row][col - 1] == 'O') { // check west
            probability -= 1;
        }
        if (col + 1 < 10 && guesses[row][col + 1] == 'O') { // check east
            probability -= 1;
        }
        if (row - 1 >= 0 && guesses[row - 1][col] == 'O') { // check south
            probability -= 1;
        }
        if (row + 1 < 10 && guesses[row + 1][col] == 'O') { // check north
            probability -= 1;
        }

		return probability + directionScore(row, col, guesses, boatsAlive);
	}

    /**
     * The value of an 'X' decreases exponentially from the current square.
     * Uses the exponential decay formula found here: https://www.cuemath.com/exponential-decay-formula/
     * Nothing special about these numbers, they're just from me running 50k sims tweaking the settings manually each time.
     * decayFactor of 0.44 and starting value of 85 have given me the best results.
     * @param currentSquare The row or column of the current square.
     * @param i The position of the 'X' on the row or column.
     * @return The calculated probability of the current square.
     */
    public static int calcProbability(int currentSquare, int i) {
        double decayFactor = 0.44;
        int distance = Math.abs(currentSquare - i);
		return ((int)(Math.pow(decayFactor, distance) * 85));
	}

    /**
     * This method checks which boats are alive, and sees which of those boats can pass through the current square in
     * the four cardinal directions (north, east, south, west) and gives a score based on that information.
     * @param row The current square's row.
     * @param col The current square's col.
     * @param guesses Grid describing the state of the game.
     * @param boatsAlive Array of boats alive.
     * @return The direction score.
     */
    public static int directionScore(int row, int col, char[][] guesses, int[] boatsAlive){
		int directionScore = 0;
		for (int n = 1; n <= boatsAlive.length - 1; n++) { // Check from the smallest boat up to the biggest alive.
			int up = 1, right = 1, down = 1, left = 1;
			for (int i = 1; i < boatsAlive[n]; i++) {
				if (up == 1 && (row - i < 0 || (guesses[row - i][col] != '.' && guesses[row - i][col] != 'X'))) { // Check north
					up--;
				} 
                if (right == 1 && (col + i >= 10 || (guesses[row][col + i] != '.' && guesses[row][col + i] != 'X'))) { // Check east
					right--;
				} 
                if (down == 1 && (row + i >= 10 || (guesses[row + i][col] != '.' && guesses[row + i][col] != 'X'))) { // Check south
					down--;
				}
                if (left == 1 && (col - i < 0 || (guesses[row][col - i] != '.' && guesses[row][col - i] != 'X'))) { // Check west
					left--;
				}
			}
			// for each valid direction, add the size of the boat to the direction score.
			directionScore += ((up * boatsAlive[n]) + (right * boatsAlive[n]) + (down * boatsAlive[n]) + (left * boatsAlive[n]));
		}
		return directionScore ;
	}

    /**
     * Converts integer coordinates to string coordinates.
     * @param row The best square's row.
     * @param col The best square's column
     * @return String coordinate version of the int coordinates.
     */
    public static String stringCoords(int row, int col) {
		// return the string version of row + col
		String first = String.valueOf((char)('A' + row));
		String last = String.valueOf((char)('1' + col));
		if (col == 9) { // '9' + '1' != '10' so return "10" directly.
			return first + "10";
		}
		return first + last; 
	}
}
