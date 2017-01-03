/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
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

	// ========= random ships placed in the ocean test ============== //

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
				if (ships[i][j].isRealShip() && ships[i][j].getLength() == establishShipLengthFromShipType(ships[i][j]))
				{
					// (counting only ships with length equal to the max length
					// for the particular ship type, as they represent the ship
					// bow and there is only one bow per ship)
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

					if (ships[i][j].getLength() == establishShipLengthFromShipType(ships[i][j]))
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

	@Test
	public void test_ships_should_not_be_adjacent_diagonally_when_placed_randomly_on_ocean()
	{
		boolean adjacent = false;

		// if you move along each row until you find a horizontal ship bow and
		// there are ships in the ocean spots placed diagonally, there shouldn't
		// be any other ship around
		for (Ship[] oceanRow : ships)
		{
			adjacent = checkDiagonalAdjecency(oceanRow, ships);
		}

		// we expect not to have any diagonal adjacency when moving horizontally
		assertEquals("checking diagonal adjacency moving horizontally along each row", false, adjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		Ship[][] rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();

		for (Ship[] oceanColumn : rotatedOcean)
		{
			adjacent = checkDiagonalAdjecency(oceanColumn, rotatedOcean);
		}

		// we expect not to have any diagonal adjacency when moving vertically
		assertEquals("checking diagonal adjacency moving vertically along each column", false, adjacent);
	}

	@Test
	public void test_ships_should_not_be_adjacent_on_a_straight_line_when_placed_randomly_on_ocean()
	{
		// if there are any adjacent ship there will be a mismatch between the
		// distance from each ship bow till the first empty sea area and the
		// number of ships expected to have a certain length
		int expectedBattleshipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH;
		int expectedCruiserArea = Ocean.CRUISERS * Cruiser.CRUISER_LENGTH;
		int expecteDestroyerArea = Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH;
		int expectedSubmarineArea = Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper = new HashMap<>();

		shipTypeToAreaMapper.put(Battleship.class, 0);
		shipTypeToAreaMapper.put(Cruiser.class, 0);
		shipTypeToAreaMapper.put(Destroyer.class, 0);
		shipTypeToAreaMapper.put(Submarine.class, 0);

		// if we scan the ocean horizontally and increment the ship area for
		// each horizontal ship we encounter
		scanOceanAndAddShipValuesToTheMapper(ships, shipTypeToAreaMapper);

		// and then do the same vertically
		Ship[][] rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		scanOceanAndAddShipValuesToTheMapper(rotatedOcean, shipTypeToAreaMapper);

		// we should expect that the area covered by each ship type (in the
		// given amount) is what it would be if there were not adjacent ships in
		// the ocean on straight lines
		boolean actual = shipTypeToAreaMapper.get(Battleship.class) == expectedBattleshipArea
				&& shipTypeToAreaMapper.get(Cruiser.class) == expectedCruiserArea
				&& shipTypeToAreaMapper.get(Destroyer.class) == expecteDestroyerArea
				&& shipTypeToAreaMapper.get(Submarine.class) == expectedSubmarineArea;

		assertTrue("checking horizontal and vertical adjecency", actual);

		// if instead we create an empty ocean
		ocean = new Ocean();

		// and add adjacent ships on a straight line
		ships = ocean.getShipArray();
		ships[0][0] = new Battleship();
		ships[3][0] = new Submarine();
		ships[4][0] = new Submarine();
		ships[5][0] = new Submarine();
		ships[6][0] = new Submarine();

		// we should get this amount
		int expectedBattleshipSurface = Battleship.BATTLESHIP_LENGTH * 1;
		int expectedSubmarineSurface = Submarine.SUBMARINE_LENGTH * 4;

		// if we scan the ocean horizontally
		HashMap<Class<? extends Ship>, Integer> failMapper = new HashMap<>();
		failMapper.put(Battleship.class, 0);
		failMapper.put(Submarine.class, 0);

		// then vertically
		scanOceanAndAddShipValuesToTheMapper(ships, failMapper);
		Ship[][] rotatatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		scanOceanAndAddShipValuesToTheMapper(rotatatedOcean, shipTypeToAreaMapper);

		// we get this amount instead
		actual = failMapper.get(Battleship.class) == expectedBattleshipSurface
				&& failMapper.get(Submarine.class) == expectedSubmarineSurface;

		assertFalse("checking failing horizontal and vertical adjecency", actual);
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

	// ================ shooting at ocean location test ============== //

	@Test
	public void test_shooting_afloat_ship_should_be_succesfull()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		Ship ship = new Battleship();
		ship.setBowRow(0);
		ship.setBowColumn(0);
		ship.setHorizontal(false);
		ships[ship.getBowRow()][ship.getBowColumn()] = ship;

		// if we shoot anywhere at at the ship
		Random random = new Random();
		boolean hitAfloatShip = ocean.shootAt(random.nextInt(ship.getLength()), ship.getBowColumn());

		// we should expect to catch an afloat ship
		assertTrue("hitting an afloat ship", hitAfloatShip);
	}

	@Test
	public void test_shooting_sunk_ships_should_be_unsuccesfull()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		Ship ship = new Battleship();
		ship.setBowRow(0);
		ship.setBowColumn(0);
		ship.setHorizontal(true);
		ships[ship.getBowRow()][ship.getBowColumn()] = ship;

		// if we shoot across its length
		boolean hitSunkShip;

		for (int i = 0; i < ship.getLength(); i++)
		{
			ocean.shootAt(ship.getBowRow(), ship.getBowColumn() + i);
		}

		hitSunkShip = ocean.shootAt(ship.getBowRow(), ship.getBowColumn());

		// we should expect an unsuccessful shot
		assertFalse("hitting a sunk ship", hitSunkShip);
	}

	@Test
	public void test_number_of_shots_fired_should_be_updated_at_each_shot()
	{
		fail("not implemented yet");
	}

	@Test
	public void test_number_of_hits_should_be_updated_at_each_shot()
	{
		fail("not implemented yet");
	}

	// ======================= helper methods ======================== //

	private void scanOceanAndAddShipValuesToTheMapper(Ship[][] ships,
			HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper)
	{
		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				Ship ship = ships[i][j];

				if (ship.isRealShip() && ship.getLength() == establishShipLengthFromShipType(ship)
						&& ship.isHorizontal())
				{
					// the bow is already included in the count
					int realShipAreaCounter = 1;
					int start = ship.getBowColumn();

					while (start + realShipAreaCounter < Ocean.OCEAN_WIDTH
							&& ships[i][start + realShipAreaCounter].isRealShip())
					{
						realShipAreaCounter++;
					}

					Class<? extends Ship> shipClass = ship.getClass();

					shipTypeToAreaMapper.put(shipClass, shipTypeToAreaMapper.get(shipClass) + realShipAreaCounter);
				}
			}
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

	private boolean checkDiagonalAdjecency(Ship[] oceanRow, Ship[][] ocean)
	{
		boolean adjacent = false;

		// column index, start from 1 as there is no possible diagonal
		// adjacency on the left border. Keep moving along the ocean row until a
		// horizontal ship bow is found
		for (int column = 1; column < Ocean.OCEAN_WIDTH; column++)
		{
			boolean horizontalBowFound = oceanRow[column].isRealShip()
					&& oceanRow[column].getLength() == establishShipLengthFromShipType(oceanRow[column])
					&& oceanRow[column].isHorizontal();

			if (horizontalBowFound)
			{
				Ship horizontalShip = oceanRow[column];

				// bow row coordinate of the horizontal ship found
				int row = horizontalShip.getBowRow();

				// if the ocean spot on the diagonal is a real ship, we have
				// found two ships that are adjacent diagonally
				if (row > 0 && ocean[row - 1][column - 1].isRealShip()
						|| row < Ocean.OCEAN_HEIGHT - 1 && ocean[row + 1][column - 1].isRealShip())
				{
					adjacent = true;
					break;
				}

				// stern column coordinate of the horizontal ship found
				int sternColumn = column + horizontalShip.getLength() - 1;

				// check that the column is still within the range
				if (sternColumn < Ocean.OCEAN_WIDTH - 1)
				{
					if (row > 0 && ocean[row - 1][sternColumn + 1].isRealShip()
							|| row < Ocean.OCEAN_HEIGHT - 1 && ocean[row + 1][sternColumn + 1].isRealShip())
					{
						adjacent = true;
						break;
					}
				}
			}
		}

		return adjacent;
	}

	private int establishShipLengthFromShipType(Ship ship)
	{
		if (ship instanceof Battleship)
		{
			return Battleship.BATTLESHIP_LENGTH;
		}
		else if (ship instanceof Cruiser)
		{
			return Cruiser.CRUISER_LENGTH;
		}
		else if (ship instanceof Destroyer)
		{
			return Destroyer.DESTROYER_LENGTH;
		}
		else if (ship instanceof Submarine)
		{
			return Submarine.SUBMARINE_LENGTH;
		}
		else
		{
			return 0;
		}
	}
}
