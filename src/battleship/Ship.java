package battleship;

public abstract class Ship
{
	/** measures the length of the ship */
	private int length;

	protected Ship(int length)
	{
		this.length = length;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}
}
