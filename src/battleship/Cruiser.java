package battleship;

public class Cruiser extends Ship
{
	/** length of a cruiser */
	private static final int cruiserLength = 3;

	public Cruiser()
	{
		this(cruiserLength);
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
