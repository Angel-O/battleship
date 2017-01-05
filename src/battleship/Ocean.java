package battleship;

import java.util.Random;

/**
 * Represents the {@value #OCEAN_WIDTH} x {@value #OCEAN_HEIGHT} matrix where
 * {@value #BATTLESHIPS} {@linkplain Battleship}(s), {@value #CRUISERS}
 * {@linkplain Cruiser}(s), {@value #DESTROYERS} {@linkplain Destroyer}(s),
 * {@value #SUBMARINES} {@linkplain Submarine}(s) are randomly placed onto. It
 * keeps a count of the number of shots fired during the game, the number of
 * successful hits and the number of ships that have been sunk
 *
 * @author Angelo Oparah
 *
 */
public class Ocean
{
	/** matrix to access ships in the ocean. */
	private Ship[][] ships;

	/** count of the total shots fired at any given time of the game. */
	private int shotsFired;

	/** count of the total times a shot hit any ship that was not sunken. */
	private int hitCount;

	/** number of ships sunk. */
	private int shipsSunk;

	/** max height of the {@linkplain Ocean}. */
	public static final int OCEAN_HEIGHT = 10;

	/** max length of the {@linkplain Ocean}. */
	public static final int OCEAN_WIDTH = 10;

	/** number of {@linkplain Battleship} in the ocean. */
	public static final int BATTLESHIPS = 1;

	/** number of {@linkplain Cruiser} ships in the ocean. */
	public static final int CRUISERS = 2;

	/** number of {@linkplain Destroyer} ships in the ocean. */
	public static final int DESTROYERS = 3;

	/** number of {@linkplain Submarine} ships in the ocean. */
	public static final int SUBMARINES = 4;


	/**
	 * Constructs a new Ocean instance filling each position with an
	 * {@linkplain EmptySea}.
	 *
	 */
	public Ocean()
	{
		ships = InitializeOcean();
	}

	/**
	 * Returns the total number of shots fired.
	 *
	 * @return the number of shots fired.
	 */
	public int getShotsFired()
	{
		return shotsFired;
	}

	/**
	 * Returns the hit count, that is the count of how many times a ship was
	 * hit.
	 *
	 * @return the current hit count.
	 */
	public int getHitCount()
	{
		return hitCount;
	}

	/**
	 * Returns the current number of ships sunk.
	 *
	 * @return the number of ships sunk.
	 */
	public int getShipsSunk()
	{
		return shipsSunk;
	}

	/**
	 * Indicates the current status of the game.
	 *
	 * @return {@code true} if all ships have been sunk, {@code false}
	 *         otherwise.
	 */
	public boolean isGameOver()
	{
		return shipsSunk == BATTLESHIPS + CRUISERS + DESTROYERS + SUBMARINES;
	}

	/**
	 * Return a 2-dimensional array containing the ships in the ocean. Used for
	 * testing and debugging purposes.
	 *
	 * @return the array containing the ships in the ocean.
	 */
	public Ship[][] getShipArray()
	{
		return ships;
	}

	/**
	 * Places all the ships randomly on the ocean in such a way that ships do
	 * not overlap, or are adjacent to other ships (either vertically,
	 * horizontally, or diagonally). Longer ships are placed first to increase
	 * efficiency and the chances of dropping all the ships
	 */
	public final void placeAllShipsRandomly()
	{
		// placing longer ships first so that checking if ships overlap as we
		// move forward from the bow won't be needed (except from the bow
		// itself)
		this.<Battleship>placeShipsOntoOcean(BATTLESHIPS, Battleship.class);
		this.<Cruiser>placeShipsOntoOcean(CRUISERS, Cruiser.class);
		this.<Destroyer>placeShipsOntoOcean(DESTROYERS, Destroyer.class);
		this.<Submarine>placeShipsOntoOcean(SUBMARINES, Submarine.class);
	}

	/**
	 * Checks whether or not the location specified contains a real ship. A real
	 * ship is an actual ship, that is any ship in the ocean that is not a
	 * {@linkplain EmptySea}.
	 *
	 * @param row
	 *            vertical coordinate of the ship.
	 * @param column
	 *            horizontal coordinate of the ship.
	 *
	 * @return {@code true} if the ship at the specified location is real,
	 *         {@code false} otherwise.
	 */
	public boolean isOccupied(int row, int column)
	{
		return ships[row][column].isRealShip();
	}

	/**
	 * Returns the ship type at the given location.
	 *
	 * @param row
	 *            vertical coordinate of the ship.
	 * @param column
	 *            horizontal coordinate of the ship.
	 * @return the type of the ship at the location specified.
	 */
	public String getShipTypeAt(int row, int column)
	{
		return ships[row][column].getShipType();
	}

