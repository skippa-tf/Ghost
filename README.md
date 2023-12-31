# Ghost

## Overview
Ghost is my battleship AI that I created for a school project in my first semester. It uses a probability map to calculate ship locations. 
Ghost's best average over 100,000,000 games is **45.277 turns**.

## How It Works
Ghost operates by creating a probability grid. It loops through each square on the grid and performs the following actions:
1. **Check Boat Status**: Determine which boats are still active in the game.
2. **Probability Calculation**:
   - Count the number of 'X's (hits) in the current row.
   - Count the number of 'X's in the current column.
   - Calculate the distance from 'X's to the current square.
   - Evaluate the likelihood of each boat type being in the current square, considering the boats still in play.
   - Lower the value of squares adjacent to misses.
3. **Select Highest Probability Square**:
   - If there is a tie in probability, prioritize the square more likely to contain a smaller boat.

## Installation and Usage

### Prerequisites
Ensure you have Java installed on your system to compile and run the simulation.

### Steps to Run
1. Download the source files: `BattleShipTools.java`, `RunSimulation.java`, and `Ghost.java`.
2. Compile the simulation:
   `javac .\RunSimulation.java`
3. Execute the simulation:
   `java RunSimulation`
   
### Configuration
- To alter the number of simulations, modify line 15 in `RunSimulation.java`.
- It's advisable to turn off `showAll` in `RunSimulation.java` for multiple runs, as it can significantly slow down the process.
- For debugging and viewing the probability grid, enable the debugging mode on line 54 in `Ghost.java`.

### Note
- Remember to recompile with `javac .\RunSimulation.java` every time you make changes to the source files.
- `RunSimulation.java` and `BattleShipTools.java` were not written by me, they were provided by my professor.


### Things I Learned 
- Improve my version control. Every version it would of been nice to include an average turn score for that commit.
- Just because I could run the program in a single for loop, doesn't mean I should. Efficiency was not the objective. It took me too long to change my mindset about staying in a single loop, multiple loops allowed my program much more freedom and made it a lot smarter.
- Write Javadocs while writing the program, not at the very end.
- I need to figure out a system for tracking bugs, and potential improvements.
