/**
 *
 */
package battleship;

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
