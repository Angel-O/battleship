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
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;
		int totalShipSpace = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		int expected = totoalOceanSpace - totalShipSpace;

		int actual = 0;

		// count them
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

		// verify that we get the expected number of sea areas
		assertEquals(expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_exact_number_of_ships_sould_be_placed_randomly_on_the_ocean()
	{
		// verify that you have 10 ships (if you drop a ship where another one
		// already was, part by part, you will end up with less than 10 ships as
		// you would basically overwrite the ship) ==> not entirely true!!!

		int expected = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		int actual = 0;

		// count them
		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (ships[i][j].isRealShip() && ships[i][j].getLength() > 0)
				{
					// do not count ships with length == 0 as they are
					// just part of another ship
					actual++;
				}
			}
		}

		// verify that we get the expected number of ships
		assertEquals(expected, actual);
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

	@Test(timeout = DEFAULT_TIMEOUT)
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

		// ships[0][0] = new Submarine();
		// ships[0][1] = new Submarine();

		// ships[5][0] = new Battleship();
		// ships[5][5] = new Battleship();


		boolean foundAdjacent = false;

		boolean nothingAround;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				Ship ship = ships[i][j];

				// if nothing around is found it will be true
				nothingAround = checkShipSides(ship) && checkShipEnds(ship);


				if (!nothingAround)
				{
					foundAdjacent = true;
					break;
				}
			}
		}

		assertEquals("checking adjacency", foundAdjacent, false);

		// verify that when you hit a ship, only one will be hit (if they
		// overlap that wouldn't be the case ==> not always true)

	}

	private boolean checkShipEnds(Ship ship)
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
		int width = horizontal ? ship.getLength() : 1;
		int height = horizontal ? 1 : ship.getLength();

		int bowRow = ship.getBowRow();
		int bowColumn = ship.getBowColumn();

		for (int i = 0; i < (horizontal ? width : height); i++)
		{
			// if horizontal take the row below (+1)
			int rowBelow = horizontal ? bowRow + 1 : bowRow + i;
			int columnAfter = horizontal ? bowColumn + i : bowColumn + 1;

			if (rowBelow < Ocean.OCEAN_HEIGHT && columnAfter < Ocean.OCEAN_WIDTH)
			{
				// next ship part in line...=> need the side of it (+1 above)
				if (ships[rowBelow][columnAfter].isRealShip())
				{
					return false;
				}
				else
				{
					// System.out.println(ships[rowBelow][columnAfter].getShipType());
				}
			}

			int rowAbove = horizontal ? bowRow - 1 : bowRow + i;
			int columnBefore = horizontal ? bowColumn + i : bowColumn - 1;

			if (rowAbove >= 0 && columnBefore >= 0)
			{
				// next ship part in line...=> need the side of it (+1 above)
				if (ships[rowAbove][columnBefore].isRealShip())
				{
					return false;
				}
				else
				{
					// System.out.println(ships[rowAbove][columnBefore].getShipType());
				}
			}
		}
		return true;
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_ships_sould_not_overlap_when_placed_randomly_on_the_ocean()
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
}
