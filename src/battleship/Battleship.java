package battleship;

public class Battleship extends Ship
{
	/** length of a battleship */
	private static final int battleshipLength = 4;

	public Battleship()
	{
		this(battleshipLength);
	}

	public Battleship(int length)
	{
		super(length);
	}

	@Override
	public String getShipType()
	{
		return "battleship";
	}

	@Override
	public String toString()
	{
		return "4";
	}
}
