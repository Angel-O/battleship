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
 * Test class to test the API of the {@linkplain Ship} class.
 *
 * @author Angelo Oparah
 *
 */
public class ShipTest
{
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

	// ======================= constructor tests ======================== //

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test_Ship_shouldThrowExceptionIfLengthIsNegative()
	{
		// if we create a new ship with a negative length
		// we should expect an IAE to be thrown
		int invalidLength = -2;
		Ship ship = new MockShip(invalidLength);
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test_Ship_shouldThrowExceptionIfLengthIsZero()
	{
		// if we create a new ship with length = 0
		// we should expect an IAE to be thrown
		int invalidLength = 0;
		Ship ship = new MockShip(invalidLength);
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test_Ship_shouldThrowExceptionIfLengthExceedsOceanHeight()
	{
		// if we create a new ship with length greater than the Ocean's height
		// we should expect an IAE to be thrown
		int invalidLength = Ocean.OCEAN_HEIGHT + 1;
		Ship ship = new MockShip(invalidLength);
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void test_Ship_shouldThrowExceptionIfLengthExceedsOceanWidth()
	{
		// if we create a new ship with length greater than the Ocean's wdth
		// we should expect an IAE to be thrown
		int invalidLength = Ocean.OCEAN_WIDTH + 1;
		Ship ship = new MockShip(invalidLength);
	}

	// =================== getters and setters tests ==================== //

	public void test_getLength_shouldReturnCorrectShipLength()
	{
		// for each ship type we have
		Ship emptySea = new EmptySea();
		int expectedEmptySeaLength = EmptySea.EMPTY_SEA_LENGTH;
		int actualEmptySeaLength = emptySea.getLength();

		Ship battleship = new Battleship();
		int expectedEBattleshipLength = Battleship.BATTLESHIP_LENGTH;
		int actualBattleshipLength = battleship.getLength();

		Ship cruiser = new Cruiser();
		int expectedCruiserLength = Cruiser.CRUISER_LENGTH;
		int actualCruiserLength = cruiser.getLength();

		Ship destroyer = new Destroyer();
		int expectedDestroyerLength = Destroyer.DESTROYER_LENGTH;
		int actualDestroyerLength = destroyer.getLength();

		Ship submarine = new Submarine();
		int expectedSubmarineLength = Submarine.SUBMARINE_LENGTH;
		int actualSubmarineLength = submarine.getLength();

		// we should expect the correct length when calling the getLength method
		assertEquals("returned incorrect empty sea length", expectedEmptySeaLength, actualEmptySeaLength);
		assertEquals("returned incorrect battleship length", expectedEBattleshipLength, actualBattleshipLength);
		assertEquals("returned incorrect cruiser length", expectedCruiserLength, actualCruiserLength);
		assertEquals("returned incorrect destroyer length", expectedDestroyerLength, actualDestroyerLength);
		assertEquals("returned incorrect submarine length", expectedSubmarineLength, actualSubmarineLength);
	}

	@Test
	public void test_getShipType_shouldReturnCorrectShipType()
	{
		// for each ship type we have
		Ship emptySea = new EmptySea();
		String expectedEmptySeaType = EmptySea.EMPTY_SEA_TYPE;
		String actualEmptySeaType = emptySea.getShipType();

		Ship battleship = new Battleship();
		String expectedEBattleshipType = Battleship.BATTLESHIP_TYPE;
		String actualBattleshipType = battleship.getShipType();

		Ship cruiser = new Cruiser();
		String expectedCruiserType = Cruiser.CRUISER_TYPE;
		String actualCruiserType = cruiser.getShipType();

		Ship destroyer = new Destroyer();
		String expectedDestroyerType = Destroyer.DESTROYER_TYPE;
		String actualDestroyerType = destroyer.getShipType();

		Ship submarine = new Submarine();
		String expectedSubmarineType = Submarine.SUBMARINE_TYPE;
		String actualSubmarineType = submarine.getShipType();

		// we should expect to get the correct ship type when calling the get
		// ship type method
		assertEquals("returned incorrect type for empty sea", expectedEmptySeaType, actualEmptySeaType);
		assertEquals("returned incorrect type for battleship", expectedEBattleshipType, actualBattleshipType);
		assertEquals("returned incorrect type for cruiser", expectedCruiserType, actualCruiserType);
		assertEquals("returned incorrect type for destroyer", expectedDestroyerType, actualDestroyerType);
		assertEquals("returned incorrect type for submarine", expectedSubmarineType, actualSubmarineType);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setBowRow_shouldThrowAnExceptionForNegativeValues()
	{
		Ship ship = new Battleship();

		// a negative value should cause an IAE exception
		ship.setBowRow(-99);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setBowRow_shouldThrowAnExceptionForValueGreaterThanOceanHeight()
	{
		Ship ship = new Destroyer();

		// a value greater than the ocean's height should cause an IAE exception
		ship.setBowRow(Ocean.OCEAN_HEIGHT + 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setBowColumn_shouldThrowAnExceptionForNegativeValue()
	{
		Ship ship = new Cruiser();

		// a negative value should cause an IAE exception
		ship.setBowColumn(-45);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setBowColumn_shouldThrowAnExceptionForValueGreaterThanOceanWidth()
	{
		Ship ship = new Submarine();

		// a value greater than the ocean's width should cause an IAE exception
		ship.setBowColumn(Ocean.OCEAN_WIDTH + 1);
	}

	@Test
	public void test_isHorizontal_shouldReturnCorrectShipOrientation()
	{
		// if we create a new ship
		Ship ship = new Battleship();

		// we should expect its orientation to be set to the default value
		boolean expectedDefaultOrientation = ship.isHorizontal();

		assertFalse("incorect default vertical ship orientation", expectedDefaultOrientation);

		// if we set it to true
		ship.setHorizontal(true);

		// we should expect its value to change
		boolean expectedOrientation = ship.isHorizontal();
		assertTrue("incorrect ship orientation returned", expectedOrientation);
	}

	@Test
	public void test_setHorizontal_shouldNotAlterTheDefaultValueWhenSetToFalse()
	{
		// if we create a new ship
		Ship ship = new Battleship();

		// its orientation should to be set to the default value
		boolean defaultOrientation = ship.isHorizontal();

		// so if we set it to false
		ship.setHorizontal(false);

		// we should expect its value to remain unchanged
		boolean orientation = ship.isHorizontal();
		assertEquals("ship orientation has changed", defaultOrientation, orientation);
	}

	@Test
	public void test_getBowRow_shouldReturnTheCorrectValue()
	{
		// given a ship
		Ship ship = new Cruiser();

		// whatever value we set the horizontal coordinate of the bow to
		// within the Ocean's range
		int bowRow = 5;
		ship.setBowRow(bowRow);

		// we should get that value back
		int actualBowRowCoordinate = ship.getBowRow();

		assertEquals("bow row  did not have value originally set", bowRow, actualBowRowCoordinate);
	}

	@Test
	public void test_getBowColumn_shouldReturnTheCorrectValue()
	{
		// given a ship
		Ship ship = new Submarine();

		// whatever value we set the vertical coordinate of the bow to
		// within the Ocean's range
		int bowColumn = 3;
		ship.setBowColumn(bowColumn);

		// we should get that value back
		int actualBowRowCoordinate = ship.getBowColumn();

		assertEquals("bow column did not have value originally set", bowColumn, actualBowRowCoordinate);
	}

	// ====================== public methods tests ====================== //

	@Test
	public void test_isRealShip_shouldReturnFalseForAnEmptySea()
	{
		// if we create a new empty sea ship
		Ship ship = new EmptySea();

		// we should expect it not to be flagged as a real ship
		boolean isEmptySeaReal = ship.isRealShip();

		assertFalse("empty sea ship flagged as a real ship", isEmptySeaReal);
	}

	@Test
	public void test_isRealShip_shouldReturnTrueForRealShips()
	{
		// for every real ship we have
		Ship battleship = new Battleship();
		boolean isBattleshipReal = battleship.isRealShip();

		Ship cruiser = new Cruiser();
		boolean isCruiserReal = cruiser.isRealShip();

		Ship destroyer = new Destroyer();
		boolean isDestroyerReal = destroyer.isRealShip();

		Ship submarine = new Submarine();
		boolean isSubmarinReal = submarine.isRealShip();

		// we should expect them to be flagged as actual ships
		assertTrue("battleships not flagged as real ship", isBattleshipReal);
		assertTrue("cruisers not flagged as real ship", isCruiserReal);
		assertTrue("destroyers not flagged as real ship", isDestroyerReal);
		assertTrue("submarines not flagged as real ship", isSubmarinReal);
	}

	public void test_isSunk_shipsShouldSinkWhenShotsAreFiredAcrossTheirLength()
	{
		// if we have any type of ship
		Ship battleship = new Battleship();
		Ship destroyer = new Destroyer();
		Ship cruiser = new Cruiser();
		Ship submarine = new Submarine();

		// and shoot each part along the length of the ship
		fireShotsAcrossShipLength(battleship, Battleship.BATTLESHIP_LENGTH);
		fireShotsAcrossShipLength(destroyer, Destroyer.DESTROYER_LENGTH);
		fireShotsAcrossShipLength(cruiser, Cruiser.CRUISER_LENGTH);
		fireShotsAcrossShipLength(submarine, Submarine.SUBMARINE_LENGTH);

		boolean expectedBattleshipState = battleship.isSunk();
		boolean expectedDestroyerState = destroyer.isSunk();
		boolean expectedCruiserState = cruiser.isSunk();
		boolean expectedSubmarineState = submarine.isSunk();

		// we should expect them to sink
		assertTrue("shooting a battleship until it sinks failed to sink it", expectedBattleshipState);
		assertTrue("shooting a destroyer until it sinks failed to sink it", expectedDestroyerState);
		assertTrue("shooting a cruiser until it sinks failed to sink it", expectedCruiserState);
		// submarines have length equal to 1, so we only need a shot to sink one
		assertTrue("shooting a submarine failed to sink it", expectedSubmarineState);
	}

	public void test_isSunk_shipsShouldNotSinkIfNotAllPartsAreHit()
	{
		// if we have any type of ship
		Ship battleship = new Battleship();
		Ship destroyer = new Destroyer();
		Ship cruiser = new Cruiser();
		Ship submarine = new Submarine();

		// with any orientation
		battleship.setHorizontal(true);
		submarine.setHorizontal(true);

		// and shoot along the length of the ship without hitting all parts
		fireShotsAcrossShipLength(battleship, Battleship.BATTLESHIP_LENGTH - 1);
		fireShotsAcrossShipLength(destroyer, Destroyer.DESTROYER_LENGTH - 1);
		fireShotsAcrossShipLength(cruiser, Cruiser.CRUISER_LENGTH - 1);
		fireShotsAcrossShipLength(submarine, Submarine.SUBMARINE_LENGTH - 1);

		boolean expectedBattleshipState = battleship.isSunk();
		boolean expectedDestroyerState = destroyer.isSunk();
		boolean expectedCruiserState = cruiser.isSunk();
		boolean expectedSubmarineState = submarine.isSunk();

		// we should not expect them to sink
		assertFalse("shooting a battleship without sinking it did actually sink it", expectedBattleshipState);
		assertFalse("shooting a destroyer without sinking it did actually sink it", expectedDestroyerState);
		assertFalse("shooting a cruiser without sinking it did actually sink it", expectedCruiserState);
		// submarines have length equal to 1, so we aren't actually hitting them
		assertFalse("submarine was missed", expectedSubmarineState);
	}

	@Test
	public void test_toString_shouldReturnCorrectRepresentationForEmptySea()
	{
		// if we have a brand new empty sea with bow coordinates set to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new EmptySea();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);

		// we should expect it to be represented as a "." (dot)
		String expectedRepresentation = ".";
		String actualRepresentation = ship.toString();
		assertEquals("representation of non hit sea spot was incorrect", expectedRepresentation, actualRepresentation);

		// if then we shoot at it
		ship.shootAt(bowRow, bowColumn);

		// we should expect its representation to change to "-" (dash)
		expectedRepresentation = "-";
		actualRepresentation = ship.toString();
		assertEquals("representation of hit sea spot was incorrect", expectedRepresentation, actualRepresentation);

	}

	@Test
	public void test_toString_shouldReturnCorrectRepresentationForRealShip()
	{
		// if we have a brand new real horizontal battleship with bow
		// coordinates set to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Battleship();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);
		ship.setHorizontal(true);

		// we should expect its represenatation to be equal to this
		String expectedRepresentation = "....";
		String actualRepresentation = ship.toString();
		assertEquals("representation of real ship yet to be hit once was incorrect", expectedRepresentation,
				actualRepresentation);

		// if then we shoot at it one shot at a time we should expect its
		// representation to get an "S" for each part hit.
		// So if we shoot at the first part
		ship.shootAt(bowRow, bowColumn);

		// we should expect its representation to change to this
		expectedRepresentation = "S...";
		actualRepresentation = ship.toString();
		assertEquals("representation of a real ship hit on one part was incorrect", expectedRepresentation,
				actualRepresentation);

		// and if we shoot at the second part
		ship.shootAt(bowRow, bowColumn + 1);

		// we should expect its representation to change to this
		expectedRepresentation = "SS..";
		actualRepresentation = ship.toString();
		assertEquals("representation of a real ship hit on two parts was incorrect", expectedRepresentation,
				actualRepresentation);

		// then if we shoot at the third part
		ship.shootAt(bowRow, bowColumn + 2);

		// we should expect its representation to change to this
		expectedRepresentation = "SSS.";
		actualRepresentation = ship.toString();
		assertEquals("representation of a real ship hit on three parts was incorrect", expectedRepresentation,
				actualRepresentation);

		// lastly if we shoot at the fourth part
		ship.shootAt(bowRow, bowColumn + 3);

		// we should expect its representation to change to this
		expectedRepresentation = "SSSS";
		actualRepresentation = ship.toString();
		assertEquals("representation of a real ship hit on every part was incorrect", expectedRepresentation,
				actualRepresentation);
	}

	@Test
	public void test_shootAt_onlyShipPartsThatWereShotShouldBeMarkedInHitArray()
	{
		// if we have a brand new real horizontal ship with bow coordinates set
		// to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Destroyer();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);
		ship.setHorizontal(true);

		// and we shoot at it on its last part
		ship.shootAt(bowRow, bowColumn + Destroyer.DESTROYER_LENGTH - 1);

		// if then we get the state of the ship we should expect only the last
		// part of it to be equal to "S"
		String shipState = ship.toString();
		int lastPartIndex = Destroyer.DESTROYER_LENGTH - 1;
		int actualLastPartShipState = shipState.indexOf('S');

		assertEquals("part of the ship that was shot wasn't marked as hit", lastPartIndex, actualLastPartShipState);
	}

	@Test
	public void test_shootAt_shouldReturnTrueEachTimeRealShipStillAfloatGetsShot()
	{
		// if we have a brand new real vertical ship with bow coordinates set to
		// (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Cruiser();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);

		// and we shoot at it on its first part for instance
		ship.shootAt(bowRow, bowColumn);

		// any subsequent shot on the same part should be still succesful
		boolean expectedOutcome = ship.shootAt(bowRow, bowColumn);
		assertTrue("repeatedly shooting afloat ship on the same part was marked as a miss", expectedOutcome);
	}

	@Test
	public void test_shootAt_shouldReturnFalseEachTimeRealShipIsShotAfterBeingSunk()
	{
		// if we have a brand new real vertical ship with bow coordinates set to
		// (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Cruiser();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);

		// and we shoot at it until it sinks
		fireShotsAcrossShipLength(ship, Cruiser.CRUISER_LENGTH);

		// any subsequent shot on the same part should be unsuccesful
		boolean expectedOutcome = ship.shootAt(bowRow, bowColumn);
		assertFalse("repeatedly shooting sunk ship on the same part was marked as a hit", expectedOutcome);
	}

	@Test
	public void test_shootAt_shouldReturnFalseWheneverEmptySeaIsHit()
	{
		// if we have an empty sea with bow coordinates set to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new EmptySea();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);

		// and we shoot at it repeatedly on the same (unique in this case) spot
		ship.shootAt(bowRow, bowColumn);
		ship.shootAt(bowRow, bowColumn);

		// any subsequent shot on the same part should be unsuccesful
		boolean expectedOutcome = ship.shootAt(bowRow, bowColumn);
		assertFalse("repeatedly shooting empty sea was marked as a hit", expectedOutcome);
	}

	@Test
	public void test_shootAt_shouldReturnFalseIfTheCoordinatesGivenAreNotTheCoordinatesTheShipWasPlacedAt()
	{
		// if we have a ship with bow coordinates set to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Submarine();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);

		// and we shoot at it with coordinates that surely are not its
		// coordinates
		int wrongRowCoordinate = bowRow + Submarine.SUBMARINE_LENGTH;
		int wrongColumnCoordinate = bowColumn + Submarine.SUBMARINE_LENGTH;

		// we should expect the shot to be unsuccessful
		boolean expectedOutcome = ship.shootAt(wrongRowCoordinate, bowColumn);
		assertFalse("shooting at ship with wrong coordinates was succesful", expectedOutcome);

		// we should expect the shot to be unsuccessful
		expectedOutcome = ship.shootAt(bowRow, wrongColumnCoordinate);
		assertFalse("shooting at ship with wrong coordinates was succesful", expectedOutcome);

		// we should expect the shot to be unsuccessful
		wrongRowCoordinate = bowRow - 1;
		expectedOutcome = ship.shootAt(wrongRowCoordinate, bowColumn);
		assertFalse("shooting at ship with wrong coordinates was succesful", expectedOutcome);

		// we should expect the shot to be unsuccessful
		wrongColumnCoordinate = bowColumn - 1;
		expectedOutcome = ship.shootAt(bowRow, wrongColumnCoordinate);
		assertFalse("shooting at ship with wrong coordinates was succesful", expectedOutcome);
	}

	// ========================== mock objects ========================= //

	/**
	 * Mock class to emulate the behaviour of a {@linkplain Ship}. In particular
	 * it allows to pass a length parameter to the constuctor without using a
	 * default hardcoded one.
	 *
	 * @author Angelo Oparah
	 *
	 */
	private class MockShip extends Ship
	{
		/**
		 * Builds a mock ship class of the specified length.
		 *
		 * @param length
		 *            length of the ship.
		 */
		public MockShip(int length)
		{
			super(length);
		}

		@Override
		public String getShipType()
		{
			return "mock ship";
		}
	}

	// ========================= helper methods ======================== //

	/**
	 * Helper method to shoot at a ship across its length, in a sequential way.
	 * No shots will be fired twice at the same location.
	 *
	 * @param ship
	 *            target to shoot at
	 * @param shots
	 *            number of shots to be fired in sequence from the bow
	 *            coordinates of the ship
	 */
	private void fireShotsAcrossShipLength(Ship ship, int shots)
	{
		// get ship orientation
		boolean horizontal = ship.isHorizontal();

		int start = horizontal ? ship.getBowColumn() : ship.getBowRow();

		for (int i = start; i < start + shots; i++)
		{
			// if the ship is horizontal increment the column, otherwise
			// increment the row
			ship.shootAt(horizontal ? ship.getBowRow() : i, horizontal ? i : ship.getBowColumn());
		}
	}

}
