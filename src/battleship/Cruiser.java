package battleship;

public class Cruiser extends Ship
{
	/** length of a cruiser */
	public static final int CRUISER_LENGTH = 3;

	public Cruiser()
	{
		this(CRUISER_LENGTH);
	}

	public Cruiser(int length)
	{
		super(length);
	}

	@Override
	public String getShipType()
	{
		return "cruiser";
	}

	@Override
	public String toString()
	{
		return "3";
	}
}
