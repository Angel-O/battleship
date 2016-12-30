package battleship;

public class Ocean
{
	/** matrix to access ships in the ocean */
	private Ship[][] ships;

	/** count of the total shots fired at any given time of the game */
	private int shotsFired;

	/** count of the total hits */
	private int hitCount;

	/** number of ships sunk */
	private int shipsSunk;

	/** max height of the Ocean */
	public static final int OceanHeight = 10;

	/** max length of the Ocean */
	public static final int OceanWidth = 10;

	public Ocean()
	{
		ships = InitializeOcean();
	}

	/**
	 * Sets {@link EmptySea} in the ocean.
	 *
	 * @return a matrix containing all EmptySea instances representing the
	 *         initial state of the ocean before any ship is paced onto it
	 *
	 */
	private static final Ship[][] InitializeOcean()
	{
		Ship[][] ships = new Ship[OceanWidth][OceanHeight];

		for (int i = 0; i < OceanWidth; i++)
		{
			for (int j = 0; j < OceanHeight; j++)
			{
				ships[i][j] = new EmptySea();
			}
		}

		return ships;
	}

	/**
	 * Returns the number of shots fired.
	 *
	 * @return the number of shots fired.
	 */
	public int getShotsFired()
	{
		return shotsFired;
	}

	/**
	 * Returns the hit count.
	 *
	 * @return the current hit count.
	 */
	public int getHitCount()
	{
		return hitCount;
	}

	/**
	 * Returns the number of ships sunk
	 *
	 * @return the number of ships sunk
	 */
	public int getShipsSunk()
	{
		return shipsSunk;
	}

	public boolean isGameOver()
	{
		return true;
	}

	public Ship[][] getShipArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void placeAllShipsRandomly()
	{
		// TODO Auto-generated method stub

	}
}
