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
 * Test class to test the API of the {@linkplain Ocean} class.
 *
 * @author Angelo Oparah
 *
 */
public class OceanTest
{
	/** Instance of the class under test */
	private Ocean ocean;

	/** Contains the shios in the ocean */
	private Ship[][] ships;

	/**
	 * Contains the same ships in the ocean, rotated 90 degreess anticlockwise
	 * to aid testing.
	 */
	private Ship[][] rotatedShips;

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
	 * Initializes a new instance of the {@linkplain Ocean} class; gets the
	 * {@linkplain Ship} 2-dimentional array, creates a matrix of the same ships
	 * rotated 90 degrees anti-clockwise to aid testing.
	 *
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
	 * Clears the Ocean instance and the two matrices associated with it.
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		ocean = null;
		ships = null;
		rotatedShips = null;
	}

	// ======================= constructor tests ======================== //

	@Test
	public void test_Ocean_allCellsShouldContainAnEmptySeaWhenOceanIsCreated()
	{
		// if we create an empty Ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// the total amount of cells should be equal to
		int totalCellAmount = Ocean.OCEAN_HEIGHT * Ocean.OCEAN_WIDTH;

		// and each one of them should contain an Empty Sea instance
		int actualAmountOfEmptySea = countTotalSeaArea();
		assertEquals("counting empty sea on initialization", totalCellAmount, actualAmountOfEmptySea);
	}

	@Test
	public void test_Ocean_totalShotsCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initally the total amount of shots fired
		int actualInitialAmountOfShots = ocean.getShotsFired();

		// should be equal to zero
		assertEquals("initial amount of shots fired should be set to zero", 0, actualInitialAmountOfShots);
	}

	@Test
	public void test_Ocean_totalHitsCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initally the total amount of hits
		int actualInitialAmountOfHits = ocean.getHitCount();

		// should be equal to zero
		assertEquals("initial amount of hits should be set to zero", 0, actualInitialAmountOfHits);
	}

	@Test
	public void test_Ocean_totalShipSunkCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initally the total amount of ships sunk
		int actualInitialAmountOfShipSunk = ocean.getShipsSunk();

		// should be equal to zero
		assertEquals("initial amount of sunk ships should be zero", 0, actualInitialAmountOfShipSunk);
	}

	// =================== getters and setters tests ==================== //

	@Test
	public void test_getShotsFired_shouldReturnTheCorrectAmountOfShotsFired()
	{
		// if we shoot a random number of times
		Random random = new Random();
		int expectedTotalShots = random.nextInt(100);

		// at random locations within the ocean's borders
		for (int i = 0; i < expectedTotalShots; i++)
		{
			ocean.shootAt(random.nextInt(Ocean.OCEAN_HEIGHT), random.nextInt(Ocean.OCEAN_WIDTH));
		}

		int actualShotCount = ocean.getShotsFired();

		// we should expect the number of shots registered by the ocean to be
		// whatever it was fired, regardless of the outcome of the shots
		assertEquals("shot count should increase each time a shot is fired", expectedTotalShots, actualShotCount);
	}

	@Test
	public void test_getHitCount_shouldReturnTheCorrectNumberOfHits()
	{
		// if we place a ship onto a newly created ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		int bowRow = 0;
		int bowColumn = 0;
		boolean isHorizontal = true;

		placeShipTypeAt(Battleship.class, bowRow, bowColumn, isHorizontal, ocean);

		// and then we shoot at it once
		int expectedNumberOfHits = 1;
		boolean shipWasHit = ocean.shootAt(bowRow, bowColumn);

		// knowing that we actually hit it
		assertTrue("first hit was succesfull", shipWasHit);

		// and the ship is still afloat
		boolean stillAfloat = !ships[bowRow][bowColumn].isSunk();
		assertTrue("ship is still afloat after first hit", stillAfloat);

		// we should expect the number of hits registered by the ocean to be the
		// same
		int actualNumberOfHits = ocean.getHitCount();
		assertEquals("number of hits correctly reported on first hit", expectedNumberOfHits, actualNumberOfHits);

		// if we shoot one more time and we hit the ship again
		expectedNumberOfHits++;
		shipWasHit = ocean.shootAt(bowRow, bowColumn + 1);

		// knowing that we succesfully hit it again
		assertTrue("second hit was succesfull", shipWasHit);

		// and the ship is not sunk yet
		stillAfloat = !ships[bowRow][bowColumn].isSunk();
		assertTrue("ship is still afloat after second hit", stillAfloat);

		// we should still expect the number of hits registered by the ocean to
		// be the same
		actualNumberOfHits = ocean.getHitCount();
		assertEquals("number of hits correctly reported on second hit", expectedNumberOfHits, actualNumberOfHits);

	}

	@Test
	public void test_getShipsSunk_shouldReturnTheCorrectNumberOfShipsSunk()
	{
		// if have just started the game
		int shipsSunkSoFar = ocean.getShipsSunk();

		// we shouldn't expect to have any sunk ship
		int shipsToSink = 0;
		assertEquals("getting ships sunk count at game start", shipsToSink, shipsSunkSoFar);

		// if then we sink a ship
		shipsToSink++;
		shootUntilShipsAreSunk(shipsToSink);

		// we should expect the values to be equal
		shipsSunkSoFar = ocean.getShipsSunk();
		assertEquals("getting ships sunk count after sinkin one ship", shipsToSink, shipsSunkSoFar);

		// if we sink 3 more ships
		shipsToSink += 3;
		shootUntilShipsAreSunk(shipsToSink);

		// we should still expect the figures to match
		shipsSunkSoFar = ocean.getShipsSunk();
		assertEquals("getting ships sunk count after sinking 4 ships in total", shipsToSink, shipsSunkSoFar);

	}

	// ======================== public methods tests ==================== //

	@Test
	public void test_placeAllShipsRandomly_exactNumberOfEmptySeaShouldBePlacedRandomlyOnTheOcean()
	{
		// the total ocean area should be equal to this
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// and the total area covered by real ships should be equal to this
		int totalShipSpace = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// if we count the empty ocean areas
		int totalSeaArea = countTotalSeaArea();

		// we should get this amount of "empty sea" portions
		int expected = totoalOceanSpace - totalShipSpace;

		assertEquals("total sea area is correct", expected, totalSeaArea);
	}

	@Test
	public void test_placeAllShipsRandomly_exactNumberOfShipsShouldBePlacedRandomlyOnTheOcean()
	{
		// if we count the total number of horizontal ships in the ocean
		int totalNumberOFShips = countShipsOnEachOceanRow(ships);

		// and then we add the count of the vertical ships
		totalNumberOFShips += countShipsOnEachOceanRow(rotatedShips);

		// we should have this amount of ships
		int expected = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		assertEquals("the total number of ships is correct", expected, totalNumberOFShips);
	}

	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotOverlapWhenPlacedRandomlyOnTheOcean()
	{
		// if ships overlap the total area covered by the ships will be less
		// than what it would normally be (for the same amount of ships, having
		// a specific length)
		int totalShipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		int totalShips = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		// the total ocean area should be equal to this
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// count the areas with real ships and the number of ships
		int actualShipArea = totoalOceanSpace - countTotalSeaArea();

		// if we count the total number of ships in the ocean
		int actualTotalShips = countShipsOnEachOceanRow(ships) + countShipsOnEachOceanRow(rotatedShips);

		// we would expect that the total number of ships and the total area
		// covered by the ships is what we would have if there weren't any ship
		// overlapping
		boolean shipsAreNotOverlapping = totalShipArea == actualShipArea && totalShips == actualTotalShips;

		assertTrue("no overlapping ships in the ocean", shipsAreNotOverlapping);
	}

	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotBeAdjacentDiagonallyWhenPlacedRandomlyOnOcean()
	{
		boolean shipsAreDiagonallyAdjacent = false;

		// if you move along each row until you find a horizontal ship bow and
		// there are ships in the ocean spots placed diagonally, there shouldn't
		// be any other ship around
		shipsAreDiagonallyAdjacent = checkDiagonalAdjacencyMovingHorizontally(ships);

		// we expect not to have any diagonal adjacency when moving horizontally
		assertFalse("checking diagonal adjacency moving horizontally along each row", shipsAreDiagonallyAdjacent);

		// then, if you move along each column in the same ocean until you find
		// a vertical ship bow and there are ships in the ocean spots placed
		// diagonally, there shouldn't be any other ship around
		shipsAreDiagonallyAdjacent = checkDiagonalAdjacencyMovingHorizontally(rotatedShips);


		// we expect not to have any diagonal adjacency when moving vertically
		assertFalse("checking diagonal adjacency moving vertically along each column", shipsAreDiagonallyAdjacent);
	}

	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotBeAdjacentOnAStraightLineWhenPlacedRandomlyOnOcean()
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

		// if we map each ship type to the area each of them covers
		HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper = new HashMap<>();
		shipTypeToAreaMapper.put(Battleship.class, 0);
		shipTypeToAreaMapper.put(Cruiser.class, 0);
		shipTypeToAreaMapper.put(Destroyer.class, 0);
		shipTypeToAreaMapper.put(Submarine.class, 0);

		// and we scan the ocean horizontally and increment the ship area for
		// each horizontal ship we encounter
		countShipAreaByShipTypeOnEachOceanRow(shipTypeToAreaMapper, ships);

		// and then do the same vertically
		countShipAreaByShipTypeOnEachOceanRow(shipTypeToAreaMapper, rotatedShips);

		// we should expect that the area covered by each ship type (in the
		// given amount) is what it would be if there were not adjacent ships in
		// the ocean on straight lines
		boolean eachShipTypeCoversTheExpectedArea = shipTypeToAreaMapper.get(Battleship.class) == expectedBattleshipArea
				&& shipTypeToAreaMapper.get(Cruiser.class) == expectedCruiserArea
				&& shipTypeToAreaMapper.get(Destroyer.class) == expecteDestroyerArea
				&& shipTypeToAreaMapper.get(Submarine.class) == expectedSubmarineArea;

		assertTrue("checking horizontal and vertical adjecency", eachShipTypeCoversTheExpectedArea);

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

		// and map each type to the area covered
		HashMap<Class<? extends Ship>, Integer> failMapper = new HashMap<>();
		failMapper.put(Battleship.class, 0);
		failMapper.put(Submarine.class, 0);

		// we should get this amount for each ship type
		int expectedBattleshipSurface = Battleship.BATTLESHIP_LENGTH * 1;
		int expectedSubmarineSurface = Submarine.SUBMARINE_LENGTH * 4;

		// so if we scan the ocean horizontally
		countShipAreaByShipTypeOnEachOceanRow(failMapper, ships);

		// then vertically
		rotatedShips = rotateOceanNinetyDegreeAntiClockwise();
		countShipAreaByShipTypeOnEachOceanRow(failMapper, rotatedShips);

		// we should expect a mismatch between what we found and the expected
		// values
		eachShipTypeCoversTheExpectedArea = failMapper.get(Battleship.class) == expectedBattleshipSurface
				&& failMapper.get(Submarine.class) == expectedSubmarineSurface;

		assertFalse("checking failing horizontal and vertical adjecency", eachShipTypeCoversTheExpectedArea);
	}

	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotExceedOceanBorders()
	{
		// if ships exceed the ocean's borders then the total area covered by
		// the ships will be less than what it would normally be if every ship
		// was within the borders
		int totalShipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// the total ocean area should be equal to this
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// if we count the area with real ships
		int actualShipArea = totoalOceanSpace - countTotalSeaArea();

		// then the total area covered by real ships should be equal to the sum
		// of the number of each real ship type multiplied by the correspondent
		// length
		boolean shipsAreWithinOceanBorders = totalShipArea == actualShipArea;

		assertTrue("all ships are within the ocean's borders", shipsAreWithinOceanBorders);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isOccupied_occupiedOceanSpotsShouldBeFlaggedAccordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and we place ships onto it
		boolean isHorizontal = true;
		placeShipTypeAt(Battleship.class, 0, 0, isHorizontal, ocean);
		placeShipTypeAt(Cruiser.class, 2, 0, isHorizontal, ocean);
		placeShipTypeAt(Destroyer.class, 4, 0, isHorizontal, ocean);
		placeShipTypeAt(Submarine.class, 6, 0, isHorizontal, ocean);

		// then any random ocean spots from the bow till the ship length should
		// be marked as occupied
		Random random = new Random();
		boolean firstLocationIsOccupied = ocean.isOccupied(0, random.nextInt(Battleship.BATTLESHIP_LENGTH));
		boolean secondLocationIsOccupied = ocean.isOccupied(2, random.nextInt(Cruiser.CRUISER_LENGTH));
		boolean thirdLocationIsOccupied = ocean.isOccupied(4, random.nextInt(Destroyer.DESTROYER_LENGTH));
		boolean fourthLocationIsOccupied = ocean.isOccupied(6, random.nextInt(Submarine.SUBMARINE_LENGTH));

		boolean locationIsOccupied = true;
		assertEquals("first location is occupied", locationIsOccupied, firstLocationIsOccupied);
		assertEquals("second location is occupied", locationIsOccupied, secondLocationIsOccupied);
		assertEquals("third location is occupied", locationIsOccupied, thirdLocationIsOccupied);
		assertEquals("fourth location is occupied", locationIsOccupied, fourthLocationIsOccupied);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isOccupied_clearOceanSpotsShouldBeFlaggedAccordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// whatever random area that we pick within its borders
		Random random = new Random();
		int row = random.nextInt(Ocean.OCEAN_HEIGHT);
		int column = random.nextInt(Ocean.OCEAN_WIDTH);

		// they should not be flagged as occupied
		boolean oceanLocationIsOccupied = false;
		boolean locationIsOccupied = ocean.isOccupied(row, column);

		assertEquals("clear ocean spots should be flagged as such", oceanLocationIsOccupied, locationIsOccupied);
	}

	@Test
	public void test_shootAt_theNumberOfHitsShouldIncreaseWhenARealAfloatShipGetsHit()
	{
		// if we place a real ship onto a newly created ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		int bowRow = 3;
		int bowColumn = 4;
		boolean isHorizontal = false;
		placeShipTypeAt(Cruiser.class, bowRow, bowColumn, isHorizontal, ocean);

		// knowing that the intial hit count will be set to zero
		int initialNumberOfHits = 0;

		// and then we shoot at it once
		boolean shipWasHit = ocean.shootAt(bowRow, bowColumn);

		// knowing that we actually hit it
		assertTrue("first hit was succesfull", shipWasHit);

		// and the ship is still afloat
		boolean stillAfloat = !ships[bowRow][bowColumn].isSunk();
		assertTrue("ship is still afloat", stillAfloat);

		// we should expect the number of hits registered by the ocean to
		// increase by one
		int numberOfHitsAfterFirstSuccessfulShot = ocean.getHitCount();

		boolean numberOfHitsHasIncrementedAfterFirstSuccessfulShot = numberOfHitsAfterFirstSuccessfulShot
				- initialNumberOfHits == 1;
		assertTrue("number of hits has increased after a successful shot",
				numberOfHitsHasIncrementedAfterFirstSuccessfulShot);
	}

	@Test
	public void test_shootAt_theNumberOfHitsShouldNotIncreaseWhenTheShotMissesTheTarget()
	{
		// if we create a new empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and then we shoot at a random location
		Random random = new Random();
		boolean shipWasHit = ocean.shootAt(random.nextInt(Ocean.OCEAN_HEIGHT), random.nextInt(Ocean.OCEAN_WIDTH));

		// knowing that the intial hit count will be set to zero
		int initialNumberOfHits = 0;

		// and that the shot was actually unsuccesful
		assertFalse("shot was a miss", shipWasHit);

		// we should expect the number of hits registered by the ocean to
		// stay unchanged
		int numberOfHitsAfterFirstUnsuccessfulShot = ocean.getHitCount();

		boolean numberOfHitsHasDidNotIncreaseAfterUnsuccesfulShot = numberOfHitsAfterFirstUnsuccessfulShot == initialNumberOfHits;
		assertTrue("number of hits has increased after a successful shot",
				numberOfHitsHasDidNotIncreaseAfterUnsuccesfulShot);
	}

	@Test
	public void test_shootAt_shootingAfloatShipShouldBeSuccessful()
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
		boolean hitAfloatShipIsSuccessful = ocean.shootAt(random.nextInt(Battleship.BATTLESHIP_LENGTH), column);

		// we should expect to catch an afloat ship
		assertTrue("hitting an afloat ship", hitAfloatShipIsSuccessful);
	}

	@Test
	public void test_shootAt_repeatedlyShootingAfloatShipShouldBeSuccessful()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		int row = 0;
		int column = 0;
		boolean isHorizontal = false;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

		// if we shoot at it
		boolean succesfulShot = ocean.shootAt(row, column);

		// knowing that we didn't sink it
		boolean shipIsSunk = ships[row][column].isSunk();
		assertFalse("ship is afloat after first shot", shipIsSunk);

		// we should expect the shot to be succesful
		assertTrue("succesfully hitting an afloat ship once", succesfulShot);

		// if then we shoot at it again
		succesfulShot = ocean.shootAt(row, column);

		// knowing that the ship is still afloat
		shipIsSunk = ships[row][column].isSunk();
		assertFalse("ship is afloat after second shot", shipIsSunk);

		// we should expect the shot to be still successful
		assertTrue("succesfully hitting an afloat ship twice", succesfulShot);
	}

	@Test
	public void test_shootAt_shootingSunkShipsShouldBeUnsuccessful()
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
	public void test_shootAt_theNumberOfShotsFiredShouldBeUpdatedAtEachShot()
	{
		// if we shoot a random number of times
		Random random = new Random();
		int expectedTotalShots = random.nextInt(100);

		// at random locations within the ocean's borders
		for (int i = 0; i < expectedTotalShots; i++)
		{
			ocean.shootAt(random.nextInt(Ocean.OCEAN_HEIGHT), random.nextInt(Ocean.OCEAN_WIDTH));
		}

		// we should expect the number of shots registered by the ocean to be
		// whatever it was fired
		int actualNumberOfShotsFired = ocean.getShotsFired();

		assertEquals("verifying shooting at ocean's locations increases after each shot", expectedTotalShots,
				actualNumberOfShotsFired);
	}

	@Test
	public void test_shootAt_numberOfHitsShouldBeUpdatedOnlyAfterSuccessfulShots()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a real ship at location (0,0), vertically
		int bowRow = 0;
		int bowColumn = 0;
		boolean isHorizontal = false;
		placeShipTypeAt(Battleship.class, bowRow, bowColumn, isHorizontal, ocean);

		// and then we shoot across its length till the end of the ocean's
		// border
		fireShotsAcrossOceanLine(Ocean.OCEAN_HEIGHT, bowRow, bowColumn, isHorizontal);

		// knowing that the Battleship does not span across the whole length
		// of the ocean's side
		assertTrue("the ship length is shorter than the ocean's height",
				Battleship.BATTLESHIP_LENGTH < Ocean.OCEAN_HEIGHT);

		// and it is placed at a distance greater than its length from teh
		// ocean's border
		int sternColumn = bowColumn + Battleship.BATTLESHIP_LENGTH - 1;
		int distanceFromTheBorder = Ocean.OCEAN_HEIGHT - sternColumn;
		assertTrue("the ship is not placed at a distance greater than its length from the ocean border",
				distanceFromTheBorder > Battleship.BATTLESHIP_LENGTH);

		// we should expect to only get 4 successful shots (one for each ship
		// part across the ship length)
		int expectedNumberOfHits = Battleship.BATTLESHIP_LENGTH;
		int actualNumberOfHits = ocean.getHitCount();
		assertEquals("hit count gets updated after each successful shot", expectedNumberOfHits, actualNumberOfHits);
	}

	@Test
	public void test_hasSunkShipAt_shouldReturnTrueIfLocationContainsSunkShip()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and place a ship onto it
		int row = 0;
		int column = 0;
		boolean isHorizontal = true;
		placeShipTypeAt(Destroyer.class, row, column, isHorizontal, ocean);

		// if we shoot across its length without sinking it
		int numberOfShotsToFire = Destroyer.DESTROYER_LENGTH;
		fireShotsAcrossOceanLine(numberOfShotsToFire, row, column, isHorizontal);

		// and then check if the locations across which the ship spans contains
		// a sunk ship
		Random random = new Random();
		boolean locationHasASunkShip = ocean.hasSunkShipAt(row,
				random.nextInt(column + Destroyer.DESTROYER_LENGTH - 1));

		// we should expect it to actually have one
		assertTrue("location contains a sunk ship", locationHasASunkShip);
	}

	@Test
	public void test_hasSunkShipAt_shouldReturnFalseIfLocationContainsAfloatShip()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and place a ship onto it
		int row = 0;
		int column = 0;
		boolean isHorizontal = true;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

		// if we shoot across its length without sinking it
		int numberOfShotsToFire = Battleship.BATTLESHIP_LENGTH - 1;
		fireShotsAcrossOceanLine(numberOfShotsToFire, row, column, isHorizontal);

		// and then check if the locations across which the ships spans contains
		// a sunk ship
		Random random = new Random();
		boolean locationHasASunkShip = ocean.hasSunkShipAt(row,
				random.nextInt(column + Battleship.BATTLESHIP_LENGTH - 1));


		// we should expect it not to have one
		assertFalse("ocean location with partly damaged ship should not contain a sunk ship", locationHasASunkShip);
	}

	@Test
	public void test_hasSunkShipAt_shouldReturnFalseIfLocationContainsEmptySea()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// then check a random location to see if it ontains a sunk ship
		Random random = new Random();
		boolean locationHasASunkShip = ocean.hasSunkShipAt(random.nextInt(Ocean.OCEAN_HEIGHT),
				random.nextInt(Ocean.OCEAN_WIDTH));

		// we should expect it not to have one
		assertFalse("empty sea locations do not have sunk ships on them", locationHasASunkShip);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isGameOver_shouldReturnFalseIfNotAllShipsHaveBeenSunk()
	{
		// if we check the game status before sinking all the ships
		// we should expect the game to be on
		boolean gameStatus = ocean.isGameOver();

		assertFalse("checking game status at game start", gameStatus);

		// if then we fire shots unitl we sink a number of ships
		// less than the total number of ships
		Random random = new Random();
		int numberOfShipsToSink = random.nextInt(4);

		shootUntilShipsAreSunk(numberOfShipsToSink);

		// and we check the game status again
		gameStatus = ocean.isGameOver();

		// we should expect it to be still on
		assertFalse("checking game status after sinking a few ships", gameStatus);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isGameOver_shouldReturnTrueIfAllShipsHaveBeenSunk()
	{
		// if we check the game status before sinking all the ships
		// we should expect the game to be on
		boolean gameStatus = ocean.isGameOver();

		assertFalse("checking game status at game start", gameStatus);

		// if then we fire shots unitl we sink all the ships
		int numberOfShipsToSink = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		shootUntilShipsAreSunk(numberOfShipsToSink);

		// and we check the game status again
		gameStatus = ocean.isGameOver();

		// we should expect it to be over
		assertTrue("checking game status after sinking every ship", gameStatus);
	}

	@Test
	public void test_getShipsTypeAt_shouldReturnTheCorrectShipType()
	{
		// if we have an empty ocean
		ocean = new Ocean();

		// and we get the ship type anywhere at its location
		Random random = new Random();
		String emptySeaType = ocean.getShipTypeAt(random.nextInt(Ocean.OCEAN_HEIGHT),
				random.nextInt(Ocean.OCEAN_WIDTH));

		// we should expect them to be empty sea types
		assertEquals(EmptySea.EMPTY_SEA_TYPE, emptySeaType);

		// if then we place a few ships onto the ocean
		int bowRow = 0;
		int bowColumn = 0;
		boolean isHorizontal = true;

		placeShipTypeAt(Battleship.class, bowRow, bowColumn, isHorizontal, ocean);
		placeShipTypeAt(Cruiser.class, bowRow + 1, bowColumn, isHorizontal, ocean);
		placeShipTypeAt(Destroyer.class, bowRow + 2, bowColumn, isHorizontal, ocean);
		placeShipTypeAt(Submarine.class, bowRow + 3, bowColumn, isHorizontal, ocean);

		// and get their ship types
		String battleshipType = ocean.getShipTypeAt(bowRow, bowColumn);
		String cruiserType = ocean.getShipTypeAt(bowRow + 1, bowColumn);
		String destroyerType = ocean.getShipTypeAt(bowRow + 2, bowColumn);
		String submarineType = ocean.getShipTypeAt(bowRow + 3, bowColumn);

		// we should expect to match their correspondent ship type
		assertEquals("battleships return the correct ship type", Battleship.BATTLESHIP_TYPE, battleshipType);
		assertEquals("cruiser return the correct ship type", Cruiser.CRUISER_TYPE, cruiserType);
		assertEquals("destroyer return the correct ship type", Destroyer.DESTROYER_TYPE, destroyerType);
		assertEquals("submarines return the correct ship type", Submarine.SUBMARINE_TYPE, submarineType);
	}

	// ======================= helper methods ======================== //

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

	private int countTotalSeaArea()
	{
		int totalSeaArea = 0;

		for (int i = 0; i < Ocean.OCEAN_WIDTH; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_HEIGHT; j++)
			{
				if (!ships[i][j].isRealShip())
				{
					totalSeaArea++;
				}
			}
		}

		return totalSeaArea;
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

					// increment the total count of the "ship area" and stop
					// when an empty sea is found or the edge of the border is
					// reached
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

	private boolean checkDiagonalAdjacencyMovingHorizontally(Ship[][] ships)
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

	private <T extends Ship> void placeShipTypeAt(Class<T> shipClass, int bowRow, int bowColumn, boolean isHorizontal,
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

	private <T extends Ship> Ship createShip(Class<T> shipClass)
	{
		if (shipClass == Battleship.class)
		{
			return new Battleship();
		}
		else if (shipClass == Cruiser.class)
		{
			return new Cruiser();
		}
		else if (shipClass == Destroyer.class)
		{
			return new Destroyer();
		}
		else if (shipClass == Submarine.class)
		{
			return new Submarine();
		}
		else
		{
			return new EmptySea();
		}
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

	private void shootUntilShipsAreSunk(int numberOfShipsToSink)
	{
		if (numberOfShipsToSink == 0)
		{
			// do not attempt if you don't want to sink any ship
			return;
		}

		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				if (ocean.shootAt(i, j))
				{
					if (ocean.getShipsSunk() == numberOfShipsToSink)
					{
						// stop shooting once the desired number of ships have
						// been sunk
						return;
					}
				}
			}
		}
	}

	/**
	 * Helper method to shoot at a ship across its length, in a sequential way.
	 * No shots will be fired twice at the samem location.
	 *
	 * @param ship
	 *            target to shoot at
	 * @param shots
	 *            number of shots to be fired in sequence from the bow
	 *            coordinates of the ship
	 */
	private void fireShotsAcrossOceanLine(int shots, int row, int column, boolean horizontal)
	{
		int start = horizontal ? column : row;

		for (int i = start; i < start + shots; i++)
		{
			// if the ship is horizontal increment the column, otherwise
			// increment the row
			ocean.shootAt(horizontal ? row : i, horizontal ? i : column);
		}
	}

	// ========================== debug =============================== //

	private static void print(Ship[][] matrix)
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
