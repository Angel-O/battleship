/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;

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
	public void test_should_return_battleship_correct_lenght()
	{
		Ship ship = new Battleship();
		int expected = 4;
		int actual = ship.getLength();
		assertEquals("verifying battleship length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_cruiser_correct_lenght()
	{
		Ship ship = new Cruiser();
		int expected = 3;
		int actual = ship.getLength();
		assertEquals("verifying cruiser length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_destroyer_correct_lenght()
	{
		Ship ship = new Destroyer();
		int expected = 2;
		int actual = ship.getLength();
		assertEquals("verifying destroyer length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_submarine_correct_lenght()
	{
		Ship ship = new Submarine();
		int expected = 1;
		int actual = ship.getLength();
		assertEquals("verifying submarine length", expected, actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_should_return_empty_sea_dummy_lenght()
	{
		Ship ship = new EmptySea();
		int expected = 0;
		int actual = ship.getLength();
		assertEquals("verifying dummy length of empty sea", expected, actual);
	}
}