	/**
	 * Indicates if the given location contains a sunken ship.
	 *
	 * @param row
	 *            horizontal coordinate.
	 * @param column
	 *            vertical coordinate.
	 * @return {@code true} if the location contains a sunken ship,
	 *         {@code false} otherwise.
	 */
	public boolean hasSunkShipAt(int row, int column)
	{
		return ships[row][column].isSunk();
	}

	/**
	 * Displays the current status of the ocean on a numbered grid.
	 *
	 * "S" indicates a location that was fired with a successful outcome. "-"
	 * indicates a location that was fired with an unsuccessful outcome. "x"
	 * indicates a location containing a sunken ship. "." indicates a location
	 * that is yet to be fired upon.
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
					else if (j == 0)
					{
						// print the first column of numbers
						System.out.print(i - 1);
					}
				}
				else
				{
					// otherwise print the whatever is in the ocean
					System.out.print(displayShipState(i - 1, j - 1));
				}
			}
			// print the next row on a separate line
			System.out.println();
		}
	}


	/**
	 * Shoots at the location correspondent to the given coordinates and updates
	 * the shot and the hit count. If a If a location contains a real ship, it
	 * returns true every time the user shoots at that same location. Once a
	 * ship has been sunk, additional shots at the same location will return
	 * false.
	 *
	 * @param row
	 *            horizontal coordinate to be fired upon.
	 * @param column
	 *            vertical coordinate to be fired upon.
	 * @return {@code true} if the location contains a real ship still afloat,
	 *         {@code false} otherwise.
	 */
	public boolean shootAt(int row, int column)
	{
		// increment the total number of shots fired
		shotsFired++;

		if (ships[row][column].shootAt(row, column))
		{
			// increment the hit count in case of a successful shot
			hitCount++;

			if (ships[row][column].isSunk())
			{
				// increment the count of ship sunk if this shot
				// sunk the ship
				shipsSunk++;
			}

			return true;
		}

		return false;
	}

	// ============== private methods ============= //

	/**
	 * Sets {@linkplain EmptySea} in the ocean.
	 *
	 * @return a matrix containing all EmptySea instances representing the
	 *         initial state of the ocean before any ship is placed onto it.
	 *
	 */
	private static final Ship[][] InitializeOcean()
	{
		Ship[][] ships = new Ship[OCEAN_WIDTH][OCEAN_HEIGHT];

		for (int i = 0; i < OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < OCEAN_HEIGHT; j++)
			{
				Ship emptySea = new EmptySea();
				emptySea.setBowRow(i);
				emptySea.setBowColumn(j);

				ships[i][j] = emptySea;
			}
		}

