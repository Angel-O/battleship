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
		this.length = length;
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
}
