package battleship;

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

	/** count of the total times ships were hit */
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

		Ship emptySea = new EmptySea();

		for (int i = 0; i < OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < OCEAN_HEIGHT; j++)
			{
				ships[i][j] = emptySea;
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
	 * @param nextInt
	 * @param bowColumn
	 * @return
	 *
	 *
	 * 		OCEAN Shoots at the part of the ship at that location. Returns
	 *         true if the given location contains a real ship (not an
	 *         EmptySea), still afloat, false if it does not. In addition, this
	 *         method updates the number of shots that have been fired, and the
	 *         number of hits. Note: If a location contains a real ship, shootAt
	 *         should return true every time the user shoots at that same
	 *         location. Once a ship has been sunk, additional shots at its
	 *         location should return false.
	 *
	 *         SHIP boolean shootAt(int row, int column) If a part of the ship
	 *         occupies the given row and column, and the ship hasn't been sunk,
	 *         mark that part of the ship as hit (in the hit array, index 0
	 *         indicates the bow) and return true, otherwise return false.
	 */
	public boolean shootAt(int row, int column)
	{
		// increment the total number of shots fired
		shotsFired++;

		if (ships[row][column].shootAt(row, column))
		{
			// increment the hit count
			hitCount++;
			return true;
		}

		return false;
	}


	/**
	 * @param amount
	 * @param shipClass
	 */
	private <T extends Ship> void placeShips(int amount, Class<T> shipClass)
	{
		assert amount > 0 : "number of ships must be strictly positive";

		// get the length of the particular ship type
		int shipLength = establishShipLengthFromShipType(shipClass);

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

			// get a random orientation for the current ship
			boolean horizontal = random.nextBoolean();

			if (!areaIsSuitableToPlaceShip(bowRow, bowColumn, shipLength, horizontal))
			{
				// if there is a ship already there or the area around is
				// not clear, try a new position
				continue;
			}

			// create a ship representing a ship part with length
			// based on the offset from the bow
			Ship shipPart = createShip(shipClass);

			// set the orientation of the ship part and the coordinates
			shipPart.setHorizontal(horizontal);
			shipPart.setBowRow(bowRow);
			shipPart.setBowColumn(bowColumn);

			for (int i = 0; i < shipLength; i++)
			{
				// place copies of the ship part onto the ocean at 'i' offset
				// from the bow
				// each ship part will hold a reference to the same hit array
				int row = horizontal ? bowRow : bowRow + i;
				int column = horizontal ? bowColumn + i : bowColumn;
				ships[row][column] = shipPart;
			}

			shipPlaced++;
		}
		while (shipPlaced < amount);
	}

	private <T extends Ship> Ship createShip(Class<T> shipClass)
	{
		Ship shipPart = null;

		if (shipClass == Battleship.class)
		{
			shipPart = new Battleship();
		}
		else if (shipClass == Cruiser.class)
		{
			shipPart = new Cruiser();
		}
		else if (shipClass == Destroyer.class)
		{
			shipPart = new Destroyer();
		}
		else if (shipClass == Submarine.class)
		{
			shipPart = new Submarine();
		}

		return shipPart;
	}

	private <T extends Ship> int establishShipLengthFromShipType(Class<T> shipClass)
	{
		if (shipClass == Battleship.class)
		{
			return Battleship.BATTLESHIP_LENGTH;
		}
		else if (shipClass == Cruiser.class)
		{
			return Cruiser.CRUISER_LENGTH;
		}
		else if (shipClass == Destroyer.class)
		{
			return Destroyer.DESTROYER_LENGTH;
		}
		else if (shipClass == Submarine.class)
		{
			return Submarine.SUBMARINE_LENGTH;
		}
		else
		{
			return 0;
		}
	}

	private boolean areaIsSuitableToPlaceShip(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		return !isOccupied(bowRow, bowColumn) && areaAtEachEndIsClear(bowRow, bowColumn, shipLength, horizontal)
				&& areaAlongTheLengthIsClear(bowRow, bowColumn, shipLength, horizontal);
	}

	private boolean areaAlongTheLengthIsClear(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		int row;
		int column;

		// checking both ship sides vertically or horizontally
		for (int i = 0; i < shipLength; i++)
		{
			// checking bottom (if horizontal), or right (if vertical)
			// if horizontal take the row below(+1), iterate over the column(+i)
			// if vertical take the right column(+1), iterate over the row(+i)
			row = horizontal ? bowRow + 1 : bowRow + i;
			column = horizontal ? bowColumn + i : bowColumn + 1;

			if (row < Ocean.OCEAN_HEIGHT && column < Ocean.OCEAN_WIDTH)
			{
				if (isOccupied(row, column))
				{
					return false;
				}
			}

			// checking top (if horizontal), or left (if vertical)
			// if horizontal take the row above(-1), iterate over the column(+i)
			// if vertical take the left column(-1), iterate over the row(+i)
			row = horizontal ? bowRow - 1 : bowRow + i;
			column = horizontal ? bowColumn + i : bowColumn - 1;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (isOccupied(row, column))
				{
					return false;
				}
			}
		}
		return true;
	}

	private boolean areaAtEachEndIsClear(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		int sternRow = horizontal ? bowRow : bowRow + shipLength - 1;
		int sternColumn = horizontal ? bowColumn + shipLength - 1 : bowColumn;

		int row;
		int column;

		// checking both ship ends vertically or horizontally...diagonally

		// from -1 to 1..
		for (int i = -1; i <= 1; i++)
		{
			// checking left end (if horizontal), or top end (if vertical)
			// if horizontal take the left column(-1), iterate over the row(+i)
			// if vertical take the row above(-1), iterate over the column(+i)
			row = horizontal ? bowRow + i : bowRow - 1;
			column = horizontal ? bowColumn - 1 : bowColumn + i;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (isOccupied(row, column))
				{
					return false;
				}
			}

			// checking right end (if horizontal), or bottom end (if vertical)
			// if horizontal take the right column(+1), iterate over the row(+i)
			// if vertical take the row below(+1), iterate over the column(+i)
			row = horizontal ? sternRow + i : sternRow + 1;
			column = horizontal ? sternColumn + 1 : sternColumn + i;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (isOccupied(row, column))
				{
					return false;
				}
			}
		}

		return true;
	}
}
