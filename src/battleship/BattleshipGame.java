package battleship;

import java.util.*;

/**
 * One-player version of the Battleship game for the command line.
 *
 * @author Carsten Fuhs
 */
public class BattleshipGame {

    /**
     * Runs the Battleship game.
     */
    public static void runGame() {
        System.out.println("============ B A T T L E S H I P ============");
        Ocean ocean = new Ocean();
        System.out.println("Placing ships on ocean ...");
        ocean.placeAllShipsRandomly();
        System.out.println("Done");
        System.out.println("The current state of the ocean: ");
        ocean.print();
        while (! ocean.isGameOver()) {
            // inform the player of the results of their actions so far
            System.out.println();
            System.out.println("You have fired " + ocean.getShotsFired() + " shots.");
            System.out.println("You have scored " + ocean.getHitCount() + " hits.");
            System.out.println("You have sunk " + ocean.getShipsSunk() + " ships.");

            // get a new target from the player
            System.out.println("Please enter the row for your next shot.");
            int row = getZeroToNine();
            System.out.println("Please enter the column for your next shot.");
            int col = getZeroToNine();

            // fire!
            boolean hit = ocean.shootAt(row, col);
            if (hit) {
                System.out.println("Hit!");
                if (ocean.hasSunkShipAt(row, col)) {
                    System.out.println("You just sank a " + ocean.getShipTypeAt(row, col));
                }
            } else {
                System.out.println("Miss!");
            }

            System.out.println("The current state of the ocean: ");
            ocean.print();
            System.out.println();
        }
        System.out.println("All ships have been sunk.");
        System.out.println(ocean.getShotsFired() + " shots were fired.");
    }

    /**
     * Asks the user repeatedly until an integer from 0 to 9 is entered
     * on the keyboard.
     *
     * @return the entered integer from 0 to 9
     */
    private static int getZeroToNine() {
        boolean needData = true;
        int val = 0; // dummy initialisation
        while (needData) {
            Scanner scan = new Scanner(System.in);
            System.out.print("Please enter an integer from 0 to 9: ");
            if (scan.hasNextInt()) {
                val = scan.nextInt();
                if (val >= 0 && val <= 9) {
                    needData = false;
                }
            }
        }
        return val;
    }

    /**
     * Calls the method to run the game once.
     *
     * @param args Ignored.
     */
    public static void main(String[] args) {
        runGame();
    }
}
