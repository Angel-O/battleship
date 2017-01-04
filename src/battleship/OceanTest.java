/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	private boolean scanHorizontally = true;

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

	@Test
	public void test_exact_number_of_ships_should_be_placed_randomly_on_the_ocean()
	{
		// if we count the total number of horizontal ships in the ocean
		int actual = countShipsOnEachOceanLine(scanHorizontally);

		// and then we add the count of the vertical ships
		actual += countShipsOnEachOceanLine(!scanHorizontally);

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

		// if we count the total number of ships in the ocean
		int actualTotalShips = countShipsOnEachOceanLine(scanHorizontally)
				+ countShipsOnEachOceanLine(!scanHorizontally);

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

		// if you move along each row until you find a horizontal ship bow and
		// there are ships in the ocean spots placed diagonally, there shouldn't
		// be any other ship around
		adjacent = checkDiagonalAdjecencyMovingHorizontally();

		// we expect not to have any diagonal adjacency when moving horizontally
		assertEquals("checking diagonal adjacency moving horizontally along each row", false, adjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		adjacent = checkDiagonalAdjecencyMovingVertically();


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
		countShipAreaByTypeHorizontally(shipTypeToAreaMapper);

		// and then do the same vertically
		countShipAreaByTypeVertically(shipTypeToAreaMapper);

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

		boolean horizontal = false;
		placeShipTypeAt(Battleship.class, 0, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 3, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 4, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 5, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 6, 0, horizontal, ocean);

		// and add adjacent ships on a straight line
		ships = ocean.getShipArray();

		// we should get this amount
		int expectedBattleshipSurface = Battleship.BATTLESHIP_LENGTH * 1;
		int expectedSubmarineSurface = Submarine.SUBMARINE_LENGTH * 4;

		// if we scan the ocean horizontally
		HashMap<Class<? extends Ship>, Integer> failMapper = new HashMap<>();
		failMapper.put(Battleship.class, 0);
		failMapper.put(Submarine.class, 0);

		// then vertically
		countShipAreaByTypeHorizontally(failMapper);
		countShipAreaByTypeVertically(failMapper);

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
		boolean isHorizontal = false;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

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
		boolean isHorizontal = true;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

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
		// if we shoot a random number of times
		Random random = new Random();
		int expected = random.nextInt(100);

		// at random locations within the ocean's borders
		for (int i = 0; i < expected; i++)
		{
			ocean.shootAt(random.nextInt(Ocean.OCEAN_HEIGHT), random.nextInt(Ocean.OCEAN_WIDTH));
		}

		// we should expect the number of shots registered by the ocean to be
		// the whatever it was fired
		assertEquals(expected, ocean.getShotsFired());
	}

	@Test
	public void test_number_of_hits_should_be_updated_when_ship_is_succesfully_hit()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and place a Battleship at location (0,0), vertically
		int bowRow = 0;
		int bowColumn = 0;
		boolean isHorizontal = false;
		placeShipTypeAt(Battleship.class, bowRow, bowColumn, isHorizontal, ocean);

		int expected = Battleship.BATTLESHIP_LENGTH;

		// if then we shoot 4 shots across its length at different locations
		// each time and keep shooting till the end of the empty ocean' border
		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			ocean.shootAt(bowRow + i, bowColumn);
		}

		// knowing that the Battleship does not span across the whole length
		// of the ocean's side
		assertTrue(Battleship.BATTLESHIP_LENGTH < Ocean.OCEAN_HEIGHT);

		// we should expect to only get 4 successful shots
		assertEquals(expected, ocean.getHitCount());
	}

	// ===================== game status test ======================== //

	@Test
	public void test_sunk_ship_should_be_correctly_reported_when_requested()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		int row = 0;
		int column = 0;
		boolean isHorizontal = true;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

		for (int i = 0; i < ships[row][column].getLength(); i++)
		{
			ocean.shootAt(row, column + i);
		}

		// then request the ocean for if the location contains a sunk ship
		boolean actual = ocean.hasSunkShipAt(row, column);

		// we should expect it to be correctly reported
		assertTrue("reporting sunk ship", actual);
	}

	// ======================= helper methods ======================== //

	private void countShipAreaByTypeHorizontally(HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper)
	{
		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				// get the current ship
				Ship ship = ships[i][j];

				// if it's a real horizontal ship count the area occupied
				// by the ship
				if (ship.isRealShip() && ship.isHorizontal())
				{
					// the bow is already included in the count
					int realShipAreaCounter = 1;
					int shipLength = ship.getLength();

					Class<? extends Ship> shipClass = ship.getClass();

					// stop when an empty sea is found or the edge of the border
					// is reached
					while (j + realShipAreaCounter < Ocean.OCEAN_WIDTH
							&& ships[i][j + realShipAreaCounter].isRealShip())
					{
						realShipAreaCounter++;
					}

					shipTypeToAreaMapper.put(shipClass, shipTypeToAreaMapper.get(shipClass) + realShipAreaCounter);

					// move past the current ship
					j += shipLength - 1;
				}
			}
		}
	}

	private void countShipAreaByTypeVertically(HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper)
	{
		for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
		{
			for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
			{
				// get the current ship
				Ship ship = ships[i][j];

				// if it's a real horizontal ship count the area occupied
				// by the ship
				if (ship.isRealShip() && !ship.isHorizontal())
				{
					// the bow is already included in the count
					int realShipAreaCounter = 1;
					int shipLength = ship.getLength();

					Class<? extends Ship> shipClass = ship.getClass();

					// stop when an empty sea is found or the edge of the border
					// is reached
					while (i + realShipAreaCounter < Ocean.OCEAN_HEIGHT
							&& ships[i + realShipAreaCounter][j].isRealShip())
					{
						realShipAreaCounter++;
					}

					shipTypeToAreaMapper.put(shipClass, shipTypeToAreaMapper.get(shipClass) + realShipAreaCounter);

					// move past the current ship
					i += shipLength - 1;
				}
			}
		}
	}

	private boolean checkDiagonalAdjecencyMovingHorizontally()
	{
		boolean adjacent = false;

		// Keep moving along the ocean row until a
		// horizontal ship bow is found
		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{

				if (ships[i][j].isRealShip() && ships[i][j].isHorizontal())
				{
					Ship ship = ships[i][j];

					// there is no possible diagonal adjacency on the left
					// border,
					// but we still want to enter
					// the for loop as at column == 1 there could be a real ship
					// part belonging to a ship
					// with the bow adjacent to the left border: we need to skip
					// that as we are only
					// checking bows and sterns
					if (j > 0)
					{
						// if the ocean spot on the diagonal is a real ship, we
						// have
						// found two ships that are adjacent diagonally
						if (i > 0 && ships[i - 1][j - 1].isRealShip()
								|| i < Ocean.OCEAN_HEIGHT - 1 && ships[i + 1][j - 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}

					// stern column coordinate of the horizontal ship found
					int sternColumn = j + ship.getLength() - 1;

					// check that the column is still within the range
					if (sternColumn < Ocean.OCEAN_WIDTH - 1)
					{
						if (i > 0 && ships[i - 1][sternColumn + 1].isRealShip()
								|| i < Ocean.OCEAN_HEIGHT - 1 && ships[i + 1][sternColumn + 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}

					// get past the current ship
					j = sternColumn;
				}
			}
		}

		return adjacent;
	}

	private boolean checkDiagonalAdjecencyMovingVertically()
	{
		boolean adjacent = false;

		// Keep moving along the ocean row until a
		// horizontal ship bow is found
		for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
		{
			for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
			{
				if (ships[i][j].isRealShip() && !ships[i][j].isHorizontal())
				{
					Ship ship = ships[i][j];

					// there is no possible diagonal adjacency on the TOP
					// border,
					// but we still want to enter
					// the for loop as at column == 1 there could be a real ship
					// part belonging to a ship
					// with the bow adjacent to the left border: we need to skip
					// that as we are only
					// checking bows and sterns
					if (i > 0)
					{
						// if the ocean spot on the diagonal is a real ship, we
						// have
						// found two ships that are adjacent diagonally
						if (j > 0 && ships[i - 1][j - 1].isRealShip()
								|| j < Ocean.OCEAN_WIDTH - 1 && ships[i - 1][j + 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}

					// stern column coordinate of the horizontal ship found
					int sternRow = i + ship.getLength() - 1;

					// check that the column is still within the range
					if (sternRow < Ocean.OCEAN_HEIGHT - 1)
					{
						if (j < Ocean.OCEAN_HEIGHT - 1 && ships[sternRow + 1][j + 1].isRealShip()
								|| j > 0 && ships[sternRow + 1][j - 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}

					// get past the current ship
					i = sternRow;
				}
			}
		}

		return adjacent;
	}

	private static <T extends Ship> void placeShipTypeAt(Class<T> shipClass, int bowRow, int bowColumn,
			boolean isHorizontal,
			Ocean ocean)
	{
		int row;
		int column;

		// create the ship
		Ship ship = createShip(shipClass);
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);
		ship.setHorizontal(isHorizontal);

		Ship[][] ships = ocean.getShipArray();

		for (int k = 0; k < ship.getLength(); k++)
		{
			// place it on the ocean
			row = isHorizontal ? bowRow : bowRow + k;
			column = isHorizontal ? bowColumn + k : bowColumn;
			ships[row][column] = ship;
		}
	}

	private static <T extends Ship> Ship createShip(Class<T> shipClass)
	{
		Ship shipPart = null;

		if (shipClass == Battleship.class)
		{
			shipPart = new Battleship();
		}
		else if (shipClass == Cruiser.class)
		{
			shipPart = new Cruiser();
		}
		else if (shipClass == Destroyer.class)
		{
			shipPart = new Destroyer();
		}
		else if (shipClass == Submarine.class)
		{
			shipPart = new Submarine();
		}
		else
		{
			shipPart = new EmptySea();
		}

		return shipPart;
	}

	private int countShipsOnEachOceanLine(boolean horizontally)
	{
		int totalShips = 0;
		int temporarySlowIndex;
		int temporaryFastIndex;

		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			temporarySlowIndex = i;

			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				temporaryFastIndex = j;

				// swap indexes depending on the navigation direction
				i = horizontally ? i : j;
				j = horizontally ? j : temporarySlowIndex;

				// if we scan horizontally we are interested in horizontal ships
				// and vice-versa
				boolean shipOrientationisRight = horizontally ? ships[i][j].isHorizontal() : !ships[i][j].isHorizontal();

				if (ships[i][j].isRealShip() && shipOrientationisRight)
				{
					// increment the running count
					totalShips++;

					// move past the current ship (by adding the ship length to
					// the 'fast' index --> inner loop; minus one since the for
					// loop performs an increment)
					j = (horizontally ? j : i) + ships[i][j].getLength() - 1;
				}
				else
				{
					j = temporaryFastIndex;
				}

				i = temporarySlowIndex;
			}
		}

		return totalShips;
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
