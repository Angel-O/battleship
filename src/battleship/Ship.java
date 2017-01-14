package battleship;


/**
 * Represents a ship of variable length in the {@linkplain Ocean}. Each ship
 * holds information about the coordinates of the bow, the length of the ship
 * and its orientation, vertical or horizontal.
 *
 * @author Angelo Oparah
 */
public abstract class Ship
{
	/** measures the length of the ship. */
	private int length;

	/** indicates the vertical position of the front of the ship. */
	private int bowRow;

	/** indicates the horizontal position of the front of the ship. */
	private int bowColumn;

	/** set to true to indicate that the ship occupies a single row. */
	private boolean horizontal;

	/** indicates what part of the ship has been hit. */
	private boolean[] hit;


	/**
	 * Builds a part of a ship that will be placed onto the {@linkplain Ocean}
	 * and sets its initial state to 'no-hits', meaning no part of the ship has
	 * suffered a shot. Note: the length of the ship instead refers to the whole
	 * ship so each ship part should have the same length. The length must be
	 * less than both the {@linkplain Ocean}'s height and width to ensure that
	 * the ship can be placed anywhere in the ocean regardless of its
	 * orientation.
	 *
	 *
	 * @param length
	 *            the length of the ship; must be greater than zero.
	 * @throws IllegalArgumentException
	 *             if the length provided is less or equal to zero or greater
	 *             than the {@linkplain Ocean} boundaries.
	 */
	public Ship(int length)
	{
		if (length <= 0)
		{
			throw new IllegalArgumentException("Illegal non positive value for ship length: " + length);
		}
		if (length > Ocean.OCEAN_HEIGHT || length > Ocean.OCEAN_WIDTH)
		{
			throw new IllegalArgumentException("Illegal out of range value for ship length: " + length);
		}

		this.length = length;
		hit = new boolean[length];
	}

	/**
	 * Returns the type of the ship.
	 *
	 * @return a {@code string} indicating the type of the ship.
	 */
	public abstract String getShipType();

	/**
	 * Shoots at the part of the ship placed at the given location and marks
	 * that part of the ship as hit.
	 *
	 * @param row
	 *            represents the vertical coordinate to be hit.
	 * @param column
	 *            represents the horizontal coordinate to be hit.
	 * @return {@code true} if the ship is not sunk, {@code false} otherwise.
	 */
	public boolean shootAt(int row, int column)
	{
		if (!isSunk())
		{
			// get the offset from the bow of the shot fired at the location
			// provided, based on the orientation of the ship
			int offsetFromTheBow = horizontal ? column - bowColumn : row - bowRow;

			// mark the hit array accordingly to indicate what
			// part of the ship (as a whole) was hit.
			hit[offsetFromTheBow] = true;

			return isRealShip();
		}

		return false;
	}

	/**
	 * Indicates if the ship has been sunk.
	 *
	 * @return {@code true} if every part of the ship has been hit {@code false}
	 *         otherwise.
	 */
	public boolean isSunk()
	{
		for (boolean shipPartWasHit : hit)
		{
			if (!shipPartWasHit)
			{
				// return false soon as a "safe" (hit == false) part of the ship
				// is found
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the length of the ship.
	 *
	 * @return the length of the ship.
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * Returns the vertical coordinate of the bow.
	 *
	 * @return the vertical coordinate of the bow.
	 */
	public int getBowRow()
	{
		return bowRow;
	}

	/**
	 * Sets the vertical coordinate of the bow.
	 *
	 * @param bowRow
	 *            value to set the vertical coordinate to; must be greater or
	 *            equal to zero and less than the {@linkplain Ocean} Height.
	 * @throws IllegalArgumentException
	 *             if the coordinate falls outside the {@linkplain Ocean}
	 *             borders
	 */
	public void setBowRow(int bowRow)
	{
		if (bowRow < 0 || bowRow > Ocean.OCEAN_HEIGHT)
		{
			throw new IllegalArgumentException("Illegal out of range value for bowRow: " + bowRow);
		}
		this.bowRow = bowRow;
	}

	/**
	 * Returns the horizontal coordinate of the bow.
	 *
	 * @return the horizontal coordinate of the bow.
	 */
	public int getBowColumn()
	{
		return bowColumn;
	}

	/**
	 * Sets the horizontal coordinate of the bow.
	 *
	 * @param bowColumn
	 *            value to set the vertical coordinate to; must be greater or
	 *            equal to zero and less than the {@linkplain Ocean} Width.
	 * @throws IllegalArgumentException
	 *             if the coordinate falls outside the {@linkplain Ocean}
	 *             borders
	 */
	public void setBowColumn(int bowColumn)
	{
		if (bowColumn < 0 || bowColumn > Ocean.OCEAN_WIDTH)
		{
			throw new IllegalArgumentException("Illegal out of range value for bowColumn: " + bowColumn);
		}
		this.bowColumn = bowColumn;
	}

	/**
	 * Refers the orientation of the ship. A ship will be placed horizontally in
	 * the {@linkplain Ocean} if this value is set to true.
	 *
	 * @return {@code true} if the ship is positioned horizontally,
	 *         {@code false} otherwise
	 */
	public boolean isHorizontal()
	{
		return horizontal;
	}

	/**
	 * Sets the orientation of the ship in the {@linkplain Ocean}.
	 *
	 * @param horizontal
	 *            {@code boolean} flag indicating the horizontal orientation of
	 *            the ship. A {@code true} value sets it to horizontal, a
	 *            {@code false} value sets it to vertical.
	 */
	public void setHorizontal(boolean horizontal)
	{
		this.horizontal = horizontal;
	}

	/**
	 * Indicates whether or not the ship is real. By default it returns
	 * {@code true}: subclasses might override this method accordingly.
	 *
	 * @return {@code true} (default value) if it's a real ship, {@code false}
	 *         otherwise.
	 */
	public boolean isRealShip()
	{
		return true;
	}

	/**
	 * Returns a string representing the current state of the ship. "S"
	 * indicates a location that was fired upon and hit a ship; "-" indicates a
	 * location that was fired upon without hitting any ship and "." to
	 * indicates a location that is yet to be fired upon. Note: if the ship is
	 * sunk it will be marked with "x" by the {@linkplain Ocean} when displaying
	 * the grid, regardless of the representation returned by this method.
	 */
	@Override
	public String toString()
	{
		String state = "";

		for (boolean shipPartWasHit : hit)
		{
			// if the ship part was hit add an "S" for real ships
			// or a "-" for empty sea spots, otherwise add a "."
			state += shipPartWasHit ? (isRealShip() ? "S" : "-") : ".";
		}

		return state;
	}
}
