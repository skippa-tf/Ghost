# Ghost
Ghost is my battleship AI that I created for a school project. It uses a probability map to calculate ship locations.

Ghost's best average over 100,000,000 games is 45.277 turns.

#### How It Works
Ghost creates a probability grid by looping through each square, and does the following things:

    1. Check what boats are alive.
    2. Fill out the probability grid based on a few factors:
        2a. Number of X's in the row.
        2b. Number of X's in the column.
        2c. Distance of the X's to the current square.
        2d. What boats are alive, and the likelihood of the current square holding each boat, in each direction.
        2e. Devalue squares with adjacent misses.
    3. Pick the highest probability square.
        3a. If there are two equal squares, pick the one more likely to hold a smaller boat.

#### How To Use It

1. Download `BattleShipTools.java`, `RunSimulation.java`, and `Ghost.java`.
2. Compile with `javac .\RunSimulation.java`.
3. Run with `java RunSimulation`

To change the amount of simulations change line 15 in `RunSimulation.java`.
  I would recommend to turn off showAll in `RunSimulation.java` for anything more than 1 run.
To view the probability grid, enable debugging on line 54 in `Ghost.java`.

#### Remember to recompile every change.

