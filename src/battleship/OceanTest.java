/**
 *
 */
package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
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
	 * to aid performing any operation on vertical ships without the hassle of
	 * changing the logic compared to what happens to vertical ships.
	 */
	private Ship[][] rotatedShips;

	/**
	 * Whether or not the ocean matrix should be rotated: set to false when the
	 * matrix containing the ships needs to be scanned horizontally, true
	 * otherwise
	 */
	private boolean rotateOcean = true;

	/** default timeout test duration in milliseconds */
	private static final int DEFAULT_TIMEOUT = 2000;

	/**
	 * Initializes a new instance of the {@linkplain Ocean} class and places all
	 * the ships randomly; gets the {@linkplain Ship} 2-dimentional array,
	 * creates a matrix of the same ships rotated 90 degrees anti-clockwise to
	 * aid testing.
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

	/**
	 * When the ocean is created all the cells contain an empty sea
	 */
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

	/**
	 * When the ocean is created the count of shots fired should be set to zero
	 */
	@Test
	public void test_Ocean_totalShotsCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initally the total amount of shots fired
		int actualInitialAmountOfShots = ocean.getShotsFired();

		// should be equal to zero
		assertEquals("initial amount of shots fired should be set to zero", 0, actualInitialAmountOfShots);
	}

	/**
	 * When the ocean is created the count of succesful hits should be set to
	 * zero
	 */
	@Test
	public void test_Ocean_totalHitsCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initally the total amount of hits
		int actualInitialAmountOfHits = ocean.getHitCount();

		// should be equal to zero
		assertEquals("initial amount of hits should be set to zero", 0, actualInitialAmountOfHits);
	}

	/**
	 * When the ocean is created the count of ship sunk should be set to zero
	 */
	@Test
	public void test_Ocean_totalShipSunkCountShouldBeSetToZeroWhenOceanIsCreated()
	{
		// initially the total amount of ships sunk
		int actualInitialAmountOfShipSunk = ocean.getShipsSunk();

		// should be equal to zero
		assertEquals("initial amount of sunk ships should be zero", 0, actualInitialAmountOfShipSunk);
	}

	// =================== getters and setters tests ==================== //

	/**
	 * Getter should return correct value
	 */
	@Test
	public void test_getShotsFired_shouldReturnTheCorrectAmountOfShotsFired()
	{
		// if we shoot 5 time at any location
		int expectedTotalShots = 5;
		ocean.shootAt(0, 0);
		ocean.shootAt(0, 1);
		ocean.shootAt(2, 5);
		ocean.shootAt(3, 2);
		ocean.shootAt(9, 9);

		// and we goet the count of shots fired
		int actualShotCount = ocean.getShotsFired();

		// we should expect the number of shots registered by the ocean to be
		// whatever it was fired (regardless of the outcome of the shots)
		assertEquals("shot count should increase each time a shot is fired", expectedTotalShots, actualShotCount);
	}

	/**
	 * Getter should return correct value
	 */
	@Test
	public void test_getHitCount_shouldReturnTheCorrectNumberOfHits()
	{
		// if we place a ship onto a newly created ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// horizontally at location (0, 0)
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

	/**
	 * Getter should return correct value
	 */
	@Test
	public void test_getShipsSunk_shouldReturnTheCorrectNumberOfShipsSunk()
	{
		// if have just started the game
		int shipsSunkSoFar = ocean.getShipsSunk();

		// we shouldn't expect to have any sunk ship
		int shipsToSink = 0;
		assertEquals("getting ships sunk count at game start", shipsToSink, shipsSunkSoFar);

		// if then we sink a ship
		sinkShips(shipsToSink += 1);

		// we should expect the count of ships sunk so far to be equal to the
		// count of ships we sunk
		shipsSunkSoFar = ocean.getShipsSunk();
		assertEquals("getting ships sunk count after sinkin one ship", shipsToSink, shipsSunkSoFar);

		// if we sink 3 more ships
		sinkShips(shipsToSink += 3);

		// we should still expect the figures to match
		shipsSunkSoFar = ocean.getShipsSunk();
		assertEquals("getting ships sunk count after sinking 4 ships in total", shipsToSink, shipsSunkSoFar);
	}

	// ======================== public methods tests ==================== //

	/**
	 * The number of empty sea ships placed on the ocean should be correct
	 */
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

		assertEquals("incorrect total empty sea area", expected, totalSeaArea);
	}

	/**
	 * The number of real ships placed on the ocean should be correct
	 */
	@Test
	public void test_placeAllShipsRandomly_exactNumberOfShipsShouldBePlacedRandomlyOnTheOcean()
	{
		// if we count the total number of horizontal ships in the ocean
		int totalNumberOFShips = countHorizontalShips(!rotateOcean);

		// and then we add the count of the vertical ships
		totalNumberOFShips += countHorizontalShips(rotateOcean);

		// we should have this amount of ships
		int expected = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		assertEquals("incorrect total number of ships", expected, totalNumberOFShips);
	}

	/**
	 * Ships should not overlap. If that is not the case the total area covered
	 * by the ships will be less than what it would normally be (for the same
	 * amount of ships, having a specific length)
	 */
	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotOverlapWhenPlacedRandomlyOnTheOcean()
	{
		// knowing that the total ship are should be qual to this
		int totalShipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// and given this amount of ships
		int totalShips = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;

		// and since the total ocean area should be equal to this
		int totoalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// if we count the actual areas with real ships
		int actualShipArea = totoalOceanSpace - countTotalSeaArea();

		// and also the total number of ships in the ocean
		int actualTotalShips = countHorizontalShips(!rotateOcean) + countHorizontalShips(rotateOcean);

		// if ships don't overlap we would expect that the actual total number
		// of ships and the actual total area covered by the ships to match
		// respectively with the total ships count and total ship area
		boolean shipsAreNotOverlapping = totalShipArea == actualShipArea && totalShips == actualTotalShips;

		assertTrue("found overlapping ships in the ocean", shipsAreNotOverlapping);
	}

	/**
	 * Ships should no t be adjacent diagonally. If by moving along each row
	 * until you find a horizontal ship bow there are ships in the ocean spots
	 * placed diagonally, then that means ther are adjacent ships diagonally. By
	 * moving vertically the smae logic applies
	 */
	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotBeAdjacentDiagonallyWhenPlacedRandomlyOnOcean()
	{
		boolean shipsAreDiagonallyAdjacent = false;

		// if for every horizontal ship we check if there are any ship which are
		// adjacent to it diagoanlly
		shipsAreDiagonallyAdjacent = checkDiagonalAdjacencyMovingHorizontally(!rotateOcean);

		// we expect not to have any diagonal adjacency
		assertFalse("found diagonally adjacenct ships moving horizontally along each row", shipsAreDiagonallyAdjacent);

		// then if we do the same thing vertically
		shipsAreDiagonallyAdjacent = checkDiagonalAdjacencyMovingHorizontally(rotateOcean);

		// we should still expect the same outcome
		assertFalse("found diagonally adjacenct ships moving vertically along each column", shipsAreDiagonallyAdjacent);
	}

	/**
	 * Ships should not be adjacent vertically or horizontally. If there are any
	 * adjacent ship horizontally or vertically then there will be a mismatch
	 * between the total area covered by a certain ship type and what we would
	 * normally expect, at least for one ship type. So for instance if a
	 * battleship is adjacent to another ship (vertically or horizontally) its
	 * length will be longer than what it would normally be if it wasn't:
	 * therefore rather than 1 * 4 squared occupied by the battleship, but we
	 * would get 5. The error could compensate for a particular type of ship,
	 * but overall at least one would be different, therefore we have to check
	 * all the ships and every ship type
	 */
	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotBeAdjacentOnAStraightLineWhenPlacedRandomlyOnOcean()
	{
		// for each shp type this would be the expected total area
		int expectedBattleshipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH;
		int expectedCruiserArea = Ocean.CRUISERS * Cruiser.CRUISER_LENGTH;
		int expecteDestroyerArea = Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH;
		int expectedSubmarineArea = Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// so if we map each ship type to the area each of them will cover
		HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper = new HashMap<>();
		shipTypeToAreaMapper.put(Battleship.class, 0);
		shipTypeToAreaMapper.put(Cruiser.class, 0);
		shipTypeToAreaMapper.put(Destroyer.class, 0);
		shipTypeToAreaMapper.put(Submarine.class, 0);

		// and then we scan the ocean horizontally and increment the ship area
		// for each horizontal ship we encounter
		countShipAreaByShipTypeHorizontally(shipTypeToAreaMapper, !rotateOcean);

		// and then do the same vertically
		countShipAreaByShipTypeHorizontally(shipTypeToAreaMapper, rotateOcean);

		// we should expect that the area covered by each ship type (in the
		// given amount) is what it would be if there wasn't any adjacent ships
		// in the ocean vertically and horizontally
		boolean eachShipTypeCoversTheExpectedArea = shipTypeToAreaMapper.get(Battleship.class) == expectedBattleshipArea
				&& shipTypeToAreaMapper.get(Cruiser.class) == expectedCruiserArea
				&& shipTypeToAreaMapper.get(Destroyer.class) == expecteDestroyerArea
				&& shipTypeToAreaMapper.get(Submarine.class) == expectedSubmarineArea;

		assertTrue("found adjacent ships horizontally or vertically", eachShipTypeCoversTheExpectedArea);
	}

	/**
	 * Ships should not exceed the ocean's borders. If ships exceed the ocean's
	 * borders then the total area covered by the ships will be less than what
	 * it would normally be if every ship was within the borders
	 */
	@Test
	public void test_placeAllShipsRandomly_shipsShouldNotExceedOceanBorders()
	{
		int totalShipArea = Ocean.BATTLESHIPS * Battleship.BATTLESHIP_LENGTH + Ocean.CRUISERS * Cruiser.CRUISER_LENGTH
				+ Ocean.DESTROYERS * Destroyer.DESTROYER_LENGTH + Ocean.SUBMARINES * Submarine.SUBMARINE_LENGTH;

		// the total ocean area should be equal to this
		int totalOceanSpace = Ocean.OCEAN_WIDTH * Ocean.OCEAN_HEIGHT;

		// if we count the area with real ships
		int actualShipArea = totalOceanSpace - countTotalSeaArea();

		// then the total area covered by real ships should be equal to the sum
		// of the number of each real ship type multiplied by the correspondent
		// length
		boolean shipsAreWithinOceanBorders = totalShipArea == actualShipArea;

		assertTrue("Found ships which aren't within the ocean's borders", shipsAreWithinOceanBorders);
	}

	/**
	 * Since the amount of ships and their length are not enogh to justify a
	 * painful process to place the ships these should be placed within 10
	 * seconds. If that is not the case the algorithm could be wrong
	 */
	@Test(timeout = DEFAULT_TIMEOUT * 5)
	public void test_Ocean_shipsShouldBePlacedOnTheOceanWithinTenSeconds()
	{
		// if we have a brand new ocean
		Ocean ocean = new Ocean();

		// and we place the ships onto it, the operation should not timeout
		ocean.placeAllShipsRandomly();
	}

	/**
	 * If an ocean spot is occupied it should be marked as such
	 */
	@Test
	public void test_isOccupied_occupiedOceanSpotsShouldBeFlaggedAccordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// and we place a ship onto it, horizontally for instance
		boolean isHorizontal = true;
		int row = 0;
		int column = 0;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

		// then any ocean spots from the bow till the ship length should
		// be marked as occupied
		boolean firstLocationIsOccupied = ocean.isOccupied(row, column);
		boolean secondLocationIsOccupied = ocean.isOccupied(row, column + 1);
		boolean thirdLocationIsOccupied = ocean.isOccupied(row, column + 2);
		boolean fourthLocationIsOccupied = ocean.isOccupied(row, column + 3);

		assertTrue("first location is not occupied", firstLocationIsOccupied);
		assertTrue("second location is not occupied", secondLocationIsOccupied);
		assertTrue("third location is not occupied", thirdLocationIsOccupied);
		assertTrue("fourth location is not occupied", fourthLocationIsOccupied);
	}

	/**
	 * If an ocean spot is not occupied it should be marked as such
	 */
	@Test
	public void test_isOccupied_clearOceanSpotsShouldBeFlaggedAccordingly()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// whatever location we pick within its borders
		int row = 0;
		int column = 0;

		// they should not be flagged as occupied
		boolean locationIsOccupied = ocean.isOccupied(row, column) && ocean.isOccupied(row + 9, column + 1)
				&& ocean.isOccupied(row + 4, column) && ocean.isOccupied(row + 2, column + 3);

		assertFalse("clear ocean spots was flagged as occupied", locationIsOccupied);
	}

	/**
	 * Asking the ocean if a location out of its range contains a ship should
	 * return false
	 */
	@Test
	public void test_isOccupied_outOfRangeOceanSpotsShouldBeFlaggedAsClear()
	{
		// if we pick a location with column coordinate greater than the ocean's
		// width
		int row = 5;
		int column = Ocean.OCEAN_WIDTH + 1;
		// it should not be flagged as occupied
		boolean outOfRangeLocationIsOccupied = ocean.isOccupied(row, column);
		assertFalse("locations with column greater than ocean's width should not be flagged as occupied",
				outOfRangeLocationIsOccupied);

		// and if we pick a location with row coordinate greater than the
		// ocean's height
		row = Ocean.OCEAN_HEIGHT + 4;
		column = 0;
		// it should not be flagged as occupied either
		outOfRangeLocationIsOccupied = ocean.isOccupied(row, column);
		assertFalse("locations with row greater than ocean's height should not be flagged as occupied",
				outOfRangeLocationIsOccupied);

		// if then we pick a location with a negative column coordinate
		row = 2;
		column = -4;
		// it should not be flagged as occupied
		outOfRangeLocationIsOccupied = ocean.isOccupied(row, column);
		assertFalse("locations with negative column coordinate should not be flagged as occupied",
				outOfRangeLocationIsOccupied);

		// finally, if we pick a location with a negative row coordinate
		row = -6;
		column = 0;
		// it should not be flagged as occupied
		outOfRangeLocationIsOccupied = ocean.isOccupied(row, column);
		assertFalse("locations with negative row coordinate should not be flagged as occupied",
				outOfRangeLocationIsOccupied);
	}

	/**
	 * Each time the shot hits a an afloat ship the hit count should grow
	 */
	@Test
	public void test_shootAt_theNumberOfHitsShouldIncreaseWhenRealAfloatShipGetsHit()
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
		assertTrue("first hit was a miss", shipWasHit);

		// and the ship is still afloat
		boolean stillAfloat = !ships[bowRow][bowColumn].isSunk();
		assertTrue("ship was already sunk", stillAfloat);

		// we should expect the number of hits registered by the ocean to
		// increase by one
		int numberOfHitsAfterFirstSuccessfulShot = ocean.getHitCount();

		boolean numberOfHitsHasIncrementedAfterFirstSuccessfulShot = numberOfHitsAfterFirstSuccessfulShot
				- initialNumberOfHits == 1;
		assertTrue("number of hits hasn't increased after a successful shot",
				numberOfHitsHasIncrementedAfterFirstSuccessfulShot);
	}

	/**
	 * If the shot is a miss the hit count should not change
	 */
	@Test
	public void test_shootAt_theNumberOfHitsShouldNotIncreaseWhenTheShotMissesTheTarget()
	{
		// if we create a new empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and then we shoot at any location without hitting any ship
		// because no ships had been placed yet
		boolean shipWasHit = ocean.shootAt(5, 6);

		// knowing that the intial hit count will be set to zero
		int initialNumberOfHits = 0;

		// and that the shot was actually unsuccesful
		assertFalse("shot unexpectedly hit a ship", shipWasHit);

		// we should expect the number of hits registered by the ocean to
		// stay unchanged
		int numberOfHitsAfterFirstUnsuccessfulShot = ocean.getHitCount();

		boolean numberOfHitsHasDidNotIncreaseAfterUnsuccesfulShot = numberOfHitsAfterFirstUnsuccessfulShot == initialNumberOfHits;
		assertTrue("number of hits has increased even without hitting a ship",
				numberOfHitsHasDidNotIncreaseAfterUnsuccesfulShot);
	}

	/**
	 * Shooting afloat ships should return true
	 */
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
		boolean hitAfloatShipIsSuccessful = ocean.shootAt(row + 1, column);

		// we should expect to catch an afloat ship
		assertTrue("hitting an afloat ship was unsuccefull", hitAfloatShipIsSuccessful);
	}

	/**
	 * Repeatedly shooting afloat ships should return true as long as is did not
	 * sink in the meantime
	 */
	@Test
	public void test_shootAt_shootingAfloatShipWithoutSinkingItShouldBeSuccessful()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		int row = 3;
		int column = 4;
		boolean isHorizontal = false;
		placeShipTypeAt(Battleship.class, row, column, isHorizontal, ocean);

		// if we shoot at it
		boolean succesfulShot = ocean.shootAt(row, column);

		// knowing that we didn't sink it
		boolean shipIsSunk = ships[row][column].isSunk();
		assertFalse("ship was sunk after only one shot", shipIsSunk);

		// we should expect the shot to be succesful
		assertTrue("hitting an afloat ship once without sinking it was not succesfull", succesfulShot);

		// if then we shoot at it again
		succesfulShot = ocean.shootAt(row, column);

		// knowing that the ship is still afloat
		shipIsSunk = ships[row][column].isSunk();
		assertFalse("ship was already sunk after second shot", shipIsSunk);

		// we should expect the shot to be still successful
		assertTrue("hitting an afloat ship twice was not succesfull", succesfulShot);
	}

	/**
	 * Shooting already sunk ships should return false
	 */
	@Test
	public void test_shootAt_shootingSunkShipsShouldBeUnsuccessful()
	{
		// if we create an empty ocean
		ocean = new Ocean();
		ships = ocean.getShipArray();

		// and place a ship onto it
		int bowRow = 0;
		int bowColumn = 0;
		boolean isHorizontal = true;
		placeShipTypeAt(Destroyer.class, bowRow, bowColumn, isHorizontal, ocean);

		// if we shoot across its length until we sink it
		boolean hitSunkShip;
		fireShotsAcrossOceanLine(Destroyer.DESTROYER_LENGTH, bowRow, bowColumn, isHorizontal);

		// then shoot again at it anywhere across its length
		hitSunkShip = ocean.shootAt(bowRow, bowColumn) && ocean.shootAt(bowRow, bowColumn + 1);

		// we should expect an unsuccessful shot
		assertFalse("hitting an already sunk ship had a succesfull outcome", hitSunkShip);
	}

	/**
	 * The number of shots fired should increase at each attempt regarless of
	 * the outcome
	 */
	@Test
	public void test_shootAt_theNumberOfShotsFiredShouldBeUpdatedAtEachShot()
	{
		// if we shoot any number of times
		int expectedTotalShots = 3;

		// anywhere within or outside the ocean's borders
		// and regardless of the outcome of the shots
		int row = 0;
		int column = 0;
		ocean.shootAt(row, column + 1000);
		ocean.shootAt(row, column - 68);
		ocean.shootAt(row + 5, column);

		// we should expect the number of shots registered by the ocean to be
		// whatever it was fired
		int actualNumberOfShotsFired = ocean.getShotsFired();

		assertEquals("shots fired count did not increase after each shot", expectedTotalShots,
				actualNumberOfShotsFired);
	}

	/**
	 * The number of hits should increase at each time the shot is succesful
	 */
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
		assertTrue("the ship length was longer than the ocean's height",
				Battleship.BATTLESHIP_LENGTH < Ocean.OCEAN_HEIGHT);

		// and it is placed at a distance greater than its length from the
		// ocean's border
		int sternColumn = bowColumn + Battleship.BATTLESHIP_LENGTH - 1;
		int distanceFromTheBorder = Ocean.OCEAN_HEIGHT - sternColumn;
		assertTrue("the ship length exceeded the ocean border", distanceFromTheBorder > Battleship.BATTLESHIP_LENGTH);

		// we should expect to only get 4 successful shots (one for each ship
		// part across the ship length)
		int expectedNumberOfHits = Battleship.BATTLESHIP_LENGTH;
		int actualNumberOfHits = ocean.getHitCount();
		assertEquals("hit count was not updated after each successful shot", expectedNumberOfHits, actualNumberOfHits);
	}

	/**
	 * Shooting out of range locations should return false
	 */
	@Test
	public void test_shootAt_shootingAtLocationsOutOfRangeShouldReturnFalse()
	{
		// if we shoot at a location with row coordinate greater than the
		// ocean's height
		int row = Ocean.OCEAN_HEIGHT + 5;
		int column = 0;
		// we should expect an unsuccesfull outcome
		boolean shotWasSuccesful = ocean.shootAt(row, column);
		assertFalse("shooting at locations with row coordinate greater than ocean's height was successful",
				shotWasSuccesful);

		// if then we shoot at a location with a negative row coordinate
		row = -100;
		column = 3;
		// we should expect an unsuccesfull outcome
		shotWasSuccesful = ocean.shootAt(row, column);
		assertFalse("shooting at locations with negative row coordinate was succesful", shotWasSuccesful);

		// and if we shoot at a location with a column coordinate greater than
		// the ocean's width
		row = 6;
		column = Ocean.OCEAN_WIDTH + 77;
		// we should expect an unsuccesfull outcome
		shotWasSuccesful = ocean.shootAt(row, column);
		assertFalse("shooting at locations with column coordinate greater than ocean's width was succesful",
				shotWasSuccesful);

		// finally if we shoot at a location with negative column coordinate
		row = 9;
		column = -5;
		// we should still expect an unsuccesfull outcome
		shotWasSuccesful = ocean.shootAt(row, column);
		assertFalse("shooting at locations with negative column coordinate was succesful", shotWasSuccesful);
	}

	/**
	 * If the location contains a sunk ship it should be reported correctly
	 */
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

		// if we shoot across its length until we sink it
		int numberOfShotsToFire = Destroyer.DESTROYER_LENGTH;
		fireShotsAcrossOceanLine(numberOfShotsToFire, row, column, isHorizontal);

		// and then check if any of the locations across which the ship spans
		// contains a sunk ship
		boolean locationHasASunkShip = ocean.hasSunkShipAt(row, column + 0) && ocean.hasSunkShipAt(row, column + 1);

		// we should expect it to actually have one
		assertTrue("location did not contain a sunk ship", locationHasASunkShip);
	}

	/**
	 * If the location contains an afloat ship it should be reported correctly
	 */
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

		// and then check if each of the locations across which the ships spans
		// contains a sunk ship
		boolean locationHasASunkShip = ocean.hasSunkShipAt(row, column + 0) && ocean.hasSunkShipAt(row, column + 1)
				&& ocean.hasSunkShipAt(row, column + 2) && ocean.hasSunkShipAt(row, column + 3);

		// we should expect it not to have one
		assertFalse("ocean location with partly damaged ship flagged as containing a sunk ship", locationHasASunkShip);
	}

	/**
	 * If the location contains a sunk ship it should be reported correctly
	 */
	@Test
	public void test_hasSunkShipAt_shouldReturnFalseIfLocationContainsEmptySea()
	{
		// if we create an empty ocean
		ocean = new Ocean();

		// then check a random location to see if it contains a sunk ship
		boolean locationHasASunkShip = ocean.hasSunkShipAt(3, 0);

		// we should expect it not to have one
		assertFalse("empty sea locations do not have sunk ships on them", locationHasASunkShip);

		// if we get another location
		locationHasASunkShip = ocean.hasSunkShipAt(4, 5);

		// we should still expect it not to have one
		assertFalse("empty sea locations do not have sunk ships on them", locationHasASunkShip);

		// and if we pick one more, the outcome should be the same
		locationHasASunkShip = ocean.hasSunkShipAt(5, 7);

		// we should expect them not to have one
		assertFalse("empty sea locations do not have sunk ships on them", locationHasASunkShip);
	}

	/**
	 * If the location is out of range it should not have a sunk ship
	 */
	@Test
	public void test_hasSunkShipAt_shouldReturnFalseIfLocationIsOutOfRange()
	{
		// given in range and out of values
		int inRange = 2;
		int outOfRange = Ocean.OCEAN_WIDTH + 4;

		// if we try and see if a location placed anywhere out of
		// the ocean's borders has a sunk ship, starting with an out of range
		// col
		int row = inRange;
		int column = outOfRange;
		boolean outOfRangeLocationHasASunkShip = ocean.hasSunkShipAt(row, column);

		// we should expect it not to have one
		assertFalse("location with out of range column flagged as having sunk ships on them",
				outOfRangeLocationHasASunkShip);

		// if we pick an out of range row
		row = outOfRange;
		column = inRange;

		// we should still expect the same
		outOfRangeLocationHasASunkShip = ocean.hasSunkShipAt(row, column);
		assertFalse("location with out of range row flagged as having sunk ships on them",
				outOfRangeLocationHasASunkShip);

		// and if they are both out of range
		row = outOfRange;
		column = outOfRange;

		// we should expect the same one more time
		outOfRangeLocationHasASunkShip = ocean.hasSunkShipAt(row, column);
		assertFalse("location with both coordinates out of range as having sunk ships on them",
				outOfRangeLocationHasASunkShip);
	}

	/**
	 * The game is not over if not all ships have been sunk
	 */
	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isGameOver_shouldReturnFalseIfNotAllShipsHaveBeenSunk()
	{
		// if we check the game status before sinking all the ships
		// we should expect the game to be on
		boolean gameIsOver = ocean.isGameOver();
		assertFalse("game was over at game start", gameIsOver);

		// if then we fire shots unitl we sink a number of ships
		// less than the total number of ships
		int numberOfShipsToSink = 5;
		sinkShips(numberOfShipsToSink);

		// and we check the game status again
		gameIsOver = ocean.isGameOver();

		// we should expect it to be still on
		assertFalse("game was akready over without sinking all ships", gameIsOver);
	}

	/**
	 * If all ships have been sunk the game is over
	 */
	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isGameOver_shouldReturnTrueIfAllShipsHaveBeenSunk()
	{
		// if we check the game status before sinking all the ships
		// we should expect the game to be on
		boolean gameIsOver = ocean.isGameOver();

		assertFalse("game was over at game start", gameIsOver);

		// if then we fire shots unitl we sink all the ships
		int numberOfShipsToSink = Ocean.BATTLESHIPS + Ocean.CRUISERS + Ocean.DESTROYERS + Ocean.SUBMARINES;
		sinkShips(numberOfShipsToSink);

		// and we check the game status again
		gameIsOver = ocean.isGameOver();

		// we should expect it to be over
		assertTrue("game was still on after sinking every ship", gameIsOver);
	}

	/**
	 * Asking the ocean for the ship type should return the correct result
	 */
	@Test
	public void test_getShipsTypeAt_shouldReturnTheCorrectShipType()
	{
		// if we have an empty ocean
		ocean = new Ocean();

		// and we get the ship type anywhere at its location
		String emptySeaType = ocean.getShipTypeAt(5, 6);

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
		assertEquals("battleships did not return correct battleship type", Battleship.BATTLESHIP_TYPE, battleshipType);
		assertEquals("cruiser did not return the correct cruiser type", Cruiser.CRUISER_TYPE, cruiserType);
		assertEquals("destroyer did not return the destroyer type", Destroyer.DESTROYER_TYPE, destroyerType);
		assertEquals("submarines did not return the submarines type", Submarine.SUBMARINE_TYPE, submarineType);
	}

	/**
	 * Expect a IAE for out of range coordinates
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_getShipsTypeAt_shouldThrowExceptionIfRowCoordinateisExceedsOceanHeight()
	{
		// if we try and get the ship type at a location with row coordinate
		// greater than the ocean's height
		int outOfRangeRowCoordinate = Ocean.OCEAN_HEIGHT + 2;
		int inRangeColumnCoordinate = 1;

		// we should expect an IAE to be thrown
		ocean.getShipTypeAt(outOfRangeRowCoordinate, inRangeColumnCoordinate);
	}

	/**
	 * Expect a IAE for out of range coordinates
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_getShipsTypeAt_shouldThrowExceptionIfRowCoordinateisNegative()
	{
		// if we try and get the ship type at a location with a negative row
		// coordinate
		int negativeRowCoordinate = -8;
		int inRangeColumnCoordinate = 7;

		// we should expect an IAE to be thrown
		ocean.getShipTypeAt(negativeRowCoordinate, inRangeColumnCoordinate);
	}

	/**
	 * Expect a IAE for out of range coordinates
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_getShipsTypeAt_shouldThrowExceptionIfColumnCoordinateisExceedsOceanWidth()
	{
		// if we try and get the ship type at a location with column coordinate
		// greater than the ocean's width
		int inRangeRowCoordinate = 2;
		int outOfRangeColumnCoordinate = Ocean.OCEAN_WIDTH + 5;

		// we should expect an IAE to be thrown
		ocean.getShipTypeAt(inRangeRowCoordinate, outOfRangeColumnCoordinate);
	}

	/**
	 * Expect a IAE for out of range coordinates
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_getShipsTypeAt_shouldThrowExceptionIfColumnCoordinateisNegative()
	{
		// if we try and get the ship type at a location with a negative column
		// coordinate
		int inRangeRowCoordinate = 5;
		int negativeColumnCoordinate = -7;

		// we should expect an IAE to be thrown
		ocean.getShipTypeAt(inRangeRowCoordinate, negativeColumnCoordinate);
	}

	// ======================= helper methods ======================== //

	/**
	 * Creates a matrix containing the same ships in the original ocean but
	 * rotated 90 degree anticlockwise. The method is useful to reduce branching
	 * when performing operations that are orientation-based
	 *
	 * @return a new matrix rotated 90 degrees anticlockwise
	 */
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

	/**
	 * Gets a count of the toatl area coverd by emtpy sea
	 *
	 * @return count of the toatl area coverd by emtpy sea
	 */
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

	/**
	 * For each ship type it counts the total area covered in the ocean
	 *
	 * @param shipTypeToAreaMapper
	 *            maps a ship type to the area
	 * @param rotate
	 *            whether or not the matrix should be rotated
	 */
	private void countShipAreaByShipTypeHorizontally(HashMap<Class<? extends Ship>, Integer> shipTypeToAreaMapper,
			boolean rotate)
	{
		// get the right matrix depending on orientation
		ships = rotate ? rotatedShips : ships;

		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				// if it's a real horizontal ship count the area occupied
				// by the ship
				if (ships[i][j].isRealShip() && ships[i][j].isHorizontal())
				{
					// get the current ship
					Ship ship = ships[i][j];

					// the bow is already included in the count
					int realShipAreaCounter = 1;

					// increment the total count of the "ship area" and stop
					// when an empty sea is found or the edge of the border is
					// reached
					while (j + realShipAreaCounter < Ocean.OCEAN_WIDTH
							&& ships[i][j + realShipAreaCounter].isRealShip())
					{
						realShipAreaCounter++;
					}

					// update the area for the ship type found
					Class<? extends Ship> shipClass = ship.getClass();
					shipTypeToAreaMapper.put(shipClass, shipTypeToAreaMapper.get(shipClass) + realShipAreaCounter);

					// move past the current ship
					j += ship.getLength() - 1;
				}
			}
		}
	}

	/**
	 * Check if the area around the bow and the stern in diagonal directions is
	 * clear
	 *
	 * @param rotate
	 *            whether or not to rotate the matrix
	 * @return true if there are real ships in the diagonal areas
	 */
	private boolean checkDiagonalAdjacencyMovingHorizontally(boolean rotate)
	{
		boolean adjacent = false;

		// get the right matrix depending on orientation
		ships = rotate ? rotatedShips : ships;

		// keep moving along the ocean row until a horizontal ship bow is found
		for (int i = 0; i < Ocean.OCEAN_HEIGHT; i++)
		{
			for (int j = 0; j < Ocean.OCEAN_WIDTH; j++)
			{
				// if we have found an horizontal ship we will chek if there is
				// anything in the diagonal area
				if (cellHasShip(i, j, ships) && ships[i][j].isHorizontal())
				{
					int sternColumn = j + ships[i][j].getLength() - 1;

					// check the diagonal cells at the bow and the stern
					// location
					adjacent = cellHasShip(i - 1, j - 1, ships) && cellHasShip(i + 1, j - 1, ships)
							&& cellHasShip(i - 1, sternColumn + 1, ships) && cellHasShip(i + 1, sternColumn + 1, ships);

					// move past the current ship
					j = sternColumn;
				}
			}
		}

		return adjacent;
	}

	/**
	 * Checks wether or not the cell at the location indicated by row and column
	 * in the matrix paased as prameter is occupied. Since arrays are passed by
	 * value and not by reference it is not possible to change the ships in the
	 * original ocean and any call to ocean.isOccupied will always operate on
	 * the original matrix.
	 *
	 * @param row
	 *            horizontal coordinate
	 * @param column
	 *            vertical coordinate
	 * @param matrix
	 *            the matrix to scan
	 * @return
	 */
	private boolean cellHasShip(int row, int column, Ship[][] matrix)
	{
		if (row >= 0 && row < Ocean.OCEAN_HEIGHT && column >= 0 && column < Ocean.OCEAN_WIDTH)
		{
			// check only if the coordinates are in range
			return ships[row][column].isRealShip();
		}

		return false;
	}

	/**
	 * Places a ship onto the ocean
	 *
	 * @param shipClass
	 *            type of the shipto be placed
	 * @param bowRow
	 *            horizontal coordinate of the row
	 * @param bowColumn
	 *            vertical coordinate of the row
	 * @param isHorizontal
	 *            whether or not the ship is horizontal
	 * @param ocean
	 *            ocean containing the matrix where the ships will be placed
	 */
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

	/**
	 * Factory mehod to generate ships
	 *
	 * @param shipClass
	 * @return
	 */
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

	/**
	 * Gets a count of the horizontal ships in the matrix passed as parameter
	 *
	 * @param ships
	 *            matrix to be scanned
	 * @return count of horizontal ships in the ocean
	 */
	private int countHorizontalShips(boolean rotate)
	{
		// set the matrix based on orientation
		ships = rotate ? rotatedShips : ships;

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

	/**
	 * Fire shots until the given number of ships is sunk
	 *
	 * @param numberOfShipsToSink
	 */
	private void sinkShips(int numberOfShipsToSink)
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
}
