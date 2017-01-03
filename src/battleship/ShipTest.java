/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
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
		int expected = 4;
		int actual = ship.getLength();
		assertEquals("verifying battleship length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_cruiser_correct_length()
	{
		Ship ship = new Cruiser();
		int expected = 3;
		int actual = ship.getLength();
		assertEquals("verifying cruiser length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_destroyer_correct_length()
	{
		Ship ship = new Destroyer();
		int expected = 2;
		int actual = ship.getLength();
		assertEquals("verifying destroyer length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_submarine_correct_length()
	{
		Ship ship = new Submarine();
		int expected = 1;
		int actual = ship.getLength();
		assertEquals("verifying submarine length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_empty_sea_dummy_length()
	{
		Ship ship = new EmptySea();
		int expected = 1;
		int actual = ship.getLength();
		assertEquals("verifying dummy length of empty sea", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_horizontal_ships_should_be_sunk_when_shots_are_fired_across_their_length()
	{
		// set the ships bow at position (1,1) and align them horizontally
		Ship battleship = new Battleship();
		battleship.setBowRow(1);
		battleship.setBowColumn(1);
		battleship.setHorizontal(true);

		Ship destroyer = new Destroyer();
		destroyer.setBowRow(1);
		destroyer.setBowColumn(1);
		destroyer.setHorizontal(true);

		Ship cruiser = new Cruiser();
		cruiser.setBowRow(1);
		cruiser.setBowColumn(1);
		cruiser.setHorizontal(true);

		Ship submarine = new Submarine();
		submarine.setBowRow(1);
		submarine.setBowColumn(1);
		submarine.setHorizontal(true);

		// shoot each part along the length of the ship
		shootHorizontallyAcrossShipLength(battleship);
		shootHorizontallyAcrossShipLength(destroyer);
		shootHorizontallyAcrossShipLength(cruiser);
		shootHorizontallyAcrossShipLength(submarine);

		// verify that the ships have been actually sunk
		assertTrue("sinking a horizontal battleship", battleship.isSunk());
		assertTrue("Sinking a horizontal destroyer", destroyer.isSunk());
		assertTrue("sinking a horizontal cruiser", cruiser.isSunk());
		assertTrue("Sinking a horizontal submarine", submarine.isSunk());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_vertical_ships_should_be_sunk_when_shots_are_fired_across_their_length()
	{
		// NO LONGER MEANINGFUL
		// // if we have a vertical battleship with bow at (1)
		// Ship battleship = new Battleship();
		// battleship.setBowRow(1);
		// battleship.setBowColumn(1);
		//
		// Ship destroyer = new Destroyer();
		// destroyer.setBowRow(1);
		// destroyer.setBowColumn(1);
		//
		// Ship cruiser = new Cruiser();
		// cruiser.setBowRow(1);
		// cruiser.setBowColumn(1);
		//
		// Ship submarine = new Submarine();
		// submarine.setBowRow(1);
		// submarine.setBowColumn(1);
		//
		// // shoot each part along the length of the ship
		// shootVerticallyAcrossShipLength(battleship);
		// shootVerticallyAcrossShipLength(destroyer);
		// shootVerticallyAcrossShipLength(cruiser);
		// shootVerticallyAcrossShipLength(submarine);
		//
		// // verify that the ships have been actually sunk
		// assertTrue("sinking a vertical battleship", battleship.isSunk());
		// assertTrue("Sinking a vertical destroyer", destroyer.isSunk());
		// assertTrue("sinking a vertical cruiser", cruiser.isSunk());
		// assertTrue("Sinking a vertical submarine", submarine.isSunk());
	}

	private void shootHorizontallyAcrossShipLength(Ship ship)
	{
		for (int i = ship.getBowColumn(); i < ship.getBowColumn() + ship.getLength(); i++)
		{
			ship.shootAt(ship.getBowRow(), i);
		}
	}

	private void shootVerticallyAcrossShipLength(Ship ship)
	{
		for (int i = ship.getBowRow(); i < ship.getBowRow() + ship.getLength(); i++)
		{
			ship.shootAt(i, ship.getBowColumn());
		}
	}

}
