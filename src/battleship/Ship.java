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
	}

	/**
	 * Returns the type of the ship
	 *
	 * @return the type of the ship
	 */
	public abstract String getShipType();

	/**
	 * If a part of the ship occupies the given row and column, and the ship
	 * hasn't been sunk, mark that part of the ship as hit (in the hit array,
	 * index 0 indicates the bow) and return true, otherwise return false.
	 *
	 * @return {@code true} if the ship was hit, {@code false} otherwise
	 */
	public boolean shootAt(int row, int col)
	{
		return false;
	}

	/**
	 * returns true if every part of the ship has been hit (and therefore the
	 * ship is sunk) false otherwise
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

	public int getLength()
	{
		return length;
	}

	public int getBowRow()
	{
		return bowRow;
	}

	public void setBowRow(int bowRow)
	{
		this.bowRow = bowRow;
	}

	public int getBowColumn()
	{
		return bowColumn;
	}

	public void setBowColumn(int bowColumn)
	{
		this.bowColumn = bowColumn;
	}

	public boolean isHorizontal()
	{
		return horizontal;
	}

	public void setHorizontal(boolean horizontal)
	{
		this.horizontal = horizontal;
	}

	/**
	 * Indicates whether or not the ship is real
	 *
	 * @return {@code true} if it's a real ship, {@code false} otherwise
	 */
	public boolean isRealShip()
	{
		return true;
	}
}
