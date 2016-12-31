package battleship;

public class Destroyer extends Ship
{
	/** length of a destroyer */
	private static final int destroyerLength = 2;

	public Destroyer()
	{
		this(destroyerLength);
	}

	public Destroyer(int length)
	{
		super(length);
	}

	@Override
	public String getShipType()
	{
		return "destroyer";
	}

	@Override
	public String toString()
	{
		return "2";
	}
}
