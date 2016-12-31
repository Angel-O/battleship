package battleship;

public class MonoPart extends ShipPart
{
	private Bow bowside;

	private Stern sternSide;

	public MonoPart(int row, int column, boolean horizontal)
	{
		super(row, column, horizontal);

		bowside = new Bow(row, column, horizontal);
		sternSide = new Stern(row, column, horizontal);
	}

	@Override
	public boolean isEmptyAroundDetail(Ship[][] ships)
	{
		return bowside.isEmptyAroundDetail(ships) && sternSide.isEmptyAroundDetail(ships);
	}
}
