package battleship;

public class Submarine extends Ship
{
	/** length of a submarine */
	public static final int SUBMARINE_LENGTH = 1;

	public Submarine()
	{
		this(SUBMARINE_LENGTH);
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
		return isHorizontal() ? "H" : "V";
	}

}
