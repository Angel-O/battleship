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

	private static Ship[][] rotatedShips;

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
		rotatedShips = rotateOceanNinetyDegreeAntiClockwise();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		ocean = null;
		ships = null;
		rotatedShips = null;
	}

	// ========= random ships placed in the ocean test ============== //

	@Test
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
		actual += countShipsOnEachOceanRow(rotatedShips);

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
		int actualTotalShips = countShipsOnEachOceanRow(ships) + countShipsOnEachOceanRow(rotatedShips);

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
		adjacent = checkDiagonalAdjecencyMovingHorizontally(ships);

		// we expect not to have any diagonal adjacency when moving horizontally
		assertEquals("checking diagonal adjacency moving horizontally along each row", false, adjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		adjacent = checkDiagonalAdjecencyMovingHorizontally(rotatedShips);


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
		countShipAreaByShipTypeOnEachOceanRow(shipTypeToAreaMapper, ships);

		// and then do the same vertically
		countShipAreaByShipTypeOnEachOceanRow(shipTypeToAreaMapper, rotatedShips);

		// we should expect that the area covered by each ship type (in the
		// given amount) is what it would be if there were not adjacent ships in
		// the ocean on straight lines
		boolean actual = shipTypeToAreaMapper.get(Battleship.class) == expectedBattleshipArea
				&& shipTypeToAreaMapper.get(Cruiser.class) == expectedCruiserArea
				&& shipTypeToAreaMapper.get(Destroyer.class) == expecteDestroyerArea
				&& shipTypeToAreaMapper.get(Submarine.class) == expectedSubmarineArea;

		assertTrue("checking horizontal and vertical adjecency", actual);

		// if instead we create an empty ocean and add adjacent ships on a
		// straight line
		ocean = new Ocean();
		ships = ocean.getShipArray();
		boolean horizontal = false;
		placeShipTypeAt(Battleship.class, 0, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 3, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 4, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 5, 0, horizontal, ocean);
		placeShipTypeAt(Submarine.class, 6, 0, horizontal, ocean);

		HashMap<Class<? extends Ship>, Integer> failMapper = new HashMap<>();
		failMapper.put(Battleship.class, 0);
		failMapper.put(Submarine.class, 0);

		// we should get this amount for each ship type
		int expectedBattleshipSurface = Battleship.BATTLESHIP_LENGTH * 1;
		int expectedSubmarineSurface = Submarine.SUBMARINE_LENGTH * 4;

		// if we scan the ocean horizontally
		countShipAreaByShipTypeOnEachOceanRow(failMapper, ships);

		// then vertically
		rotatedShips = rotateOceanNinetyDegreeAntiClockwise();
		countShipAreaByShipTypeOnEachOceanRow(failMapper, rotatedShips);

		// we should expect a mismatch compared to the expected values
		actual = failMapper.get(Battleship.class) == expectedBattleshipSurface
				&& failMapper.get(Submarine.class) == expectedSubmarineSurface;

		assertFalse("checking failing horizontal and vertical adjecency", actual);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_occupied_ocean_spots_should_be_flagged_accordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and we place ships onto it
		boolean isHorizontal = true;
		placeShipTypeAt(Battleship.class, 0, 0, isHorizontal, ocean);
		placeShipTypeAt(Cruiser.class, 2, 0, isHorizontal, ocean);
		placeShipTypeAt(Destroyer.class, 4, 0, isHorizontal, ocean);
		placeShipTypeAt(Submarine.class, 6, 0, isHorizontal, ocean);

		// then the ocean spots where they have been placed should be marked as
		// occupied
		boolean expected = true;
		assertEquals(expected, ocean.isOccupied(0, 0));
		assertEquals(expected, ocean.isOccupied(2, 0));
		assertEquals(expected, ocean.isOccupied(4, 0));
		assertEquals(expected, ocean.isOccupied(6, 0));
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
		// whatever it was fired
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

		// then check if the location contains a sunk ship
		boolean actual = ocean.hasSunkShipAt(row, column);

		// we should expect it to be correctly reported
		assertTrue("reporting sunk ship", actual);
	}

	// ======================= helper methods ======================== //

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
				// get the info of the current ship
				int shipLength = oceanRow[j].getLength();
				int bowRow = oceanRow[j].getBowRow();
				int bowColumn = oceanRow[j].getBowColumn();
				boolean wasHorizontal = oceanRow[j].isHorizontal();

				// create a ship of the same type as the current ship
				Ship ship = createShip(oceanRow[j].getClass());

				// invert its orientation
				ship.setHorizontal(!wasHorizontal);

				// change the coordinates of the bow of the new ship to match
				// the new orientation
				int rotatedBowRow = wasHorizontal ? (oceanRow.length - 1) - (bowColumn + shipLength - 1)
						: (oceanRow.length - 1) - bowColumn;
				int rotatedBowColumn = bowRow;

				ship.setBowRow(rotatedBowRow);
				ship.setBowColumn(rotatedBowColumn);

				// place it in the rotated ocean
				rotatedOcean[(oceanRow.length - 1) - j][i] = ship;
			}
		}

		return rotatedOcean;
	}

	private void countShipAreaByShipTypeOnEachOceanRow(HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper, Ship[][] ships)
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

	private boolean checkDiagonalAdjecencyMovingHorizontally(Ship[][] ships)
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
					// border, but we still want to enter the for loop as at
					// column == 1 there could be a real ship part belonging to
					// a ship with the bow adjacent to the left border: we need
					// to skip that as we are only checking bows and sterns
					if (j > 0)
					{
						// if the ocean spot on the diagonal is a real ship, we
						// have found two ships that are adjacent diagonally
						if (i > 0 && ships[i - 1][j - 1].isRealShip()
								|| i < Ocean.OCEAN_HEIGHT - 1 && ships[i + 1][j - 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}

					// move to the stern column coordinate of the horizontal
					// ship found
					j = j + ship.getLength() - 1;

					// check that the column is still within the range
					if (j < Ocean.OCEAN_WIDTH - 1)
					{
						if (i > 0 && ships[i - 1][j + 1].isRealShip()
								|| i < Ocean.OCEAN_HEIGHT - 1 && ships[i + 1][j + 1].isRealShip())
						{
							adjacent = true;
							break;
						}
					}
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

	private int countShipsOnEachOceanRow(Ship[][] ships)
	{
		int totalShips = 0;

		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{

				if (ships[i][j].isRealShip() && ships[i][j].isHorizontal())
				{
					// increment the running count
					totalShips++;

					// move past the current ship (by adding the ship length to
					// the 'fast' index --> inner loop; minus one since the for
					// loop performs an increment)
					j = j + ships[i][j].getLength() - 1;
				}
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
					else if (j == 0)
					{
						// print the first column of numbers
						System.out.print(i - 1);
					}
				}
				else
				{
					Ship ship = matrix[i - 1][j - 1];
					boolean horizontal = ship.isHorizontal();
					Class<? extends Ship> shipClass = ship.getClass();
					// otherwise print whatever is in the ocean
					System.out.print(printShip(shipClass, horizontal));
				}
			}
			// print the next row on a separate line
			System.out.println();
		}
	}

	private static <T extends Ship> String printShip(Class<T> shipClass, boolean horizontal)
	{

		if (shipClass.equals(Battleship.class))
		{

			return "4";
		}
		else if (shipClass.equals(Cruiser.class))
		{

			return "3";
		}
		else if (shipClass.equals(Destroyer.class))
		{

			return "2";
		}
		else if (shipClass.equals(Submarine.class))
		{

			return horizontal ? "H" : "V";
		}
		else
		{
			return ".";
		}
	}
}
