package battleship;

public class Destroyer extends Ship
{
	/** length of a destroyer */
	private static final int destroyerLength = 2;

	public Destroyer()
	{
		super(destroyerLength);
	}

	@Override
	public String getShipType()
	{
		return "destroyer";
	}
}
