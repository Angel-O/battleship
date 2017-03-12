package battleship;

import java.util.Random;

/**
 * Represents the {@value #OCEAN_WIDTH} x {@value #OCEAN_HEIGHT} matrix where
 * {@value #BATTLESHIPS} {@linkplain Battleship}(s), {@value #CRUISERS}
 * {@linkplain Cruiser}(s), {@value #DESTROYERS} {@linkplain Destroyer}(s),
 * {@value #SUBMARINES} {@linkplain Submarine}(s) are randomly placed onto. It
 * keeps a count of the number of shots fired during the game, the number of
 * successful hits and the number of ships that have been sunk.
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
		ships = new Ship[OCEAN_HEIGHT][OCEAN_WIDTH];

		for (int i = 0; i < OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < OCEAN_WIDTH; j++)
			{
				// create an empty sea with bow at row "i" and column "j"
				// and drop it onto the ocean, the orientation is irrelevant:
				// we will set it to horizontal (true)
				ships[i][j] = createShip(EmptySea.class, i, j, true);
			}
		}
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
	 * Indicates if the game is still on or not.
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
	 * not overlap and are not adjacent to other ships either vertically,
	 * horizontally, or diagonally.
	 */
	public final void placeAllShipsRandomly()
	{
		// Longer ones are placed first to increase the chances of dropping all
		// the ships
		this.<Battleship>placeShipsOntoOcean(BATTLESHIPS, Battleship.BATTLESHIP_LENGTH, Battleship.class);
		this.<Cruiser>placeShipsOntoOcean(CRUISERS, Cruiser.CRUISER_LENGTH, Cruiser.class);
		this.<Destroyer>placeShipsOntoOcean(DESTROYERS, Destroyer.DESTROYER_LENGTH, Destroyer.class);
		this.<Submarine>placeShipsOntoOcean(SUBMARINES, Submarine.SUBMARINE_LENGTH, Submarine.class);
	}

	/**
	 * Checks whether or not the location specified contains a real ship. A real
	 * ship is an actual ship, that is any ship in the ocean that is not a
	 * {@linkplain EmptySea}. Note: if the coordinates given are out of range
	 * the method returns {@code false}.
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
		if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
		{
			// check only if the coordinates are in range
			return ships[row][column].isRealShip();
		}

		return false;
	}

	/**
	 * Returns the ship type at the given location. Will throw an exception if
	 * the coordinates provided are out of range.
	 *
	 * @param row
	 *            vertical coordinate of the ship.
	 * @param column
	 *            horizontal coordinate of the ship.
	 * @return the type of the ship at the location specified.
	 * @throws IllegalArgumentException
	 *             if the coordinates provided fall outside the
	 *             {@linkplain Ocean} boundaries.
	 */
	public String getShipTypeAt(int row, int column)
	{
		if (row < 0 || row >= OCEAN_HEIGHT)
		{
			throw new IllegalArgumentException("Illegal out of range value for row: " + row);
		}
		if (column < 0 || column >= OCEAN_WIDTH)
		{
			throw new IllegalArgumentException("Illegal out of range value for column: " + column);
		}

		return ships[row][column].getShipType();
	}

	/**
	 * Indicates if the given location contains a sunken ship. Note: if the
	 * coordinates given are out of range the method returns {@code false}.
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
		if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
		{
			// check only if the coordinates are in range
			return ships[row][column].isSunk();
		}

		return false;
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
				else if (i > 0)
				{
					// otherwise print whatever is in the ocean
					System.out.print(getShipStateAt(i - 1, j - 1));
				}
			}
			// print the next row on a separate line
			System.out.println();
		}
	}

	/**
	 * Shoots at the location correspondent to the given coordinates and updates
	 * the shot and the hit count. If a location contains a real ship, it
	 * returns true every time the user shoots at that same location. Once a
	 * ship has been sunk, additional shots at the same location will return
	 * {@code false}. Note: the method will also return {@code false} if the
	 * location falls outside the ocean borders.
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
		// increment the total number of shots fired, regardless
		shotsFired++;

		// try and hit ships only within the ocean's borders
		if (row >= 0 && row < OCEAN_HEIGHT && column >= 0 && column < OCEAN_WIDTH)
		{
			if (ships[row][column].shootAt(row, column))
			{
				// increment the hit count in case of a successful shot
				hitCount++;

				if (ships[row][column].isSunk())
				{
					// increment the count of the sunk ships if this shot
					// sunk the ship
					shipsSunk++;
				}

				return true;
			}
		}

		return false;
	}


	// ============== private methods ============= //

	/**
	 * Factory method to generate ship parts of the given type. It will set the
	 * bow coordinates and orientation to the values passed as arguments. It can
	 * return a null value if the class passed as parameter is not recognized.
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
	private static <T extends Ship> Ship createShip(Class<T> shipClass, int bowRow, int bowColumn, boolean horizontal)
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
		else if (shipClass == EmptySea.class)
		{
			ship = new EmptySea();
		}

		if (ship != null)
		{
			// set the bow coordinates and the orientation of the ship
			ship.setBowRow(bowRow);
			ship.setBowColumn(bowColumn);
			ship.setHorizontal(horizontal);
		}

		// can return null (rather than a ship of a default ship type): think
		// about future extensibility. If we add a new ship type but
		// forget to add it in this method, the assertion used after calling
		// this method will fail and we will know exactly what happened, rather
		// than wondering why we get extra ships of a given default type.
		return ship;
	}

	/**
	 * Returns a {@code char} representing the current state of the ship part at
	 * the given location. If the location contains a sunk {@linkplain Ship}
	 * (considered as a whole) the ship state will be overwritten with a "x" to
	 * indicate that the whole ship was sunk. Otherwise the ship state will be
	 * returned. Note: the {@linkplain Ship} class will mark a location that is
	 * yet to be fired upon with a "." (dot) , a location that was fired upon
	 * hitting a ship with a "S" and a location that was fired upon without
	 * hitting any ship with a "-" (dash).
	 *
	 * @param row
	 *            horizontal coordinate of the ship part to display.
	 * @param column
	 *            vertical coordinate of the ship part to display
	 * @return a {@code char} representing the state of the part of the ship at
	 *         the given location.
	 */
	private char getShipStateAt(int row, int column)
	{
		assert row >= 0 && row < OCEAN_HEIGHT : "row coordinate out of range: " + row;
		assert column >= 0 && column < OCEAN_WIDTH : "column coordinate out of range: " + column;

		if (!hasSunkShipAt(row, column))
		{
			// if the ship is not sunk get the relevant char from the ship state
			// based on orientation and offset from the bow
			int bowRow = ships[row][column].getBowRow();
			int bowColumn = ships[row][column].getBowColumn();
			boolean horizontal = ships[row][column].isHorizontal();

			String shipState = ships[row][column].toString();

			return shipState.charAt(horizontal ? column - bowColumn : row - bowRow);
		}

		// otherwise return an 'x', meaning the ship is sunk
		return 'x';
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

		while (shipPlaced < amount)
		{
			// get a random orientation for the current ship
			boolean horizontal = random.nextBoolean();

			// get random coordinates for the bow within the ocean's boundaries,
			// with the upper bound depending on the orientation of the ship
			int bowColumn = random.nextInt(horizontal ? OCEAN_WIDTH - shipLength + 1 : OCEAN_WIDTH);
			int bowRow = random.nextInt(horizontal ? OCEAN_HEIGHT : OCEAN_HEIGHT - shipLength + 1);

			// try and place the (whole) ship onto the ocean
			if (areaIsSuitableToPlaceShip(bowRow, bowColumn, shipLength, horizontal))
			{
				// create a ship representing a ship part to replicate
				// across the length of the (whole) ship in the given direction
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
	 *         criteria, {@code false} otherwise.
	 */
	private boolean areaIsSuitableToPlaceShip(int bowRow, int bowColumn, int shipLength, boolean horizontal)
	{
		// treating vertical ships as horizontal ships with length equal to 1
		// and height equal the length of the ship
		int height = horizontal ? 1 : shipLength;
		int length = horizontal ? shipLength : 1;

		// loop from bowRow(bowColumn) - 1 up to the ship height(length),
		// inclusively as we are checking the augmented virtual area sorrounding
		// the ocean locations where the (whole) ship will be placed
		for (int i = bowRow - 1; i <= bowRow + height; i++)
		{
			for (int j = bowColumn - 1; j <= bowColumn + length; j++)
			{
				if (isOccupied(i, j))
				{
					return false;
				}
			}
		}

		return true;
	}
}
