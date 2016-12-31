package battleship;

public class ShipPart
{
	private int row;
	private int column;
	private boolean horizontal;

	public boolean isEmptyAround(Ship[][] ships)
	{
		return isEmptyAroundGeneric(ships) && isEmptyAroundDetail(ships);
	}

	public boolean isEmptyAroundGeneric(Ship[][] ships)
	{
		boolean empty = true;

		if (isHorizontal())
		{
			if (row == 0)
			{
				return !(ships[row + 1][column].isRealShip());
			}
			else if (row == Ocean.OceanHeight - 1)
			{
				return !(ships[row - 1][column].isRealShip());
			}
			else if (ships[row - 1][column].isRealShip() || (ships[row + 1][column].isRealShip()))
			{
				empty = false;
			}
		}
		else
		{
			if (column == 0)
			{
				return !(ships[row][column + 1].isRealShip());
			}
			else if (column == Ocean.OceanWidth - 1)
			{
				return !(ships[row][column - 1].isRealShip());
			}
			else if (ships[row][column - 1].isRealShip() || ships[row][column + 1].isRealShip())
			{
				empty = false;
			}
		}

		return empty;
	};

	public boolean isEmptyAroundDetail(Ship[][] ships)
	{
		return true;
	}

	public ShipPart(int row, int column, boolean horizontal)
	{
		this.row = row;
		this.column = column;
		this.setHorizontal(horizontal);
	}

	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * @return the horizontal
	 */
	public boolean isHorizontal()
	{
		return horizontal;
	}

	/**
	 * @param horizontal
	 *            the horizontal to set
	 */
	public void setHorizontal(boolean horizontal)
	{
		this.horizontal = horizontal;
	}
}
