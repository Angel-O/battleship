package battleship;

/**
 * Represents a real {@linkplain Ship} of length equal to
 * {@value #BATTLESHIP_LENGTH}.
 *
 * @author Angelo Oparah
 *
 */
public class Battleship extends Ship
{
	/** length of a battleship. */
	public static final int BATTLESHIP_LENGTH = 4;

	/** battleship type. */
	public static final String battleshipType = "battleship";


	/**
	 * Creates a real {@linkplain Ship} of length equal to
	 * {@value #BATTLESHIP_LENGTH}.
	 */
	public Battleship()
	{
		super(BATTLESHIP_LENGTH);
	}

	@Override
	public String getShipType()
	{
		return battleshipType;
	}
}
