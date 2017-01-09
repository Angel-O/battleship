package battleship;

/**
 * Represents a real {@linkplain Ship} of length equal to
 * {@value #CRUISER_LENGTH}.
 *
 * @author Angelo Oparah
 *
 */
public class Cruiser extends Ship
{
	/** length of a cruiser. */
	public static final int CRUISER_LENGTH = 3;

	/** cruiser type. */
	public static final String CRUISER_TYPE = "cruiser";


	/**
	 * Creates a real {@linkplain Ship} of length equal to
	 * {@value #CRUISER_LENGTH}.
	 */
	public Cruiser()
	{
		super(CRUISER_LENGTH);
	}

	@Override
	public String getShipType()
	{
		return CRUISER_TYPE;
	}
}
