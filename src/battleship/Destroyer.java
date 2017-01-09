package battleship;

/**
 * Represents a real {@linkplain Ship} of length equal to
 * {@value #DESTROYER_LENGTH}.
 *
 * @author Angelo Oparah
 *
 */
public class Destroyer extends Ship
{
	/** length of a destroyer. */
	public static final int DESTROYER_LENGTH = 2;

	/** destroyer type. */
	public static final String DESTROYER_TYPE = "destroyer";


	/**
	 * Creates a real {@linkplain Ship} of length equal to
	 * {@value #DESTROYER_LENGTH}.
	 */
	public Destroyer()
	{
		super(DESTROYER_LENGTH);
	}

	@Override
	public String getShipType()
	{
		return DESTROYER_TYPE;
	}
}
