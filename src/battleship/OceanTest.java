/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Angelo Oparah
 *
 */
public class OceanTest
{
	private Ocean ocean;

	private Ship[][] ships;

	/** default timeout test duration in milliseconds */
	private static final int DEFAULT_TIMEOUT = 2000;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		ocean = new Ocean();
		ocean.placeAllShipsRandomly();
		ships = ocean.getShipArray();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		ocean = null;
		ships = null;
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_exact_number_of_emptysea_sould_be_placed_randomly_on_the_ocean()
	{
		// the total ocean area should be equal to this
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// and the total area covered by real ships should be equal to this
		int totalShipSpace = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// if we count the empty ocean areas
		int actual = 0;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (!ships[i][j].isRealShip())
				{
					actual++;
				}
			}
		}

		// we should get this amount of "empty sea" portions
		int expected = totoalOceanSpace - totalShipSpace;

		assertEquals(expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_exact_number_of_ships_sould_be_placed_randomly_on_the_ocean()
	{
		// if we count the total number of ships in the ocean
		int actual = 0;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (ships[i][j].isRealShip() && ships[i][j].getLength() > 0)
				{
					// (not counting ships with length == 0 as they are
					// just part of another ship)
					actual++;
				}
			}
		}

		// we should have this amount of ships
		int expected = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		assertEquals(expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_ships_should_not_overlap_when_placed_randomly_on_the_ocean()
	{
		// if ships overlap the total area covered by the ships will be less
		// than what it would normally be (for the same amount of ships, having
		// a specific length)
		int totalShipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		int totalShips = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		// count the areas with real ships and the number of ships
		int actualShipArea = 0;
		int actualTotalShips = 0;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (ships[i][j].isRealShip())
				{
					// if the area contains a real ship (a bow or a another
					// part) increment the total area
					actualShipArea++;

					if (ships[i][j].getLength() > 0)
					{
						// if the (real) ship length is greater than zero, we
						// have a bow: each ship has only one bow
						actualTotalShips++;
					}
				}
			}
		}

		// we would expect that the total number of ships and the total area
		// covered by the ships
		// is what we would have if there were'n't any ship overlapping
		boolean expected = totalShipArea == actualShipArea && totalShips == actualTotalShips;

		assertTrue("no overlapping ships in the ocean", expected);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_occupied_ocean_spots_should_be_flagged_accordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and create new ships
		Ship battleship = new Battleship();
		Ship cruiser = new Cruiser();
		Ship destroyer = new Destroyer();
		Ship submarine = new Submarine();

		// that we place the ships in the ocean
		ships = ocean.getShipArray();
		ships[0][0] = battleship;
		ships[0][9] = cruiser;
		ships[9][0] = destroyer;
		ships[9][9] = submarine;

		// then the ocean spots where they have been placed should be marked as
		// occupied
		boolean expected = true;
		assertEquals(expected, ocean.isOccupied(0, 0));
		assertEquals(expected, ocean.isOccupied(0, 9));
		assertEquals(expected, ocean.isOccupied(9, 0));
		assertEquals(expected, ocean.isOccupied(9, 9));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_clear_ocean_spot_should_be_flagged_accordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and check what's on it
		ships = ocean.getShipArray();

		// whatever random area that we pick
		Random random = new Random();
		int row = random.nextInt(Ocean.OCEAN_HEIGHT);
		int column = random.nextInt(Ocean.OCEAN_WIDTH);

		// it should not be flagged as occupied
		boolean expected = false;
		assertEquals(expected, ocean.isOccupied(row, column));
	}

	@Test
	public void test_ships_should_not_be_adjacent_diagonally_when_placed_randomly_on_ocean()
	{
		for (int i = 0; i < 10000; i++)
		{


			ocean = new Ocean();
			ocean.placeAllShipsRandomly();
			ships = ocean.getShipArray();

		boolean adjacent = false;

		// if you move along each row until you find a horizontal ship bow and
		// there are ships in the ocean spots placed diagonally, there shouldn't
		// be any other ship around
		for (Ship[] oceanRow : ships)
		{
			adjacent = checkDiagonalAdjecency(oceanRow);
		}

		// we expect not to have any diagonal adjacency when moving horizontally
		assertEquals("checking diagonal adjacency moving horizontally along each row", false, adjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		Ship[][] rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();

		for (Ship[] oceanColumn : rotatedOcean)
		{
			adjacent = checkDiagonalAdjecency(oceanColumn);
		}

		// we expect not to have any diagonal adjacency when moving vertically
		assertEquals("checking diagonal adjacency moving vertically along each column", false, adjacent);
		}
	}

	private Ship[][] rotateOceanNinetyDegreeAntiClockwise()
	{
		Ship[][] rotatedOcean = new Ship[Ocean.OCEAN_WIDTH][Ocean.OCEAN_HEIGHT];

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			// get the i-th row of the ocean
			Ship[] oceanRow = ships[i];

			// place each element of the row in the rotated ocean so that the
			// vertical ships appear as horizontal and vice-versa
			for (int j = oceanRow.length - 1; j >= 0; j--)
			{
				// invert the orientation of each ship
				oceanRow[j].setHorizontal(!oceanRow[j].isHorizontal());

				rotatedOcean[j][i] = oceanRow[j];

				// match the coordinates of the ships to reflect the new
				// orientation that each ship will have in the rotated ocean
				oceanRow[j].setBowRow(j);
				oceanRow[j].setBowColumn(i);
			}
		}

		return rotatedOcean;
	}

	private boolean checkDiagonalAdjecency(Ship[] oceanRow, boolean rotatedOcean)
	{
		boolean adjacent = false;

		// column index, start from 1 as there is no possible diagonal
		// adjacency on the left border. Keep moving along the ocean row until a
		// horizontal ship bow is found
		for (int column = 1; column < Ocean.OCEAN_WIDTH; column++)
		{
			// TODO : fix this after removing zero-length ships
			boolean horizontalBowFound = oceanRow[column].isRealShip() && oceanRow[column].getLength() > 0
					&& oceanRow[column].isHorizontal();

			if (horizontalBowFound)
			{
				Ship horizontalShip = oceanRow[column];

				// bow row coordinate of the horizontal ship found
				int row = horizontalShip.getBowRow();

				// if the ocean spot on the diagonal is a real ship, we have
				// found to adjacent ships diagonally
				if (row > 0 && ships[row - 1][column - 1].isRealShip()
						|| row < Ocean.OCEAN_HEIGHT - 1 && ships[row + 1][column - 1].isRealShip())
				{
					adjacent = true;
					break;
				}

				// stern column coordinate of the horizontal ship found
				int sternColumn = column + horizontalShip.getLength() - 1;

				// check that the column is still within the range
				if (sternColumn < Ocean.OCEAN_WIDTH - 1)
				{
					if (row > 0 && ships[row - 1][sternColumn + 1].isRealShip()
							|| row < Ocean.OCEAN_HEIGHT - 1 && ships[row + 1][sternColumn + 1].isRealShip())
					{
						adjacent = true;
						break;
					}
				}
			}
		}

		return adjacent;
	}

	@Test
	public void test_ships_sould_not_be_adjacent_when_placed_randomly_on_the_ocean()
	{
		// ships can overlap partially (having only one part in common) or
		// entirely.
		// when they overlap entirely we have two options:
		// - they are the same length
		// - they aren't


		// each ship part has 9 cells around it unless the ship is on the border
		// verify that each part of each ship has nothing around it apart from
		// empty sea portions or another ship part belonging to he same ship

		// verify that no ship is in each others range: given length and
		// orientation
		// no other ship should be found across the length on that direction



		// horizontally & vertically mismatch between ships lengths


		// ships[0][0] = new Submarine();
		// ships[0][1] = new Submarine();

		// ships[5][0] = new Battleship();
		// ships[5][5] = new Battleship();


		boolean foundAdjacent = false;

		boolean nothingAround;

		for (int k = 0; k < 10000000; k++)
		{

			ocean = new Ocean();
			ocean.placeAllShipsRandomly();
			ships = ocean.getShipArray();


			for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
			{
				for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
				{

					Ship ship = ships[i][j];

					if (ship.isRealShip() && ship.getLength() > 0)
					{

						// if nothing around is found it will be true
						nothingAround = checkShipSides(ship) && checkShipEnds(ship);


						if (!nothingAround)
						{
							foundAdjacent = true;
							break;
						}
						assertEquals("checking adjacency", false, foundAdjacent);
					}
				}

				if (foundAdjacent)
				{
					break;
				}
			}

			// ocean.print();

			assertEquals("checking adjacency", false, foundAdjacent);

		}


		// verify that when you hit a ship, only one will be hit (if they
		// overlap that wouldn't be the case ==> not always true)

	}

	private boolean checkEnds(Ship ship)
	{
		int length = ship.getLength();
		boolean horizontal = ship.isHorizontal();
		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();

		int sternRow = horizontal ? bowRow : bowRow + length - 1;
		int sternColumn = horizontal ? bowColumn + length - 1 : bowColumn;

		if (horizontal)
		{
			if (bowColumn > 0 && bowColumn > 0 && bowRow < Ocean.OCEAN_HEIGHT - 1 && sternColumn < Ocean.OCEAN_WIDTH - 1
					&& sternRow > 0 && sternRow > 0)
			{
				return !(ocean.isOccupied(bowRow - 1, bowColumn - 1) && ocean.isOccupied(bowRow, bowColumn - 1)
						&& ocean.isOccupied(bowRow + 1, bowColumn - 1)
						&& ocean.isOccupied(sternRow - 1, sternColumn + 1)
						&& ocean.isOccupied(sternRow, sternColumn + 1)
						&& ocean.isOccupied(sternRow + 1, sternColumn + 1));
			}
			return true;
		}
		else
		{
			if (bowRow > 0 && bowColumn > 0 && sternRow < Ocean.OCEAN_HEIGHT - 1 && sternColumn < Ocean.OCEAN_WIDTH - 1
					&& sternColumn > 0 && bowColumn < Ocean.OCEAN_WIDTH - 1)
			{
				return !(ocean.isOccupied(bowRow - 1, bowColumn - 1) && ocean.isOccupied(bowRow - 1, bowColumn)
						&& ocean.isOccupied(bowRow - 1, bowColumn + 1)
						&& ocean.isOccupied(sternRow + 1, sternColumn - 1)
						&& ocean.isOccupied(sternRow + 1, sternColumn)
						&& ocean.isOccupied(sternRow + 1, sternColumn + 1));
			}
			return true;
		}

	}

	private boolean checkShipSides(Ship ship)
	{
		boolean horizontal = ship.isHorizontal();

		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();

		int row;
		int column;

		// checking both ship sides vertically or horizontally
		for (int i = 0; i < ship.getLength(); i++)
		{
			// checking bottom (if horizontal), or right (if vertical)
			// if horizontal take the row below(+1), iterate over the column(+i)
			// if vertical take the right column(+1), iterate over the row(+i)
			row = horizontal ? bowRow + 1 : bowRow + i;
			column = horizontal ? bowColumn + i : bowColumn + 1;

			if (row < Ocean.OCEAN_HEIGHT && column < Ocean.OCEAN_WIDTH)
			{
				if (ships[row][column].isRealShip())
				{
					return false;
				}
			}

			// checking top (if horizontal), or left (if vertical)
			// if horizontal take the row above(-1), iterate over the column(+i)
			// if vertical take the left column(-1), iterate over the row(+i)
			row = horizontal ? bowRow - 1 : bowRow + i;
			column = horizontal ? bowColumn + i : bowColumn - 1;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (ships[row][column].isRealShip())
				{
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkShipEnds(Ship ship)
	{
		boolean horizontal = ship.isHorizontal();
		int length = ship.getLength();

		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();

		int sternRow = horizontal ? bowRow : bowRow + length - 1;
		int sternColumn = horizontal ? bowColumn + length - 1 : bowColumn;

		int row;
		int column;

		// checking both ship ends vertically or horizontally...diagonally

		// from -1 to 1..
		for (int i = -1; i <= 1; i++)
		{
			// checking left end (if horizontal), or top end (if vertical)
			// if horizontal take the left column(-1), iterate over the row(+i)
			// if vertical take the row above(-1), iterate over the column(+i)
			row = horizontal ? bowRow + i : bowRow - 1;
			column = horizontal ? bowColumn - 1 : bowColumn + i;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (ships[row][column].isRealShip())
				{
					return false;
				}
			}

			// checking right end (if horizontal), or bottom end (if vertical)
			// if horizontal take the right column(+1), iterate over the row(+i)
			// if vertical take the row below(+1), iterate over the column(+i)
			row = horizontal ? sternRow + i : sternRow + 1;
			column = horizontal ? sternColumn + 1 : sternColumn + i;

			if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
			{
				if (ships[row][column].isRealShip())
				{
					return false;
				}
			}
		}

		return true;
	}
}
