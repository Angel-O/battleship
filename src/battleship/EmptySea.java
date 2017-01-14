package battleship;

/**
 * Represents a dummy {@linkplain Ship} of length equal to
 * {@value #EMPTY_SEA_LENGTH}.
 *
 * @author Angelo Oparah
 *
 */
public class EmptySea extends Ship
{
	/** length of an empty portion of sea. */
	public static final int EMPTY_SEA_LENGTH = 1;

	/** empty sea type. */
	public static final String EMPTY_SEA_TYPE = "empty sea";


	/**
	 * Creates a dummy {@linkplain Ship} of length equal to
	 * {@value #EMPTY_SEA_LENGTH}, representing an empty sea location in the
	 * {@linkplain Ocean}.
	 */
	public EmptySea()
	{
		super(EMPTY_SEA_LENGTH);
	}

	/**
	 * {@inheritDoc} For an EmptySea it returns the string
	 * {@value #EMPTY_SEA_TYPE}.
	 */
	@Override
	public String getShipType()
	{
		return EMPTY_SEA_TYPE;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRealShip()
	{
		return false;
	}

	/** {@inheritDoc} For an EmptySea it will always return {@code false}. */
	@Override
	public boolean isSunk()
	{
		return false;
	}

	/** {@inheritDoc} For an EmptySea it will always return {@code false}. */
	@Override
	public boolean shootAt(int row, int col)
	{
		// go up to the super class to mark the hit array and return false (by
		// default the superclass returns true only if the location hit contains
		// a real ship, so for an empty sea will always return false)
		return super.shootAt(row, col);
	}
}
