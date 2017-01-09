package battleship;

/**
 * Represents a real {@linkplain Ship} of length equal to
 * {@value #SUBMARINE_LENGTH}.
 *
 * @author Angelo Oparah
 *
 */
public class Submarine extends Ship
{
	/** length of a submarine. */
	public static final int SUBMARINE_LENGTH = 1;

	/** submarine type. */
	public static final String SUBMARINE_TYPE = "submarine";


	/**
	 * Creates a real {@linkplain Ship} of length equal to
	 * {@value #SUBMARINE_LENGTH}.
	 */
	public Submarine()
	{
		super(SUBMARINE_LENGTH);
	}

	@Override
	public String getShipType()
	{
		return SUBMARINE_TYPE;
	}
}