		return ships;
	}

	/**
	 * Returns a {@code char} representing the current state of the ship part,
	 * where "x" indicates a sunk ship, "." a location that is yet to be fired
	 * upon, "S" a location that was fired upon hitting a ship and "-" a
	 * location that was fired upon without hitting any ship.
	 *
	 * @param row
	 *            horizontal coordinate of the ship part to display.
	 * @param column
	 *            vertical coordinate of the ship part to display
	 * @return a {@code char} representing the state of the part of the ship at
	 *         the given location.
	 */
	private char displayShipState(int row, int column)
	{
		return hasSunkShipAt(row, column) ? 'x' : getShipStateAndPrintShipPart(row, column);
	}

	/**
	 * Gets the current state of the ship in {@code string} format and extracts
	 * a {@code char} representing the part of the (yet to be sunken, if real)
	 * ship at the given location. If hit, the ship part will be marked by the
	 * {@linkplain Ship} class with a "S" if it's a real ship or a "-" if it's a
	 * {@linkplain EmptySea}, otherwise it will be marked with a "." to indicate
	 * that the location in yet to be fired upon.
	 *
	 * @param row
	 *            horizontal coordinate of the ship part to display.
	 * @param column
	 *            vertical coordinate of the ship part to display
	 * @return a {@code char} representing the state of the part of the ship at
	 *         the given location.
	 */
	private char getShipStateAndPrintShipPart(int row, int column)
	{
		Ship ship = ships[row][column];

		String shipState = ships[row][column].toString();

		// get the relevant char based on orientation and offset from the bow
		return ship.isHorizontal() ? shipState.charAt(column - ship.getBowColumn())
				: shipState.charAt(row - ship.getBowRow());
	}

	/**
	 * Randomly places the given number of ships of the given type on to the
	 * ocean, without having any ship overlapping, be adjacent in any direction
	 * or exceed the ocean's border.
	 *
	 * @param amount
	 *            number of ships to place.
	 * @param shipClass
	 *            type of the ship to place.
	 */
	private <T extends Ship> void placeShipsOntoOcean(int amount, Class<T> shipClass)
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

			// otherwise create a ship representing a ship part to replicate
			// across the length of the (whole) ship
			Ship shipPart = createShip(shipClass);

			assert shipPart != null : "unable to build ship part";

			// set the orientation of the ship part and the bow coordinates
			shipPart.setHorizontal(horizontal);
			shipPart.setBowRow(bowRow);
			shipPart.setBowColumn(bowColumn);

			// place copies of the ship part onto the ocean at 'i' offset from
			// the bow. Copying the same ship part will ensure that the
			// information contained in the hit array will be the same
			// regardless of the location of the ship part
			for (int i = 0; i < shipLength; i++)
			{
				int row = horizontal ? bowRow : bowRow + i;
				int column = horizontal ? bowColumn + i : bowColumn;
				ships[row][column] = shipPart;
			}

			// increment the count of ship placed
			shipPlaced++;
		}
		while (shipPlaced < amount);
	}

	/**
	 * Factory method to generate ship parts.
	 *
	 * @param <T>
	 *            subclass of the {@linkplain Ship} type.
	 * @param shipClass
	 *            type of the ship part to be generated
	 * @return a ship part of the requested type.
	 */
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



	/**
	 * Returns the length of the ship based on the ship type; if the ship type
	 * does not match any Known ship type it will return {@code 0}.
	 *
	 * @param <T>
	 *            subclass of the {@linkplain Ship} type.
	 * @param shipClass
	 *            type of the ship whose length needs to be found.
	 * @return the length of the particular ship type.
	 */
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
		else if (shipClass == EmptySea.class)
		{
			return 1;
		}
		return 0;
	}



	/**
	 * Determines whether or not a ship can be placed in the area starting from
	 * the bow coordinates. The criteria to pass the test are: the ship cannot
	 * overlap with any other ship and cannot be adjacent to any other ship,
	 * either vertically, horizontally or diagonally and the ship length cannot
	 * exceed the ocena's borders.
	 *
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param shipLength
	 *            length of the ship.
	 * @param horizontal
	 *            orientation on the ocean's matrix (vertical or diagonal).
	 * @return {@code true} if the ship can be placed in the area respecting the
	 *         criteria.
	 */
	private boolean areaIsSuitableToPlaceShip(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		// there is no need to check the mid area, where the ship will
		// actually be placed (overlapping is guaranteed not to occur since
		// we are dropping the longest ships first). The only overlapping that
		// can happen in the mid area is at the bow location
		return !isOccupied(bowRow, bowColumn) && areaAtEachEndIsClear(bowRow, bowColumn, shipLength, horizontal)
				&& areaAlongTheLengthIsClear(bowRow, bowColumn, shipLength, horizontal);
	}



	/**
	 * Determines whether the area along the length of the ship is clear.
	 *
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param shipLength
	 *            length of the ship.
	 * @param horizontal
	 *            orientation on the ocean's matrix (vertical or diagonal).
	 * @return {@code true} if the ship can be placed in the area respecting the
	 *         criteria.
	 */
	private boolean areaAlongTheLengthIsClear(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		// iteration variables used to scan the area
		int row;
		int column;

		// checking both sides (vertically or horizontally) of the area that
		// could potentially host the ship
		for (int i = 0; i < shipLength; i++)
		{
			// checking bottom (if horizontal), or right (if vertical)
			// if horizontal take the row below(+1), iterate over the column(+i)
			// if vertical take the right column(+1), iterate over the row(+i)
			row = horizontal ? bowRow + 1 : bowRow + i;
			column = horizontal ? bowColumn + i : bowColumn + 1;

			if (row < OCEAN_HEIGHT && column < OCEAN_WIDTH)
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

			if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
			{
				if (isOccupied(row, column))
				{
					return false;
				}
			}
		}
		return true;
	}



	/**
	 * Determines whether the area at both ends of the ship is clear.
	 *
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param shipLength
	 *            length of the ship.
	 * @param horizontal
	 *            orientation on the ocean's matrix (vertical or diagonal)
	 * @return {@code true} if the ship can be placed in the area respecting the
	 *         criteria.
	 */
	private boolean areaAtEachEndIsClear(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		// coordinates of the end of the ship
		int sternRow = horizontal ? bowRow : bowRow + shipLength - 1;
		int sternColumn = horizontal ? bowColumn + shipLength - 1 : bowColumn;

		// iteration variables used to scan the area
		int row;
		int column;

		// checking both ends (vertically or horizontally, on the same line, and
		// diagonally) of the area that could potentially host the ship,
		// looping from -1 to 1 as we only need to check 3 cells at each end
		// placed above(or left ==> -1), on the same line (==> 0) or below(or
		// right ==> +1)
		for (int i = -1; i <= 1; i++)
		{
			// checking left end (if horizontal), or top end (if vertical)
			// if horizontal take the left column(-1), iterate over the row(+i)
			// if vertical take the row above(-1), iterate over the column(+i)
			row = horizontal ? bowRow + i : bowRow - 1;
			column = horizontal ? bowColumn - 1 : bowColumn + i;

			if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
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

			if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
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
