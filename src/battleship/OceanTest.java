/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		ocean = null;
	}

	@Test
	public void test_exactly_ten_ships_sould_be_placed_randomly_on_the_ocean()
	{
		// get the ships in the ocean
		Ship[][] ships = ocean.getShipArray();

		int totalShips = 0;

		int expected = 0;

		// count them
		for (int i = 0; i < Ocean.OceanWidth; i++)
		{
			for (int j = 0; j < Ocean.OceanHeight; j++)
			{
				if (ships[i][j].isRealShip())
				{
					totalShips++;
				}
			}
		}

		int actual = totalShips;

		// verify that we get the expected number of ships
		assertEquals(expected, actual);
	}

	@Test
	public void test_ships_sould_not_overlap_when_placed_randomly_on_the_ocean()
	{
		// ships can overlap partially (having only one part in common) or
		// entirely.
		// when they overlap entirely we have two options:
		// - they are the same length
		// - they aren't

		// verify that you have 10 ships (if you drop a ship where another one
		// already was, part by part, you will end up with less than 10 ships as
		// you would basically overwrite the ship) ==> not entirely true!!!

		// verify that no ship is in each others range: given length and
		// orientation
		// no other ship should e found in across the length on that direction

		// verify that when you hit a ship, only one will be hit (if they
		// overlap that wouldn't be the case)

	}

	@Test
	public void test_ships_sould_not_be_adjacent_when_placed_randomly_on_the_ocean()
	{
		// each ship part has 9 cells around it unless the ship is on the border
		// verify that each part of each ship has nothing around it apart from
		// empty sea portions or another ship part belonging to he same ship
		fail("not implemented yet");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_occupied_ocean_spot_should_be_flagged_accordingly()
	{
		for (int i = 0; i < Ocean.OceanWidth; i++)
		{
			for (int j = 0; i < Ocean.OceanHeight; j++)
			{
				if (!ocean.getShipTypeAt(i, j).equals(new EmptySea().getShipType()))
				{
					assertTrue("checking occupied state", ocean.isOccupied(i, j));
				}
			}
		}
	}

}
