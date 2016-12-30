package battleship;

public abstract class Ship
{
	/** measures the length of the ship */
	private int length;

	/** indicates the vertical position of the front of the ship */
	private int bowRow;

	/** indicates the horizontal position of the front of the ship */
	private int bowColumn;

	/** set to true to indicate that the ship occupies a single row */
	private boolean horizontal;

	/** indicates what part of the ship has been hit */
	private boolean[] hit;

	protected Ship(int length)
	{
		assert length > 0 : "ship length cannot be negative";
		this.length = length;
		hit = new boolean[length];
	}

	/**
	 * Returns the type of the ship
	 *
	 * @return a string indicating the type of the ship
	 */
	public abstract String getShipType();

	/**
	 * If a part of the ship occupies the given row and column, and the ship
	 * hasn't been sunk, mark that part of the ship as hit (in the hit array,
	 * index 0 indicates the bow) and return true, otherwise return false.
	 *
	 * @param row
	 *            represents the vertical coordinate to be hit
	 * @param column
	 *            represents the horizontal coordinate to be hit
	 * @return {@code true} if the ship was hit, {@code false} otherwise
	 */
	public boolean shootAt(int row, int column)
	{
		if (!isSunk() && trajectoryIsRight(row, column))
		{
			// if the ship is horizontal check the horizontal range and try
			// and shoot, otherwise check the vertical range.
			return isHorizontal() ? checkRangetAndShoot(bowColumn, column) : checkRangetAndShoot(bowRow, row);
		}
		return false;
	}

	/**
	 * Checks that the shot fired has potential to hit the ship, by checking its
	 * trajectory. A ship can only be hit with a horizontal or vertical
	 * trajectory.
	 *
	 * @param row
	 *            vertical coordinate of the shot
	 * @param horizontal
	 *            coordinate of the shot
	 * @return {@code true} if the trajectory is either horizontal or vertical,
	 *         {@code false} otherwise
	 */
	private boolean trajectoryIsRight(int row, int column)
	{
		// horizontal trajectory: target row is equal to bow row
		// vertical trajectory: target column is equal to bow column
		return row == bowRow || column == bowColumn;
	}

	/**
	 * Checks if the ship is in range and if so it will mark the hit point.
	 * Returns true in case of a successful shot, false otherwise.
	 *
	 * @param bowCoordinate
	 *            coordinate of the bow. If the ship is horizontal the bow
	 *            column coordinate should be used
	 * @param targetCoordinate
	 *            coordinate targeted by the hit. If the ship is horizontal the
	 *            target column coordinate should be used
	 * @return {@code true} if it's a hit, {@code false} it if's a miss
	 */
	private boolean checkRangetAndShoot(int bowCoordinate, int targetCoordinate)
	{
		// Add the length to the relevant coordinate
		// and subtract one since the ship will stretch from the bow to length -
		// 1 at most.
		if (bowCoordinate + length - 1 >= targetCoordinate)
		{
			// mark the hit point
			hit[targetCoordinate - bowCoordinate] = true;
			return true;
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
	 * Returns the length of the ship
	 *
	 * @return the length of the ship
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * Returns the vertical coordinate of the bow
	 *
	 * @return the vertical coordinate of the bow
	 */
	public int getBowRow()
	{
		return bowRow;
	}

	/**
	 * Sets the vertical coordinate of the bow
	 *
	 * @param bowRow
	 *            value to set the vertical coordinate to
	 */
	public void setBowRow(int bowRow)
	{
		this.bowRow = bowRow;
	}

	/**
	 * Returns the horizontal coordinate of the bow
	 *
	 * @return the horizontal coordinate of the bow
	 */
	public int getBowColumn()
	{
		return bowColumn;
	}

	/**
	 * Sets the horizontal coordinate of the bow
	 *
	 * @param bowColumn
	 *            value to set the vertical coordinate to
	 */
	public void setBowColumn(int bowColumn)
	{
		this.bowColumn = bowColumn;
	}

	/**
	 * Refers the orientation of the ship
	 *
	 * @return {@code true} if the ship is positioned horizontally,
	 *         {@code false} otherwise
	 */
	public boolean isHorizontal()
	{
		return horizontal;
	}

	/**
	 * Sets the orientation of the ship
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
	 * Indicates whether or not the ship is real
	 *
	 * @return {@code true} (default value) if it's a real ship, {@code false}
	 *         otherwise.
	 */
	public boolean isRealShip()
	{
		return true;
	}
}
