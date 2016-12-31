package battleship;

public class Submarine extends Ship
{
	/** length of a submarine */
	private static final int submarineLength = 1;

	public Submarine()
	{
		this(submarineLength);
	}

	public Submarine(int length)
	{
		super(length);
	}

	@Override
	public String getShipType()
	{
		return "submarine";
	}

	@Override
	public String toString()
	{
		return "1";
	}

}
