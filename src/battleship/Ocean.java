package battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the matrix which ships are placed onto.
 *
 * @author Angelo Oparah
 *
 */
public class Ocean
{
	/** matrix to access ships in the ocean */
	private Ship[][] ships;

	/** count of the total shots fired at any given time of the game */
	private int shotsFired;

	/** count of the total hits */
	private int hitCount;

	/** number of ships sunk */
	private int shipsSunk;

	/** max height of the {@link Ocean} */
	public static final int OCEAN_HEIGHT = 10;

	/** max length of the {@link Ocean} */
	public static final int OCEAN_WIDTH = 10;

	/** number of {@link Battleship} in the ocean */
	public static final int BATTLESHIPS = 1;

	/** number of {@link Cruiser} ships in the ocean */
	public static final int CRUISERS = 2;

	/** number of {@link Destroyer} ships in the ocean */
	public static final int DESTROYERS = 3;

	/** number of {@link Submarine} ships in the ocean */
	public static final int SUBMARINES = 4;


	/**
	 * Constructs a new Ocean instance filling each position with an
	 * {@link EmptySea}.
	 *
	 */
	public Ocean()
	{
		ships = InitializeOcean();
	}

	/**
	 * Sets {@link EmptySea} in the ocean.
	 *
	 * @return a matrix containing all EmptySea instances representing the
	 *         initial state of the ocean before any ship is placed onto it
	 *
	 */
	private static final Ship[][] InitializeOcean()
	{
		Ship[][] ships = new Ship[OCEAN_WIDTH][OCEAN_HEIGHT];

		for (int i = 0; i < OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < OCEAN_HEIGHT; j++)
			{
				ships[i][j] = new EmptySea();
			}
		}

