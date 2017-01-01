package battleship;

public class Battleship extends Ship
{
	/** length of a battleship */
	public static final int BATTLESHIP_LENGTH = 4;

	public Battleship()
	{
		this(BATTLESHIP_LENGTH);
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
