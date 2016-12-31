package battleship;

import java.util.ArrayList;
import java.util.Random;

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

	private static final int Battleships = 1;
	private static final int Cruisers = 2;
	private static final int Destroyers = 3;
	private static final int Submarines = 4;


	public Ocean()
	{
		ships = InitializeOcean();
	}

	/**
	 * Sets {@link EmptySea} in the ocean.
	 *
	 * @return a matrix containing all EmptySea instances representing the
	 *         initial state of the ocean before any ship is placed onto it
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
		return ships;
	}

	public void placeAllShipsRandomly()
	{
		this.<Battleship>placeShips(Battleships, new ArrayList<Battleship>(), Battleship.class);
		this.<Cruiser>placeShips(Cruisers, new ArrayList<Cruiser>(), Cruiser.class);
		this.<Destroyer>placeShips(Destroyers, new ArrayList<Destroyer>(), Destroyer.class);
		this.<Submarine>placeShips(Submarines, new ArrayList<Submarine>(), Submarine.class);
	}

	private <T extends Ship> void placeShips(int amount, ArrayList<T> shipList, Class<T> shipClass)
	{
		// populate the ship list with the correct amount of ships of the right
		// type
		shipList = populateShipList(amount, shipClass);

		// get the length of the ship type
		int shipLength = shipList.get(0).getLength();

		Random random = new Random();

		for (int i = 0; i < shipList.size(); i++)
		{
			do
			{
				// get random coordinates for the current ship bow
				int bowRow = random.nextInt(OceanWidth - shipLength);
				int bowColumn = random.nextInt(OceanHeight - shipLength);

				if (ships[bowRow][bowColumn].isRealShip())
				{
					// if there is a ship is already there, try a new position
					continue;
				}

				// set the bow coordinates for the current ship
				shipList.get(i).setBowRow(bowRow);
				shipList.get(i).setBowColumn(bowColumn);

				// set a random orientation for the current ship
				shipList.get(i).setHorizontal(random.nextBoolean());

				// place the current ship in the ocean
				ships[bowRow][bowColumn] = shipList.get(i);

				if (ships[bowRow][bowColumn].hasNotAdjacentShipsAround(ships))
				{
					// if there is nothing around it go to the next ship in the
					// list
					break;
				}
				else
				{
					// otherwise remove the ship just placed and try again with
					// new random coordinates
					ships[bowRow][bowColumn] = new EmptySea();
					continue;
				}
			}
			while (true);

		}
	}

	private <T> ArrayList<T> populateShipList(int amount, Class<T> shipClass)
	{
		ArrayList<T> shipList = new ArrayList<>();

		for (int i = 0; i < amount; i++)
		{
			try
			{
				shipList.add(shipClass.newInstance());
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		return shipList;
	}

	public boolean isOccupied(int row, int column)
	{
		return ships[row][column].isRealShip();
	}

	public Object getShipTypeAt(int i, int j)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
