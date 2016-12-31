package battleship;

public class Stern extends ShipPart
{
	public Stern(int row, int column, boolean horizontal)
	{
		super(row, column, horizontal);
	}

	@Override
	public boolean isEmptyAroundDetail(Ship[][] ships)
	{
		if (isHorizontal())
		{
			if (getRow() == 0)
			{
				if (getColumn() < Ocean.OceanWidth - 1)
				{
					// check right, bottom/right
					return !(ships[getRow()][getColumn() + 1].isRealShip()
							|| ships[getRow() - 1][getColumn() + 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
			else if (getRow() == Ocean.OceanHeight - 1)
			{
				if (getColumn() > 0)
				{
					// check top-right, right
					return !(ships[getRow() + 1][getColumn() + 1].isRealShip()
							|| ships[getRow()][getColumn() + 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
			else
			{
				if (getColumn() > 0)
				{
					// check right, top-right, bottom-right
					return !(ships[getRow()][getColumn() + 1].isRealShip()
							|| ships[getRow() + 1][getColumn() + 1].isRealShip()
							|| ships[getRow() - 1][getColumn() + 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
		}
		else // vertical
		{
			// left border
			if (getColumn() == 0)
			{
				if (getRow() < Ocean.OceanHeight - 1)
				{
					// check bottom and bottom-right
					return !(ships[getRow() + 1][getColumn()].isRealShip()
							|| ships[getRow() + 1][getColumn() + 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
			else if (getColumn() == Ocean.OceanWidth - 1)
			{
				if (getColumn() > 0)
				{
					// check bottom, bottom/left
					return !(ships[getRow() + 1][getColumn()].isRealShip()
							|| ships[getRow() + 1][getColumn() - 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
			else
			{
				if (getColumn() > 0)
				{
					// check bottom, bottom/left, bottom/right
					return !(ships[getRow() + 1][getColumn()].isRealShip()
							|| ships[getRow() + 1][getColumn() + 1].isRealShip()
							|| ships[getRow() + 1][getColumn() - 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
		}
	}
}
