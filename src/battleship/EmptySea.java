package battleship;

public class EmptySea extends Ship
{
	/** dummy length for an empty portion of sea */
	private static final int emptySeaLength = 0;

	public EmptySea()
	{
		super(emptySeaLength);
	}
}
