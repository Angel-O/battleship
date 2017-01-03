package battleship;

public class EmptySea extends Ship
{
	/** dummy length for an empty portion of sea */
	private static final int EMPTY_SEA_LENGTH = 1;

	public EmptySea()
	{
		super(EMPTY_SEA_LENGTH);
	}

	/** For an EmptySea it returns the string {@code empty sea} */
	@Override
	public String getShipType()
	{
		return "empty sea";
	}

	/** For an EmptySea it returns {@code false} */
	@Override
	public boolean isRealShip()
	{
		return false;
	}

	/** For an EmptySea it returns {@code false} */
	@Override
	public boolean isSunk()
	{
		return false;
	}

	/** For an EmptySea it returns {@code false} */
	@Override
	public boolean shootAt(int row, int col)
	{
		return false;
	}
}