		return ships;
	}

	/**
	 * Returns the number of shots fired.
	 *
	 * @return the number of shots fired.
	 */
	public int getShotsFired()
	{
		return shotsFired;
	}

	/**
	 * Returns the hit count.
	 *
	 * @return the current hit count.
	 */
	public int getHitCount()
	{
		return hitCount;
	}

	/**
	 * Returns the number of ships sunk
	 *
	 * @return the number of ships sunk
	 */
	public int getShipsSunk()
	{
		return shipsSunk;
	}

	/**
	 * @return
	 */
	public boolean isGameOver()
	{
		return true;
	}

	/**
	 * @return
	 */
	public Ship[][] getShipArray()
	{
		return ships;
	}

	/**
	 * Places all the ships randomly on the ocean in such a way that ships do
	 * not overlap, or are adjacent to other ships (either vertically,
	 * horizontally, or diagonally).
	 */
	public void placeAllShipsRandomly()
	{
		this.<Battleship>placeShips(BATTLESHIPS, Battleship.class);
		this.<Cruiser>placeShips(CRUISERS, Cruiser.class);
		this.<Destroyer>placeShips(DESTROYERS, Destroyer.class);
		this.<Submarine>placeShips(SUBMARINES, Submarine.class);
	}

	/**
	 * Checks whether or not the location specified contains a real ship. A real
	 * ship is an actual ship, that is any ship in the ocean that is not a
	 * {@link EmptySea}.
	 *
	 * @param row
	 *            vertical coordinate of the ship
	 * @param column
	 *            horizontal coordinate of the ship
	 *
	 * @return {@code true} if the ship at the specified location is real,
	 *         {@code false} otherwise
	 */
	public boolean isOccupied(int row, int column)
	{
		return ships[row][column].isRealShip();
	}

	/**
	 * Returns the ship type at the given location
	 *
	 * @param row
	 *            vertical coordinate of the ship
	 * @param column
	 *            horizontal coordinate of the ship
	 * @return the type of the ship at the location specified
	 */
	public String getShipTypeAt(int row, int column)
	{
		return ships[row][column].getShipType();
	}

	/**
	 * Displays the current status of the ocean.
	 *
	 */
	public void print()
	{
		for (int i = 0; i <= OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j <= OCEAN_WIDTH; j++)
			{
				// if it's the first row or first column
				if (i == 0 || j == 0)
				{
					if (i == 0 && j < OCEAN_WIDTH)
					{
						// print the first row of numbers
						System.out.print(j == 0 ? " " + j : j);
					}
					else if (i > 0 && j < OCEAN_HEIGHT)
					{
						// print the first column of numbers
						System.out.print(i == 0 ? " " : i - 1);
					}
				}
				else
				{
					// otherwise print the whatever is in the ocean
					System.out.print(ships[i - 1][j - 1]);
				}
			}
			// print the next row on a separate line
			System.out.println();
		}
	}

	/**
	 * @param amount
	 * @param shipClass
	 */
	private <T extends Ship> void placeShips(int amount, Class<T> shipClass)
	{
		assert amount > 0 : "number of ships must be strictly positive";

		// fill the ship list with the correct amount of ships of the right type
		ArrayList<T> shipList = populateShipList(amount, shipClass);

		assert shipList.size() == amount : "unable to build ships";

		// get the length of the particular ship type
		int shipLength = shipList.get(0).getLength();

		Random random = new Random();

		// counts how many ships have been successfully placed in the ocean
		int shipPlaced = 0;

		do
		{
			// get random coordinates for the current ship bow (the maximum
			// value valid for the bow is the width(or height) of the ocean
			// minus the ship length: this avoids the ship to exceed the
			// ocean's borders. Adding 1 as the higher boundary of the
			// nextInt method is non inclusive)
			int bowRow = random.nextInt(OCEAN_WIDTH - shipLength + 1);
			int bowColumn = random.nextInt(OCEAN_HEIGHT - shipLength + 1);

			if (isOccupied(bowRow, bowColumn) || !sorroundingAreaIsClear(bowRow, bowColumn))
			{
				// if there is a ship is already there or the area around is
				// not clear, try a new position
				continue;
			}

			// set the bow coordinates for the current ship in the list
			Ship ship = shipList.get(shipPlaced);
			ship.setBowRow(bowRow);
			ship.setBowColumn(bowColumn);

			// set a random orientation for the current ship
			ship.setHorizontal(random.nextBoolean());

			// try and place the ship in the ocean
			if (tryPlaceShipInTheOcean(ship, shipClass))
			{
				// if the ship was successfully dropped, go to the next ship
				// in the list (by updating the counter),
				// if not try again with new random coordinates (leave the
				// counter unchanged)
				shipPlaced++;
			}
		}
		while (shipPlaced < amount);
	}

	private void removeShipFromTheOcean(Ship ship, int length)
	{
		int row = ship.getBowRow();
		int column = ship.getBowColumn();
		boolean horizontal = ship.isHorizontal();

		for (int i = 0; i < length; i++)
		{
			// if the ship is horizontal the variable index (i) is the column,
			// otherwise it's the row
			ships[horizontal ? row : row + i][horizontal ? column + i : column] = new EmptySea();
		}
	}

	private <T extends Ship> boolean tryPlaceShipInTheOcean(Ship ship, Class<T> shipClass)
	{
		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();
		boolean horizontal = ship.isHorizontal();

		// place the bow of the current ship in the ocean
		ships[bowRow][bowColumn] = ship;

		// starting from 1 as we just placed the bow
		int shipsPlacedSoFar = 1;

		boolean wholeShipPlaced = true;

		// start from index one as the bow has already been placed
		for (int i = 1; i < ship.getLength(); i++)
		{
			// rest it for each ship part
			boolean shipPartplaced = false;

			// create a ship of length zero representing a ship part
			Ship shipPart = createShipPart(shipClass);

			// set the coordinates and the orientation of the ship part
			int row = horizontal ? bowRow : bowRow + i;
			int column = horizontal ? bowColumn + i : bowColumn;
			shipPart.setBowRow(row);
			shipPart.setBowColumn(column);
			shipPart.setHorizontal(horizontal);

			// try and place it in the ocean
			if (!isOccupied(row, column))
			{
				// check the route (horizontally or vertically) as you place
				// ship parts onto the ocean
				if (horizontal ? horizontalRouteIsClear(row, column) : verticalRouteIsClear(row, column))
				{
					ships[row][column] = shipPart;
					shipsPlacedSoFar++;
					shipPartplaced = true;
				}
			}
			if (!shipPartplaced)
			{
				// if the ship part was not dropped remove whatever was
				// dropped up to that point
				wholeShipPlaced = false;
				removeShipFromTheOcean(ship, shipsPlacedSoFar);
				break;
			}
		}

		return wholeShipPlaced;
	}

	// =============== ROUTE CHECKERS =============== //
	private boolean horizontalRouteIsClear(int row, int column)
	{

		if (row == 0)
		{
			// top edge
			return rightIsClear(row, column) && bottomIsClear(row, column) && bottomRightIsClear(row, column);
		}
		else if (row == OCEAN_HEIGHT - 1)
		{
			// bottom edge
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column);
		}
		else
		{
			// mid area
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column)
					&& bottomRightIsClear(row, column) && bottomIsClear(row, column);
		}
	}

	private boolean verticalRouteIsClear(int row, int column)
	{
		if (column == 0)
		{
			// left edge
			return bottomIsClear(row, column) && bottomRightIsClear(row, column) && rightIsClear(row, column);
		}
		else if (column == OCEAN_WIDTH - 1)
		{
			// right edge
			return bottomIsClear(row, column) && bottomLeftIsClear(row, column) && leftIsClear(row, column);
		}
		else
		{
			// mid area
			return bottomIsClear(row, column) && leftIsClear(row, column) && bottomLeftIsClear(row, column)
					&& bottomRightIsClear(row, column) && rightIsClear(row, column);
		}
	}

	// ================ AREA CHECKER ================ //
	// adjacency check before dropping the bow of the ship (scans the whole area
	// for a given location)
	private boolean sorroundingAreaIsClear(int row, int column)
	{
		if (row == 0)
		{
			// top edge
			if (column == 0)
			{
				// left corner
				return bottomIsClear(row, column) && bottomRightIsClear(row, column) && rightIsClear(row, column);
			}
			else if (column == OCEAN_WIDTH - 1)
			{
				// right corner
				return bottomIsClear(row, column) && bottomLeftIsClear(row, column) && leftIsClear(row, column);
			}
			// mid part of the top edge
			return rightIsClear(row, column) && bottomIsClear(row, column) && bottomRightIsClear(row, column)
					&& bottomLeftIsClear(row, column) && leftIsClear(row, column);
		}
		else if (row == OCEAN_HEIGHT - 1)
		{
			// bottom edge
			if (column == 0)
			{
				// left corner
				return topIsClear(row, column) && topRightIsClear(row, column) && rightIsClear(row, column);
			}
			else if (column == OCEAN_WIDTH - 1)
			{
				// right corner
				return topIsClear(row, column) && topLeftIsClear(row, column) && leftIsClear(row, column);
			}
			// mid part of the bottom edge
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column)
					&& leftIsClear(row, column) && topLeftIsClear(row, column);
		}
		else if (column == 0)
		{
			// left edge (only the mid part as the corners were considered
			// above)
			return topIsClear(row, column) && topRightIsClear(row, column) && rightIsClear(row, column)
					&& bottomRightIsClear(row, column) && bottomIsClear(row, column);

		}
		else if (column == OCEAN_WIDTH - 1)
		{
			// right edge (only the mid part as the corners were considered
			// above)
			return topIsClear(row, column) && topLeftIsClear(row, column) && leftIsClear(row, column)
					&& bottomLeftIsClear(row, column) && bottomIsClear(row, column);
		}
		else
		{
			// mid area of the ocean (no borders, no corners)
			return rightIsClear(row, column) && bottomRightIsClear(row, column) && bottomIsClear(row, column)
					&& bottomLeftIsClear(row, column) && leftIsClear(row, column) && topLeftIsClear(row, column)
					&& topIsClear(row, column) && topRightIsClear(row, column);
		}
	}

	// ============= helper CHECKERS ================ //
	private boolean bottomIsClear(int row, int column)
	{
		if (row == OCEAN_HEIGHT - 1)
		{
			return true;
		}
		return !isOccupied(row + 1, column);
	}

	private boolean rightIsClear(int row, int column)
	{
		if (column == OCEAN_WIDTH - 1)
		{
			return true;
		}
		return !isOccupied(row, column + 1);
	}

	private boolean topIsClear(int row, int column)
	{
		if (row == 0)
		{
			return true;
		}
		return !isOccupied(row - 1, column);
	}

	private boolean leftIsClear(int row, int column)
	{
		if (column == 0)
		{
			return true;
		}
		return !isOccupied(row, column - 1);
	}

	private boolean topRightIsClear(int row, int column)
	{
		if (row == 0 || column == OCEAN_WIDTH - 1)
		{
			return true;
		}
		return !isOccupied(row - 1, column + 1);
	}

	private boolean topLeftIsClear(int row, int column)
	{
		if (row == 0 || column == 0)
		{
			return true;
		}
		return !isOccupied(row - 1, column - 1);
	}

	private boolean bottomRightIsClear(int row, int column)
	{
		if (row == OCEAN_HEIGHT - 1 || column == OCEAN_WIDTH - 1)
		{
			return true;
		}
		return !isOccupied(row + 1, column + 1);
	}

	private boolean bottomLeftIsClear(int row, int column)
	{
		if (row == OCEAN_HEIGHT - 1 || column == 0)
		{
			return true;
		}
		return !isOccupied(row + 1, column - 1);
	}

	private <T extends Ship> ArrayList<T> populateShipList(int amount, Class<T> shipClass)
	{
		ArrayList<T> shipList = new ArrayList<>();

		for (int i = 0; i < amount; i++)
		{
			T ship = createShip(shipClass);

			if (ship != null)
			{
				// only add a ship if it was actually created
				shipList.add(ship);
			}
		}

		return shipList;
	}

	private <T extends Ship> T createShip(Class<T> shipClass)
	{
		T ship = null;

		try
		{
			ship = shipClass.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return ship;
	}

	private <T> Ship createShipPart(Class<T> shipClass)
	{
		Ship shipPart = null;

		if (shipClass == Battleship.class)
		{
			shipPart = new Battleship(0);
		}
		else if (shipClass == Cruiser.class)
		{
			shipPart = new Cruiser(0);
		}
		else if (shipClass == Destroyer.class)
		{
			shipPart = new Destroyer(0);
		}
		else
		{
			shipPart = new Submarine(0);
		}

		return shipPart;
	}
}
