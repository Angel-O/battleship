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
	 * {@linkplain EmptySea} and sets the hit, the shot and the ship sunk count
	 * to {@code 0}.
	 *
	 */
	public Ocean()
	{
		ships = initialiseOcean();
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
	 * Returns a 2-dimensional array containing the ships in the ocean. Used for
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
		this.<Battleship>placeShipsOntoOcean(BATTLESHIPS, Battleship.BATTLESHIP_LENGTH, Battleship.class);
		this.<Cruiser>placeShipsOntoOcean(CRUISERS, Cruiser.CRUISER_LENGTH, Cruiser.class);
		this.<Destroyer>placeShipsOntoOcean(DESTROYERS, Destroyer.DESTROYER_LENGTH, Destroyer.class);
		this.<Submarine>placeShipsOntoOcean(SUBMARINES, Submarine.SUBMARINE_LENGTH, Submarine.class);
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
	 * Places {@linkplain EmptySea} all over the ocean's surface.
	 *
	 * @return a matrix containing all EmptySea instances representing the
	 *         initial state of the ocean before any real ship is placed onto
	 *         it.
	 *
	 */
	private static final Ship[][] initialiseOcean()
	{
		Ship[][] ships = new Ship[OCEAN_HEIGHT][OCEAN_WIDTH];

		for (int i = 0; i < OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < OCEAN_WIDTH; j++)
			{
				// create an empty sea with "bow" at row "i" and column "j"
				Ship emptySea = new EmptySea();
				emptySea.setBowRow(i);
				emptySea.setBowColumn(j);

				// drop it onto the ocean
				ships[i][j] = emptySea;
			}
		}

		return ships;
	}

	/**
	 * Returns a {@code char} representing the current state of the ship part.
	 * If the location contains a sunk {@linkplain Ship} (considered as a whole)
	 * the ship state will be overwritten with a "x" to indicate that the whole
	 * ship was sunk. Otherwise the ship state will be returned. Note: the
	 * {@linkplain Ship} class will mark a location that is yet to be fired upon
	 * with a "." (dot) , a location that was fired upon hitting a ship with a
	 * "S" and a location that was fired upon without hitting any ship with a
	 * "-" (dash).
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
		return hasSunkShipAt(row, column) ? 'x' : getShipPartState(row, column);
	}

	/**
	 * Gets the current state of the (whole) ship in {@code string} format and
	 * extracts a {@code char} representing the part of the (yet to be sunken,
	 * if real) ship at the given location. If hit, the ship part will be marked
	 * by the {@linkplain Ship} class with a "S" if it's a real ship or a "-" if
	 * it's a {@linkplain EmptySea}, otherwise it will be marked with a "." to
	 * indicate that the location in yet to be fired upon.
	 *
	 * @param row
	 *            horizontal coordinate of the ship part to display.
	 * @param column
	 *            vertical coordinate of the ship part to display
	 * @return a {@code char} representing the state of the part of the ship at
	 *         the given location.
	 */
	private char getShipPartState(int row, int column)
	{
		Ship ship = ships[row][column];

		String shipState = ships[row][column].toString();

		// get the relevant char based on orientation and offset from the bow
		return ship.isHorizontal() ? shipState.charAt(column - ship.getBowColumn())
				: shipState.charAt(row - ship.getBowRow());
	}

	/**
	 * Randomly places the given number of ships of the given type and the given
	 * length on to the ocean, making sure that ships don't overlap, aren't
	 * adjacent in any direction to any other ship (horizontally, vertically or
	 * diagonally) and don't exceed the ocean's borders.
	 *
	 * @param <T>
	 *            type of the ships to place.
	 * @param amount
	 *            number of ships to place.
	 * @param shipLength
	 *            length of a single ship.
	 * @param shipClass
	 *            type of the ship to place.
	 */
	private <T extends Ship> void placeShipsOntoOcean(int amount, int shipLength, Class<T> shipClass)
	{
		assert amount > 0 : "number of ships must be strictly positive";
		assert shipLength > 0 : "ship length must be strictly positive";

		Random random = new Random();

		// keeps count of how many ships of the current type have been
		// successfully placed in the ocean
		int shipPlaced = 0;

		// coordinates and orientation will be set at random
		int bowRow;
		int bowColumn;
		boolean horizontal;

		while (shipPlaced < amount)
		{
			// get a random orientation for the current ship
			horizontal = random.nextBoolean();

			// get random coordinates for the bow within the ocean's boundaries,
			// with the upper bound depending on the orientation of the ship
			bowColumn = random.nextInt(horizontal ? OCEAN_WIDTH - shipLength + 1 : OCEAN_WIDTH);
			bowRow = random.nextInt(horizontal ? OCEAN_HEIGHT : OCEAN_HEIGHT - shipLength + 1);

			// try and place the ship at the given location
			if (areaIsSuitableToPlaceShip(bowRow, bowColumn, shipLength, horizontal))
			{
				// create a ship representing a ship part to replicate
				// across the length of the (whole) ship
				Ship shipPart = createShip(shipClass, bowRow, bowColumn, horizontal);

				assert shipPart != null : "unable to create ship. '" + shipClass + "' not recognized.";

				// place copies of the ship part onto the ocean at 'i' offset
				// from the bow. Copying the same ship part will ensure that the
				// information contained in the hit array will be the same for
				// the whole ship regardless of the location of the ship part
				for (int i = 0; i < shipLength; i++)
				{
					int row = horizontal ? bowRow : bowRow + i;
					int column = horizontal ? bowColumn + i : bowColumn;
					ships[row][column] = shipPart;
				}

				// increment the count of ship placed
				shipPlaced++;
			}
		}
	}

	/**
	 * Factory method to generate (real) ship parts of the given type. It will
	 * set the bow coordinates and orientation to the values passed as
	 * arguments. It can return a null value if the class passed as parameter is
	 * recognized or the class refers to a ship type that is not a real ship.
	 *
	 * @param <T>
	 *            subclass of the {@linkplain Ship} type.
	 * @param shipClass
	 *            type of the ship part to be generated.
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param horizontal
	 *            orientation of the ship that the ship part to create will be
	 *            part of.
	 * @return a ship part of the requested type; can be {@code null}.
	 */
	private <T extends Ship> Ship createShip(Class<T> shipClass, int bowRow, int bowColumn, boolean horizontal)
	{
		Ship ship = null;

		if (shipClass == Battleship.class)
		{
			ship = new Battleship();
		}
		else if (shipClass == Cruiser.class)
		{
			ship = new Cruiser();
		}
		else if (shipClass == Destroyer.class)
		{
			ship = new Destroyer();
		}
		else if (shipClass == Submarine.class)
		{
			ship = new Submarine();
		}

		if (ship != null)
		{
			// set the bow coordinates and the orientation of the ship
			ship.setBowRow(bowRow);
			ship.setBowColumn(bowColumn);
			ship.setHorizontal(horizontal);
		}

		// can return null (rather than a ship of a default ship type): leave
		// the door open for future extensibility. If we add a new ship type but
		// forget to add it in this method, the assertion used after calling
		// this method will fail and we will know exactly what happened, rather
		// than wondering why we get extra ships of a given default type.
		return ship;
	}

	/**
	 * Determines whether or not a ship can be placed in the area starting from
	 * the bow coordinates up to the ship length, moving in the specified
	 * direction. The criteria to determine if the area is suitable are: ships
	 * cannot overlap with any other ship, they cannot be adjacent to any other
	 * ship (either vertically, horizontally or diagonally) and they cannot
	 * exceed the ocean's borders.
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
		// treating vertical ships as horizontal ships with length equal to 1
		// and hight equal the length of the ship
		int height = horizontal ? 1 : shipLength;
		int length = horizontal ? shipLength : 1;

		// there is no need to check the mid area, where the ship will
		// actually be placed as long as we check the adjacent areas
		// (overlapping is guaranteed not to occur in the mid area, bow
		// excluded, since we are dropping the longest ships first). Under these
		// conditions the only place where ships can overlap in the mid area is
		// the location where the bow will be placed
		return !isOccupied(bowRow, bowColumn)
				&& areaAlongTheLengthIsClear(bowRow, bowColumn, length, height)
				&& areaAtEachEndIsClear(bowRow, bowColumn, length, height);
	}

	/**
	 * Determines whether the area adjacent the ship along its length is clear.
	 * Note horizontal ships have height equal to {@code 1} and length equal to
	 * the ship length, while vertical ships are treated as horizontal ships
	 * with length equal to {@code 1} and height equal to the ship length.
	 *
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param shipLength
	 *            length of the ship.
	 * @param shipHeight
	 *            height of the ship.
	 * @return {@code true} if the ship can be placed in the area respecting the
	 *         criteria.
	 */
	private boolean areaAlongTheLengthIsClear(int bowRow, int bowColumn, int shipLength, int shipHeight)
	{
		// row above the area that would host the ship
		int topRow = bowRow - 1;
		// row below the area that would host the ship
		int bottomRow = bowRow + shipHeight;
		// iteration variable used to scan the area
		int column;

		// checking both sides (top and bottom) along the length of the area
		// that could potentially host the ship, looping from 0 to length
		// (exclusive) as we do not need to check the diagonal area around the
		// ship
		for (int i = 0; i < shipLength; i++)
		{
			// iterate over the row
			column = bowColumn + i;

			if (column >= 0 && column < OCEAN_WIDTH)
			{
				if (topRow >= 0 && isOccupied(topRow, column)
						|| bottomRow < OCEAN_HEIGHT && isOccupied(bottomRow, column))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Determines whether the area adjacent the ship at both ends, including the
	 * diagonal area, is clear. Note horizontal ships have height equal to
	 * {@code 1} and length equal to the ship length, while vertical ships are
	 * treated as horizontal ships with length equal to {@code 1} and height
	 * equal to the ship length.
	 *
	 * @param bowRow
	 *            horizontal coordinate of the bow.
	 * @param bowColumn
	 *            vertical coordinate of the bow.
	 * @param shipLength
	 *            length of the ship.
	 * @param shipHeight
	 *            height of the ship.
	 * @return {@code true} if the ship can be placed in the area respecting the
	 *         criteria.
	 */
	private boolean areaAtEachEndIsClear(int bowRow, int bowColumn, int shipLength, int shipHeight)
	{
		// column on the left of area that would host the ship
		int leftColumn = bowColumn - 1;
		// column on the right of the area that would host the ship
		int rightColumn = bowColumn + shipLength;
		// iteration variable used to scan the area
		int row;

		// checking both ends (left and right) of the area that could
		// potentially host the ship, looping from -1 to height (inclusive) as
		// we also need to check the diagonal area around the ship
		for (int i = -1; i <= shipHeight; i++)
		{
			// iterate over the column
			row = bowRow + i;

			if (row >= 0 && row < OCEAN_HEIGHT)
			{
				if (leftColumn >= 0 && isOccupied(row, leftColumn)
						|| rightColumn < OCEAN_WIDTH && isOccupied(row, rightColumn))
				{
					return false;
				}
			}
		}

		return true;
	}
}
