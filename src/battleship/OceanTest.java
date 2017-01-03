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
	private static Ocean ocean;

	private static Ship[][] ships;

	private static Ship[][] rotatedOcean;

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
		rotatedOcean = null;
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

	@Test
	public void test_exact_number_of_ships_should_be_placed_randomly_on_the_ocean()
	{
		// if we count the total number of horizontal ships in the ocean
		int actual = countShipsOnEachOceanRow(ships);

		// and then we add the count of the vertical ships
		rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		actual += countShipsOnEachOceanRow(rotatedOcean);

		// we should have this amount of ships
		int expected = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		assertEquals(expected, actual);
	}


	@Test
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

		int actualTotalShips = countShipsOnEachOceanRow(ships);

		rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();

		actualTotalShips += countShipsOnEachOceanRow(rotatedOcean);

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (ships[i][j].isRealShip())
				{
					// if the area contains a real ship (a bow or a another
					// part) increment the total area
					actualShipArea++;
				}
			}
		}

		// we would expect that the total number of ships and the total area
		// covered by the ships is what we would have if there weren't any ship
		// overlapping
		boolean expected = totalShipArea == actualShipArea && totalShips == actualTotalShips;

		assertTrue("no overlapping ships in the ocean", expected);
	}

	@Test
	public void test_ships_should_not_be_adjacent_diagonally_when_placed_randomly_on_ocean()
	{
		boolean adjacent = false;

		// TODO remove these lines...
		ocean = new Ocean();
		ocean.placeAllShipsRandomly();
		ships = ocean.getShipArray();
		// if you move along each row until you find a horizontal ship bow and
		// there are ships in the ocean spots placed diagonally, there shouldn't
		// be any other ship around
		adjacent = checkDiagonalAdjecency(ships);

		// we expect not to have any diagonal adjacency when moving horizontally
		assertEquals("checking diagonal adjacency moving horizontally along each row", false, adjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		adjacent = checkDiagonalAdjecency(rotatedOcean);


		// we expect not to have any diagonal adjacency when moving vertically
		assertEquals("checking diagonal adjacency moving vertically along each column", false, adjacent);
	}

	@Test
	public void test_ships_should_not_be_adjacent_on_a_straight_line_when_placed_randomly_on_ocean()
	{
		// if there are any adjacent ship there will be a mismatch between the
		// distance from each ship bow till the first empty sea area and the
		// number of ships expected to have a certain length. So for instance
		// if a battleship is adjacent to another ship it's length will be
		// longer than what it would be if it wasn't: therefore we would expect
		// 1 * 4 squared occupied by battleships, but we would get 5. The error
		// could compensate for a particular type of ship, but overall at least
		// one would be different, therefore we have to check every ship type
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

		// print(ships);
		//
		// System.out.println("==========");
		// rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		//
		// print(rotatedOcean);

		// and then do the same vertically
		rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
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
		Ship[][] rotatedOcean = rotateOceanNinetyDegreeAntiClockwise();
		scanOceanAndAddShipValuesToTheMapper(rotatedOcean, shipTypeToAreaMapper);

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
		int row = 0;
		int column = 0;
		placeBattleshipAt(row, column, false);

		// if we shoot once anywhere at the ship
		Random random = new Random();
		boolean hitAfloatShip = ocean.shootAt(random.nextInt(Battleship.BATTLESHIP_LENGTH), column);

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
		int row = 0;
		int column = 0;
		placeBattleshipAt(row, column, true);

		// if we shoot across its length until we sink it
		boolean hitSunkShip;

		for (int i = 0; i < ships[row][column].getLength(); i++)
		{
			ocean.shootAt(row, column + i);
		}

		// then shoot again at it anywhere across its length
		Random random = new Random();
		hitSunkShip = ocean.shootAt(row, random.nextInt(Battleship.BATTLESHIP_LENGTH));

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
		for (int row = 0; row < Ocean.OCEAN_HEIGHT; row++)
		{
			for (int column = 0; column < Ocean.OCEAN_WIDTH; column++)
			{
				// get the current ship
				Ship ship = ships[row][column];

				// if it's a real horizontal ship count the area occupied
				// by the ship
				if (ship.isRealShip() && ship.isHorizontal())
				{
					// the bow is already included in the count
					int realShipAreaCounter = 1;
					int shipLength = ship.getLength();

					// stop when an empty sea is found or the edge of the border
					// is reached
					while (column + realShipAreaCounter < Ocean.OCEAN_WIDTH
							&& ships[row][column + realShipAreaCounter].isRealShip())
					{
						realShipAreaCounter++;
					}

					Class<? extends Ship> shipClass = ship.getClass();

					shipTypeToAreaMapper.put(shipClass, shipTypeToAreaMapper.get(shipClass) + realShipAreaCounter);

					// move past the current ship
					column += shipLength - 1;
				}
			}
		}
	}

	private static Ship[][] rotateOceanNinetyDegreeAntiClockwise()
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
				rotatedOcean[(oceanRow.length - 1) - j][i] = oceanRow[j];

				// invert the orientation of each ship
				oceanRow[j].setHorizontal(!oceanRow[j].isHorizontal());
			}
		}

		return rotatedOcean;
	}

	private boolean checkDiagonalAdjecency(Ship[][] ocean)
	{
		boolean adjacent = false;

		// Keep moving along the ocean row until a
		// horizontal ship bow is found
		for (int row = 0; row < Ocean.OCEAN_HEIGHT; row++)
		{
			for (int column = 0; column < Ocean.OCEAN_WIDTH; column++)
			{
				if (ocean[row][column].isRealShip() && ocean[row][column].isHorizontal())
				{
					Ship horizontalShip = ocean[row][column];

					// there is no possible diagonal adjacency on the left
					// border,
					// but we still want to enter
					// the for loop as at column == 1 there could be a real ship
					// part belonging to a ship
					// with the bow adjacent to the left border: we need to skip
					// that as we are only
					// checking bows and sterns
					if (column > 0)
					{
						// if the ocean spot on the diagonal is a real ship, we
						// have
						// found two ships that are adjacent diagonally
						if (row > 0 && ocean[row - 1][column - 1].isRealShip()
								|| row < Ocean.OCEAN_HEIGHT - 1 && ocean[row + 1][column - 1].isRealShip())
						{
							adjacent = true;
							break;
						}
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

					// get past the current ship
					column = sternColumn;
				}
			}
		}

		return adjacent;
	}

	private void placeBattleshipAt(int bowRow, int bowColumn, boolean isHorizontal)
	{
		int row;
		int column;

		for (int k = 0; k < Battleship.BATTLESHIP_LENGTH; k++)
		{
			// create the ship
			Battleship battleship = new Battleship();
			battleship.setBowRow(bowRow);
			battleship.setBowColumn(bowColumn);
			battleship.setHorizontal(isHorizontal);

			// place it on the ocean
			row = isHorizontal ? bowRow : bowRow + k;
			column = isHorizontal ? bowColumn + k : bowColumn;
			ships[row][column] = battleship;
		}
	}

	private int countShipsOnEachOceanRow(Ship[][] ocean)
	{
		int totalHorizontalShips = 0;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (ocean[i][j].isRealShip() && ocean[i][j].isHorizontal())
				{
					// increment the running count
					totalHorizontalShips++;

					// move past the current ship. minus one
					// since the for loop performs an increment
					j += ocean[i][j].getLength() - 1;
				}
			}
		}

		return totalHorizontalShips;
	}

	public static void print(Ship[][] matrix)
	{
		for (int i = 0; i <= Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j <= Ocean.OCEAN_WIDTH; j++)
			{
				// if it's the first row or first column
				if (i == 0 || j == 0)
				{
					if (i == 0 && j < Ocean.OCEAN_WIDTH)
					{
						// print the first row of numbers
						System.out.print(j == 0 ? " " + j : j);
					}
					else if (i > 0 && j < Ocean.OCEAN_HEIGHT)
					{
						// print the first column of numbers
						System.out.print(i == 0 ? " " : i - 1);
					}
				}
				else
				{
					// otherwise print the whatever is in the ocean
					System.out.print(matrix[i - 1][j - 1]);
				}
			}
			// print the next row on a separate line
			System.out.println();
		}
	}
}
