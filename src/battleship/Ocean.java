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

				if (isOccupied(bowRow, bowColumn))
				{
					// if there is a ship is already there, try a new position
					continue;
				}

				Ship ship = shipList.get(i);
				// set the bow coordinates for the current ship
				ship.setBowRow(bowRow);
				ship.setBowColumn(bowColumn);

				// set a random orientation for the current ship
				ship.setHorizontal(random.nextBoolean());

				// try and place the ship in the ocean
				int shipsPlaced = tryPlaceShipInTheOcean(ship, shipClass);

				// if not all of them were placed remove them
				// and try with new random coordinates
				if (shipsPlaced < shipLength)
				{
					// otherwise remove the ship parts placed so far and try
					// again with
					// new random coordinates
					removeShipFromTheOcean(ship, shipsPlaced);
					continue;
				}
				else if (!hasShipsAround(ship))
				{
					// if the ship was successfully dropped and there is nothing
					// around it go to the next ship in the list
					break;
				}
				else
				{
					// otherwise remove the (whole) ship just placed and try
					// again with new random coordinates
					removeShipFromTheOcean(ship, shipLength);

					continue;
				}
			}
			while (true);

		}
	}

	private boolean hasShipsAround(Ship ship)
	{
		boolean hasFrontShipsAround = false;
		boolean hasRearShipsAround = false;

		// get ship coordinates, orientation and data
		int row = ship.getBowRow();
		int column = ship.getBowColumn();
		boolean horizontal = ship.isHorizontal();
		int length = ship.getLength();

		// used for submarines and ship parts
		if (length < 1)
		{
			// checking corners
			if (row == 0 && column == 0)
			{
				return hasSternShipsAround(row, column) && hasSternShipsAroundVertical(row, column);
			}
			else if (row == 0 && column == Ocean.OceanWidth - 1)
			{
				return hasBowShipsAround(row, column) && hasSternShipsAroundVertical(row, column);
			}
			else if (row == Ocean.OceanHeight - 1 && column == 0)
			{
				return hasSternShipsAround(row, column) && hasBowShipsAroundVertical(row, column);
			}
			else if (row == Ocean.OceanHeight - 1 && column == Ocean.OceanWidth - 1)
			{
				return hasBowShipsAround(row, column) && hasBowShipsAroundVertical(row, column);
			}
			// if it's submarine (length == 1) we have to check has if we were
			// everywhere around us
			return horizontal
					? hasBowShipsAround(row, column) && hasSternShipsAround(row, column)
							&& hasMidPartShipsAround(row, column)
					: hasBowShipsAroundVertical(row, column) && hasSternShipsAroundVertical(row, column)
							&& hasMidPartShipsAroundVertical(row, column);
		}

		// checking adjacent areas across the ship's length
		for (int i = 0; i < length; i++)
		{
			// get the coordinates of the ship part based on the offset from the
			// bow and the orientation of the ship
			int shipPartRow = horizontal ? row : row + i;
			int shipPartColumn = horizontal ? column + i : column;

			if (i == 0)
			{
				// if it's horizontal the variable index is the column
				hasFrontShipsAround = horizontal ? hasBowShipsAround(row, column)
						: hasBowShipsAroundVertical(row, column);
			}
			else if (i == ship.getLength() - 1)
			{
				// if it's horizontal the variable index is the column
				hasRearShipsAround = horizontal ? hasSternShipsAround(shipPartRow, shipPartColumn)
						: hasSternShipsAroundVertical(shipPartRow, shipPartColumn);
			}
			else
			{
				// if it's horizontal the variable index is the column
				if (horizontal)
				{
					if (hasMidPartShipsAround(shipPartRow, shipPartColumn))
					{
						// return immediately to avoid overwriting its value
						// since the mid part can be made of multiple blacks
						return true;
					}
				}
				else
				{
					if (hasMidPartShipsAroundVertical(shipPartRow, shipPartColumn))
					{
						// return immediately to avoid overwriting its value
						// since the mid part can be made of multiple blacks
						return true;
					}
				}
			}
		}

		return hasFrontShipsAround && hasRearShipsAround;
	}

	private boolean hasMidPartShipsAround(int row, int column)
	{
		if (row == 0)
		{
			// check bottom
			return isOccupied(row + 1, column);
		}
		else if (row == Ocean.OceanHeight - 1)
		{
			// check top
			return isOccupied(row - 1, column);
		}
		else
		{
			// check top & bottom
			return isOccupied(row - 1, column) || isOccupied(row + 1, column);
		}
	}

	private boolean hasMidPartShipsAroundVertical(int row, int column)
	{
		if (column == 0)
		{
			// check right
			return isOccupied(row, column + 1);
		}
		else if (column == Ocean.OceanWidth - 1)
		{
			// check left
			return isOccupied(row, column - 1);
		}
		else
		{
			// check right & left
			return isOccupied(row, column + 1) || isOccupied(row, column - 1);
		}
	}

	// ALSO checking right side (column == 9)
	private boolean hasSternShipsAround(int row, int column)
	{
		if (row == 0 && column < Ocean.OceanWidth - 1)
		{
			// check right, bottom/right
			return isOccupied(row, column + 1) || isOccupied(row + 1, column + 1);
		}
		else if (row == Ocean.OceanHeight - 1 && column < Ocean.OceanWidth - 1)
		{
			// check right, top/right
			return isOccupied(row, column + 1) || isOccupied(row - 1, column + 1);
		}
		else if (column < Ocean.OceanWidth - 1)
		{
			// check right, top/right, bottom/right
			return isOccupied(row, column + 1) || isOccupied(row - 1, column + 1) || isOccupied(row + 1, column + 1);
		}
		// if column == 9 we need to check only top and bottom
		return hasMidPartShipsAround(row, column);
	}

	// ALSO checking bottom row (row == 9)
	private boolean hasSternShipsAroundVertical(int row, int column)
	{
		if (column == 0 && row < Ocean.OceanHeight - 1)
		{
			// check bottom, bottom/right
			return isOccupied(row + 1, column) || isOccupied(row + 1, column);
		}
		else if (column == Ocean.OceanWidth - 1 && row < Ocean.OceanHeight - 1)
		{
			// check bottom, bottom/left
			return isOccupied(row + 1, column) || isOccupied(row + 1, column - 1);
		}
		else if (row < Ocean.OceanHeight - 1)
		{
			// check bottom, bottom/left, bottom/right
			return isOccupied(row + 1, column) || isOccupied(row + 1, column - 1) || isOccupied(row + 1, column + 1);
		}
		// if row == 9 we only need to check left and right
		return hasMidPartShipsAroundVertical(row, column);
	}

	// ALSO checking left side (column == 0)
	private boolean hasBowShipsAround(int row, int column)
	{
		if (row == 0 && column > 0)
		{
			// check left, bottom/left
			return isOccupied(row, column - 1) || isOccupied(row + 1, column - 1);
		}
		else if (row == Ocean.OceanHeight - 1 && column > 0)
		{
			// check left, top/left
			return isOccupied(row, column - 1) || isOccupied(row - 1, column - 1);
		}
		else if (column > 0)
		{
			// check left, top/left, bottom/left
			return isOccupied(row, column - 1) || isOccupied(row - 1, column - 1) || isOccupied(row + 1, column - 1);
		}
		// if column = 0 we only need to check top and bottom
		return hasMidPartShipsAround(row, column);
	}

	// ALSO checking top row (row == 0)
	private boolean hasBowShipsAroundVertical(int row, int column)
	{
		if (column == 0 && row > 0)
		{
			// check top, top/right
			return isOccupied(row - 1, column) || isOccupied(row - 1, column + 1);
		}
		else if (column == OceanWidth - 1 && row > 0)
		{
			// check top, top/left
			return isOccupied(row - 1, column) || isOccupied(row - 1, column - 1);
		}
		else if (column > 0 && row > 0)
		{
			// check top, top/left, top/right
			return isOccupied(row - 1, column) || isOccupied(row - 1, column - 1) || isOccupied(row - 1, column + 1);
		}
		// if row == 0 we only need to check left and right
		return hasMidPartShipsAroundVertical(row, column);
	}

	private void removeShipFromTheOcean(Ship ship, int length)
	{
		int row = ship.getBowRow();
		int column = ship.getBowColumn();

		for (int i = 0; i < length; i++)
		{
			// if the ship is horizontal the variable index (i) is the column,
			// otherwise it's the row
			ships[ship.isHorizontal() ? row : row + i][ship.isHorizontal() ? column + i : column] = new EmptySea();
		}
	}

	private <T extends Ship> int tryPlaceShipInTheOcean(Ship ship, Class<T> shipClass)
	{
		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();
		// place the bow of the current ship in the ocean
		ships[bowRow][bowColumn] = ship;

		// starting from 1 as we just placed the bow
		int shipsPlacedSoFar = 1;

		// start from index one as the bow has already been placed
		for (int i = 1; i < ship.getLength(); i++)
		{
			// create a ship of length zero representing a ship part
			Ship shipPart = createShipPart(shipClass);

			// set the coordinates and the orientation of the ship part
			shipPart.setBowRow(ship.isHorizontal() ? ship.getBowRow() : ship.getBowRow() + i);
			shipPart.setBowColumn(ship.isHorizontal() ? ship.getBowColumn() + i : ship.getBowColumn());
			shipPart.setHorizontal(ship.isHorizontal());

			// place it in the ocean
			if (!isOccupied(shipPart.getBowRow(), shipPart.getBowColumn()))
			{
				ships[shipPart.getBowRow()][shipPart.getBowColumn()] = shipPart;
				shipsPlacedSoFar++;

				// check after the part was placed...
				if (hasShipsAround(shipPart))
				{
					removeShipFromTheOcean(shipPart, shipsPlacedSoFar);
					break;
				}
			}
			else
			{
				break;
			}
		}

		return shipsPlacedSoFar;
	}

	private <T extends Ship> ArrayList<T> populateShipList(int amount, Class<T> shipClass)
	{
		ArrayList<T> shipList = new ArrayList<>();

		for (int i = 0; i < amount; i++)
		{
			shipList.add(createShip(shipClass));
		}

		return shipList;
	}

	private <T extends Ship> T createShip(Class<T> shipClass)
	{
		T ship = null;

		try
		{
			ship = shipClass.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return ship;
	}

	private <T> Ship createShipPart(Class<T> shipClass)
	{
		Ship shipPart = null;

		if (shipClass == Battleship.class)
		{
			shipPart = new Battleship(0);
		}
		else if (shipClass == Cruiser.class)
		{
			shipPart = new Cruiser(0);
		}
		else if (shipClass == Destroyer.class)
		{
			shipPart = new Destroyer(0);
		}
		else
		{
			shipPart = new Submarine(0);
		}

		return shipPart;
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

	public void print()
	{
		for (int i = 0; i < OceanWidth; i++)
		{
			for (int j = 0; j < OceanHeight; j++)
			{
				System.out.print(ships[i][j] + " ");
			}
			System.out.println();
		}
	}

}
