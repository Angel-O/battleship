package battleship;

public class Bow extends ShipPart
{
	public Bow(int row, int column, boolean horizontal)
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
				if (getColumn() > 0)
				{
					return !(ships[getRow()][getColumn() - 1].isRealShip()
							|| ships[getRow() + 1][getColumn() - 1].isRealShip());
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
					return !(ships[getRow() - 1][getColumn() - 1].isRealShip()
							|| ships[getRow()][getColumn() - 1].isRealShip());
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
					return !(ships[getRow()][getColumn() - 1].isRealShip()
							|| ships[getRow() + 1][getColumn() - 1].isRealShip()
							|| ships[getRow() - 1][getColumn() - 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
		}
		else // vertical
		{
			if (getColumn() == 0)
			{
				if (getRow() > 0)
				{
					// check top and top-right
					return !(ships[getRow() - 1][getColumn()].isRealShip()
							|| ships[getRow() - 1][getColumn() + 1].isRealShip());
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
					// check top, top/left
					return !(ships[getRow() - 1][getColumn()].isRealShip()
							|| ships[getRow() - 1][getColumn() - 1].isRealShip());
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
					// check top, top/left, top/right
					return !(ships[getRow() - 1][getColumn()].isRealShip()
							|| ships[getRow() - 1][getColumn() + 1].isRealShip()
							|| ships[getRow() - 1][getColumn() - 1].isRealShip());
				}
				else
				{
					return true;
				}
			}
		}
	}
}
