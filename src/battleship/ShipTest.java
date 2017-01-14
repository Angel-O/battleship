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

	@Test(timeout = DEFAULT_TIMEOUT)
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

		// we should expect the correct length when calling the get length
		// method
		assertEquals("verifying empty sea length", expectedEmptySeaLength, actualEmptySeaLength);
		assertEquals("verifying battleship type", expectedEBattleshipLength, actualBattleshipLength);
		assertEquals("verifying cruiser type", expectedCruiserLength, actualCruiserLength);
		assertEquals("verifying destroyer type", expectedDestroyerLength, actualDestroyerLength);
		assertEquals("verifying submarine type", expectedSubmarineLength, actualSubmarineLength);
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
		assertEquals("verifying empty sea type", expectedEmptySeaType, actualEmptySeaType);
		assertEquals("verifying battleship type", expectedEBattleshipType, actualBattleshipType);
		assertEquals("verifying cruiser type", expectedCruiserType, actualCruiserType);
		assertEquals("verifying destroyer type", expectedDestroyerType, actualDestroyerType);
		assertEquals("verifying submarine type", expectedSubmarineType, actualSubmarineType);
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
		ship.setBowRow(-45);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setBowColumn_shouldThrowAnExceptionForValueGreaterThanOceanWidth()
	{
		Ship ship = new Submarine();

		// a value greater than the ocean's width should cause an IAE exception
		ship.setBowRow(Ocean.OCEAN_WIDTH + 1);
	}

	@Test
	public void test_isHorizontal_shouldReturnCorrectShipOrientation()
	{
		// if we create a new ship
		Ship ship = new Battleship();

		// we should expect its orientation to be set to the default value
		boolean expectedDefaultOrientation = ship.isHorizontal();

		assertFalse("verifying default vertical ship orientation", expectedDefaultOrientation);

		// if we set it to true
		ship.setHorizontal(true);

		// we should expect its value to change
		boolean expectedOrientation = ship.isHorizontal();
		assertTrue("verifiying horizontal ship orientation", expectedOrientation);
	}

	@Test
	public void test_setHorizontal_shouldNotAlterTheDefaultValue()
	{
		// if we create a new ship
		Ship ship = new Battleship();

		// we should expect its orientation to be set to the default value
		boolean expectedOrientation = ship.isHorizontal();

		// if we set it to false
		ship.setHorizontal(false);

		// we should expect its value to remain unchanged
		assertFalse("verifiying ship orientation gets changed", expectedOrientation);
	}

	@Test
	public void test_getBowRow_shouldReturnTheCorrectValue()
	{
		// given a ship
		Ship ship = new Cruiser();

		// whatever value we set the vertical coordinate of the bow to
		// within the Ocean's range
		ship.setBowRow(2);

		// we should get that value back
		int actualBowRowCoordinate = ship.getBowRow();

		assertEquals("bow row returned is what was originally set", 2, actualBowRowCoordinate);
	}

	@Test
	public void test_getBowColumn_shouldReturnTheCorrectValue()
	{
		// given a ship
		Ship ship = new Submarine();

		// whatever value we set the horizontal coordinate of the bow to
		// within the Ocean's range
		ship.setBowColumn(5);

		// we should get that value back
		int actualBowRowCoordinate = ship.getBowColumn();

		assertEquals("bow column returned is what was originally set", 5, actualBowRowCoordinate);
	}

	// ====================== public methods tests ====================== //

	@Test
	public void test_isRealShip_shouldReturnFalseForAnEmptySea()
	{
		// if we create a new empty sea ship
		Ship ship = new EmptySea();

		// we should expect its orientation to be set to the default value
		boolean expectedShipType = ship.isRealShip();

		assertFalse("verifying that empty sea ships are not a real ship", expectedShipType);
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
		assertTrue("verifying that battleships are indeed real ship", isBattleshipReal);
		assertTrue("verifying that cruisers are indeed real ship", isCruiserReal);
		assertTrue("verifying that destroyers are indeed real ship", isDestroyerReal);
		assertTrue("verifying that submarines are indeed real ship", isSubmarinReal);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
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

		// verify that the ships have been actually sunk
		assertTrue("shooting a battleship until it sinks", expectedBattleshipState);
		assertTrue("shooting a destroyer until it sinks", expectedDestroyerState);
		assertTrue("shooting a cruiser until it sinks", expectedCruiserState);
		assertTrue("shooting a submarine will sink it", expectedSubmarineState);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void test_isSunk_shipsShouldNotSinkIfNotAllPartsAreHit()
	{
		// if we have any type of ship
		Ship battleship = new Battleship();
		Ship destroyer = new Destroyer();
		Ship cruiser = new Cruiser();
		Ship submarine = new Submarine();

		// and shoot along the length of the ship without hitting all parts
		fireShotsAcrossShipLength(battleship, Battleship.BATTLESHIP_LENGTH - 1);
		fireShotsAcrossShipLength(destroyer, Destroyer.DESTROYER_LENGTH - 1);
		fireShotsAcrossShipLength(cruiser, Cruiser.CRUISER_LENGTH - 1);
		fireShotsAcrossShipLength(submarine, Submarine.SUBMARINE_LENGTH - 1);

		boolean expectedBattleshipState = battleship.isSunk();
		boolean expectedDestroyerState = destroyer.isSunk();
		boolean expectedCruiserState = cruiser.isSunk();
		boolean expectedSubmarineState = submarine.isSunk();

		// verify that the ships have not been sunk
		assertFalse("shooting a battleship without sinking it", expectedBattleshipState);
		assertFalse("shooting a destroyer without sinking it", expectedDestroyerState);
		assertFalse("shooting a cruiser without sinking it", expectedCruiserState);
		assertFalse("not shooting at submarine will not sink it", expectedSubmarineState);
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
		assertEquals("verifying representation of non hit sea spot", expectedRepresentation, actualRepresentation);

		// if then we shoot at it
		ship.shootAt(bowRow, bowColumn);

		// we should expect its representation to change to "-" (dash)
		expectedRepresentation = "-";
		actualRepresentation = ship.toString();
		assertEquals("verifying representation of hit sea spot", expectedRepresentation, actualRepresentation);

	}

	@Test
	public void test_toString_shouldReturnCorrectRepresentationForRealShip()
	{
		// if we have a brand new real horizontal ship with bow coordinates set
		// to (0,0)
		int bowRow = 0;
		int bowColumn = 0;
		Ship ship = new Battleship();
		ship.setBowRow(bowRow);
		ship.setBowColumn(bowColumn);
		ship.setHorizontal(true);

		// we should expect its represenatation to be equal to this
		String expectedRepresentation = "....";
		String actualRepresentation = ship.toString();
		assertEquals("verifying representation of real ship yet to be hit once", expectedRepresentation,
				actualRepresentation);

		// if then we shoot at it one shot at a time we should expect its
		// representation to get an "S" for each part hit.
		// So if we shoot at the first part
		ship.shootAt(bowRow, bowColumn);

		// we should expect its representation to change to this
		expectedRepresentation = "S...";
		actualRepresentation = ship.toString();
		assertEquals("verifying representation of a real ship hit on one part", expectedRepresentation,
				actualRepresentation);

		// and if we shoot at the second part
		ship.shootAt(bowRow, bowColumn + 1);

		// we should expect its representation to change to this
		expectedRepresentation = "SS..";
		actualRepresentation = ship.toString();
		assertEquals("verifying representation of a real ship hit on two parts", expectedRepresentation,
				actualRepresentation);

		// then if we shoot at the third part
		ship.shootAt(bowRow, bowColumn + 2);

		// we should expect its representation to change to this
		expectedRepresentation = "SSS.";
		actualRepresentation = ship.toString();
		assertEquals("verifying representation of a real ship hit on two parts", expectedRepresentation,
				actualRepresentation);

		// lastly if we shoot at the fourth part
		ship.shootAt(bowRow, bowColumn + 3);

		// we should expect its representation to change to this
		expectedRepresentation = "SSSS";
		actualRepresentation = ship.toString();
		assertEquals("verifying representation of a real ship hit on every part", expectedRepresentation,
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

		assertEquals("last part of the ship gets marked as hit", lastPartIndex, actualLastPartShipState);
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
		assertTrue("repeatedly shooting afloat ship on the same part", expectedOutcome);
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
		assertFalse("repeatedly shooting sunk ship on the same part", expectedOutcome);
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
		assertFalse("repeatedly shooting sunk ship on the same part", expectedOutcome);
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
		boolean expectedOutcome = ship.shootAt(wrongRowCoordinate, wrongColumnCoordinate);
		assertFalse("shooting at ship with wrong coordinates", expectedOutcome);
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
