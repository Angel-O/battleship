package battleship;

public class Submarine extends Ship
{
	/** length of a submarine */
	private static final int submarineLength = 1;

	public Submarine()
	{
		super(submarineLength);
	}

	@Override
	public String getShipType()
	{
		return "submarine";
	}

}
