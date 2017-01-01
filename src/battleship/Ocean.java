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
		this.<Battleship>placeShips(Battleships, Battleship.class);
		this.<Cruiser>placeShips(Cruisers, Cruiser.class);
		this.<Destroyer>placeShips(Destroyers, Destroyer.class);
		this.<Submarine>placeShips(Submarines, Submarine.class);
	}

	private <T extends Ship> void placeShips(int amount, Class<T> shipClass)
	{
		// populate the ship list with the correct amount of ships of the right
		// type
		ArrayList<T> shipList = populateShipList(amount, shipClass);

		// get the length of the ship type
		int shipLength = shipList.get(0).getLength();

		Random random = new Random();

		for (int i = 0; i < shipList.size(); i++)
		{
			do
			{
				// get random coordinates for the current ship bow (the maximum
				// value valid for the bow
				// is the width/height of the ocean minus the ship length: this
				// avoids the ship to overflow.
				// Adding 1 as the higher boundary of the nextInt method is non
				// inclusive)
				int bowRow = random.nextInt(Ocean.OceanWidth - shipLength + 1);
				int bowColumn = random.nextInt(Ocean.OceanHeight - shipLength + 1);

				if (isOccupied(bowRow, bowColumn) || !sorroundingAreaIsClear(bowRow, bowColumn))
				{
					// if there is a ship is already there or the area around is
					// not clear, try a new position
					continue;
				}

				Ship ship = shipList.get(i);
				// set the bow coordinates for the current ship
				ship.setBowRow(bowRow);
				ship.setBowColumn(bowColumn);

				// set a random orientation for the current ship
				ship.setHorizontal(random.nextBoolean());

				// try and place the ship in the ocean
				if (tryPlaceShipInTheOcean(ship, shipClass))
				{
					// if the ship was successfully dropped go to the next ship
					// in the list, otherwise try again with new random
					// coordinates
					break;
				}
			}
			while (true);
		}
	}

	// adjacency check before dropping the bow of the ship (scans the whole area
	// for a given location)
	private boolean sorroundingAreaIsClear(int row, int column)
	{
		if (row == 0)
		{
			// top edge
			if (column == 0)
			{
				// left corner
				return bottomIsClear(row, column) && bottomRightIsClear(row, column) && rightIsClear(row, column);
			}
			else if (column == Ocean.OceanWidth - 1)
			{
				// right corner
				return bottomIsClear(row, column) && bottomLeftIsClear(row, column) && leftIsClear(row, column);
			}
			// mid part of the top edge
			return rightIsClear(row, column) && bottomIsClear(row, column) && bottomRightIsClear(row, column)
					&& bottomLeftIsClear(row, column) && leftIsClear(row, column);
		}
		else if (row == Ocean.OceanHeight - 1)
		{
			// bottom edge
			if (column == 0)
			{
				// left corner
				return topIsClear(row, column) && topRightIsClear(row, column) && rightIsClear(row, column);
			}
			else if (column == Ocean.OceanWidth - 1)
			{
				// right corner
				return topIsClear(row, column) && topLeftIsClear(row, column) && leftIsClear(row, column);
			}
			// mid part of the bottom edge
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column)
					&& leftIsClear(row, column) && topLeftIsClear(row, column);
		}
		else if (column == 0)
		{
			// left edge (only the mid part as the corners were considered
			// above)
			return topIsClear(row, column) && topRightIsClear(row, column) && rightIsClear(row, column)
					&& bottomRightIsClear(row, column) && bottomIsClear(row, column);

		}
		else if (column == Ocean.OceanWidth - 1)
		{
			// right edge (only the mid part as the corners were considered
			// above)
			return topIsClear(row, column) && topLeftIsClear(row, column) && leftIsClear(row, column)
					&& bottomLeftIsClear(row, column) && bottomIsClear(row, column);
		}
		else
		{
			// mid area of the ocean (no borders, no corners)
			return rightIsClear(row, column) && bottomRightIsClear(row, column) && bottomIsClear(row, column)
					&& bottomLeftIsClear(row, column) && leftIsClear(row, column) && topLeftIsClear(row, column)
					&& topIsClear(row, column) && topRightIsClear(row, column);
		}
	}

	private void removeShipFromTheOcean(Ship ship, int length)
	{
		int row = ship.getBowRow();
		int column = ship.getBowColumn();
		boolean horizontal = ship.isHorizontal();

		for (int i = 0; i < length; i++)
		{
			// if the ship is horizontal the variable index (i) is the column,
			// otherwise it's the row
			ships[horizontal ? row : row + i][horizontal ? column + i : column] = new EmptySea();
		}
	}

	private <T extends Ship> boolean tryPlaceShipInTheOcean(Ship ship, Class<T> shipClass)
	{
		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();
		boolean horizontal = ship.isHorizontal();

		// place the bow of the current ship in the ocean
		ships[bowRow][bowColumn] = ship;

		// starting from 1 as we just placed the bow
		int shipsPlacedSoFar = 1;

		boolean wholeShipPlaced = true;

		// start from index one as the bow has already been placed
		for (int i = 1; i < ship.getLength(); i++)
		{
			// rest it for each ship part
			boolean shipPartplaced = false;

			// create a ship of length zero representing a ship part
			Ship shipPart = createShipPart(shipClass);

			// set the coordinates and the orientation of the ship part
			shipPart.setBowRow(horizontal ? bowRow : bowRow + i);
			shipPart.setBowColumn(horizontal ? bowColumn + i : bowColumn);
			shipPart.setHorizontal(horizontal);

			// try and place it in the ocean
			if (!isOccupied(shipPart.getBowRow(), shipPart.getBowColumn()))
			{
				// check the route (horizontally or vertically) as you place
				// ship parts onto the ocean
				if (horizontal ? horizontalRouteIsClear(shipPart) : verticalRouteIsClear(shipPart))
				{
					ships[shipPart.getBowRow()][shipPart.getBowColumn()] = shipPart;
					shipsPlacedSoFar++;
					shipPartplaced = true;
				}
			}
			if (!shipPartplaced)
			{
				// if the ship part was not dropped remove whatever was
				// dropped up to that point
				wholeShipPlaced = false;
				removeShipFromTheOcean(ship, shipsPlacedSoFar);
				break;
			}
		}

		return wholeShipPlaced;
	}

	// =============== ROUTE CHECKERS =============== //
	private boolean horizontalRouteIsClear(Ship shipPart)
	{
		int row = shipPart.getBowRow();
		int column = shipPart.getBowColumn();

		if (row == 0)
		{
			// top edge
			return rightIsClear(row, column) && bottomIsClear(row, column) && bottomRightIsClear(row, column);
		}
		else if (row == Ocean.OceanHeight - 1)
		{
			// bottom edge
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column);
		}
		else
		{
			// mid area
			return rightIsClear(row, column) && topIsClear(row, column) && topRightIsClear(row, column)
					&& bottomRightIsClear(row, column) && bottomIsClear(row, column);
		}
	}

	private boolean verticalRouteIsClear(Ship shipPart)
	{
		int row = shipPart.getBowRow();
		int column = shipPart.getBowColumn();

		if (column == 0)
		{
			// left edge
			return bottomIsClear(row, column) && bottomRightIsClear(row, column) && rightIsClear(row, column);
		}
		else if (column == Ocean.OceanWidth - 1)
		{
			// right edge
			return bottomIsClear(row, column) && bottomLeftIsClear(row, column) && leftIsClear(row, column);
		}
		else
		{
			// mid area
			return bottomIsClear(row, column) && leftIsClear(row, column) && bottomLeftIsClear(row, column)
					&& bottomRightIsClear(row, column) && rightIsClear(row, column);
		}
	}

	// ========= SORROUNDING AREA CHECKERS ========== //
	private boolean bottomIsClear(int row, int column)
	{
		if (row == Ocean.OceanHeight - 1)
		{
			return false;
		}
		return !isOccupied(row + 1, column);
	}

	private boolean rightIsClear(int row, int column)
	{
		if (column == Ocean.OceanWidth - 1)
		{
			return false;
		}
		return !isOccupied(row, column + 1);
	}

	private boolean topIsClear(int row, int column)
	{
		if (row == 0)
		{
			return false;
		}
		return !isOccupied(row - 1, column);
	}

	private boolean leftIsClear(int row, int column)
	{
		if (column == 0)
		{
			return false;
		}
		return !isOccupied(row, column - 1);
	}

	private boolean topRightIsClear(int row, int column)
	{
		if (row == 0 || column == Ocean.OceanWidth - 1)
		{
			return false;
		}
		return !isOccupied(row - 1, column + 1);
	}

	private boolean topLeftIsClear(int row, int column)
	{
		if (row == 0 || column == 0)
		{
			return false;
		}
		return !isOccupied(row - 1, column - 1);
	}

	private boolean bottomRightIsClear(int row, int column)
	{
		if (row == Ocean.OceanHeight - 1 || column == Ocean.OceanWidth - 1)
		{
			return false;
		}
		return !isOccupied(row + 1, column + 1);
	}

	private boolean bottomLeftIsClear(int row, int column)
	{
		if (row == Ocean.OceanHeight - 1 || column == 0)
		{
			return false;
		}
		return !isOccupied(row + 1, column - 1);
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
