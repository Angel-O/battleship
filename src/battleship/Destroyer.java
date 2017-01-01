package battleship;

public class Destroyer extends Ship
{
	/** length of a destroyer */
	public static final int DESTROYER_LENGTH = 2;

	public Destroyer()
	{
		this(DESTROYER_LENGTH);
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
