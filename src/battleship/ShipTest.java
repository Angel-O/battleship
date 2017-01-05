/**
 * @author Angelo Oparah
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Angelo Oparah
 *
 */
public class ShipTest
{
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
	{}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_battleship_correct_length()
	{
		Ship ship = new Battleship();
		int expected = Battleship.BATTLESHIP_LENGTH;
		int actual = ship.getLength();
		assertEquals("verifying battleship length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_cruiser_correct_length()
	{
		Ship ship = new Cruiser();
		int expected = Cruiser.CRUISER_LENGTH;
		int actual = ship.getLength();
		assertEquals("verifying cruiser length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_destroyer_correct_length()
	{
		Ship ship = new Destroyer();
		int expected = Destroyer.DESTROYER_LENGTH;
		int actual = ship.getLength();
		assertEquals("verifying destroyer length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_submarine_correct_length()
	{
		Ship ship = new Submarine();
		int expected = Submarine.SUBMARINE_LENGTH;
		int actual = ship.getLength();
		assertEquals("verifying submarine length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_empty_sea_dummy_length()
	{
		Ship ship = new EmptySea();
		int expected = EmptySea.EMPTY_SEA_LENGTH;
		int actual = ship.getLength();
		assertEquals("verifying empty sea length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_ships_should_be_sunk_when_shots_are_fired_across_their_length()
	{
		// if we have any type of ship
		Ship battleship = new Battleship();
		Ship destroyer = new Destroyer();
		Ship cruiser = new Cruiser();
		Ship submarine = new Submarine();

		// and shoot each part along the length of the ship
		firingShotsAcrossShipLength(battleship, Battleship.BATTLESHIP_LENGTH);
		firingShotsAcrossShipLength(destroyer, Destroyer.DESTROYER_LENGTH);
		firingShotsAcrossShipLength(cruiser, Cruiser.CRUISER_LENGTH);
		firingShotsAcrossShipLength(submarine, Submarine.SUBMARINE_LENGTH);

		// verify that the ships have been actually sunk
		assertTrue("shooting a battleship until it sinks", battleship.isSunk());
		assertTrue("shooting a destroyer until it sinks", destroyer.isSunk());
		assertTrue("shooting a cruiser until it sinks", cruiser.isSunk());
		assertTrue("shooting a submarine will sink it", submarine.isSunk());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_ships_should_not_be_sunk_if_not_all_parts_are_hit()
	{
		// if we have any type of ship
		Ship battleship = new Battleship();
		Ship destroyer = new Destroyer();
		Ship cruiser = new Cruiser();
		Ship submarine = new Submarine();

		// and shoot along the length of the ship without hitting all parts
		firingShotsAcrossShipLength(battleship, Battleship.BATTLESHIP_LENGTH - 1);
		firingShotsAcrossShipLength(destroyer, Destroyer.DESTROYER_LENGTH - 1);
		firingShotsAcrossShipLength(cruiser, Cruiser.CRUISER_LENGTH - 1);
		firingShotsAcrossShipLength(submarine, Submarine.SUBMARINE_LENGTH - 1);

		// verify that the ships have been actually sunk
		assertFalse("shooting a battleship without sinking it", battleship.isSunk());
		assertFalse("shooting a destroyer without sinking it", destroyer.isSunk());
		assertFalse("shooting a cruiser without sinking it", cruiser.isSunk());
		assertFalse("not shooting at submarine will not sink it", submarine.isSunk());
	}

	private void firingShotsAcrossShipLength(Ship ship, int length)
	{
		// get ship orientation
		boolean horizontal = ship.isHorizontal();

		int start = horizontal ? ship.getBowColumn() : ship.getBowRow();

		for (int i = start; i < start + length; i++)
		{
			// if the ship is horizontal increment the column, otherwise
			// increment the row
			ship.shootAt(horizontal ? ship.getBowRow() : i, horizontal ? i : ship.getBowColumn());
		}
	}
}
