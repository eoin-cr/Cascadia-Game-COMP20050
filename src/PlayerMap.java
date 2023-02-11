
import java.util.ArrayList;

public class PlayerMap {
	private static final int BOARD_HEIGHT = 20;
	private static final int BOARD_WIDTH = 20;
	private ArrayList<HabitatTile> tilesInMap;
	private final HabitatTile[][] tileBoardPosition = new HabitatTile[BOARD_HEIGHT][BOARD_WIDTH]; //position of tiles on map
	
	public PlayerMap() { //constructor
		tilesInMap = new ArrayList<>();
		makeStarterTiles();
	}
	
	public ArrayList<HabitatTile> getTilesInMap() {
		return tilesInMap;
	}
	
	public void makeStarterTiles() {
		HabitatTile[] starter = Generation.generateStarterHabitat();
		addTileToMap(starter[0], 8, 10); //places tiles in the middle of the map
		addTileToMap(starter[1], 9, 9);
		addTileToMap(starter[2], 9, 10);
	}

	/**
	 * Returns the 2d array of habitat tiles, which is used for printing the
	 * map of tiles.
	 *
	 * @return a 2d array of habitat tiles (if a tile has not been placed the
	 * el(String)(String)ement will be null)
	 */
	public HabitatTile[][] getTileBoardPosition() {
		return tileBoardPosition;
	}

	/**
	 * Adds a tile to the board array at position {@code board[x][y]}.
	 *
	 * @param tile the tile to be added to the board position array
	 * @param row the row of the board position array the tile will be added to
	 * @param col the column of the board position array the tile will be added
	 *          to
	 * @throws IllegalArgumentException if there is already a tile at that
	 * position
	 */
	public void addTileToMap(HabitatTile tile, int row, int col) throws IllegalArgumentException {
		if (tileBoardPosition[row][col] != null) {
			throw new IllegalArgumentException("There is already a tile at that position!");
		} //TODO: handle this in the method itself by asking them to place again somewhere else
		tileBoardPosition[row][col] = tile;
		tilesInMap.add(tile);
	}
	
	//replaces token options with placed token, inverts colours, turns boolean to true
	public boolean addTokenToTile(WildlifeToken token, int tileID, Player p) {
		//place it on the correct tile
		boolean placed = false;
		
		for (HabitatTile tile : tilesInMap) {

//			System.out.println(tile.getTileID()); //error checking tileID stuff
//			System.out.println(tileID);
//
//			System.out.println("---");
//			System.out.println(tileID);
//			System.out.println(tile.getTileID());

			if (tile.getTileID() == tileID)	{
				//check if the token type matches options
				placed = checkTokenOptionsMatch(token, tile);
				if (placed) {
					tile.setPlacedToken(token);
					System.out.println("You have successfully placed your token.");
					Display.displayTileMap(p);
					checkIfKeystoneTokenMatch(token, tile, p); //check if player gets a nature token
					break;
				}
			}
		}

		if (!placed) {
			System.out.println("You are trying to add a token to an invalid tile.");
			System.out.println("Please try again.");
		}

		// returns -1 if unsuccessful in placing, otherwise returns 0
		return placed;
	}
	
	private boolean checkTokenOptionsMatch(WildlifeToken token, HabitatTile tile) {
		if (tile.getIsTokenPlaced() == true) {
			System.out.println("There is already a token on this tile.");
			return false;
		}
		
		else {
			for (WildlifeToken w : tile.getTokenOptions()) {
				if (token == w) {
					return true;
				}
			}
			System.out.println("The tile's options for valid tokens do not match.");
			return false;
		}
	}
	
	//check if player gets a nature token once token is placed
	private void checkIfKeystoneTokenMatch(WildlifeToken token, HabitatTile tile, Player p) {
		if (tile.getKeystoneType() == HabitatTile.TileType.KEYSTONE && tile.getTokenOptions()[0] == token) {
			p.addPlayerNatureToken(); //increments player's nature tokens
			System.out.println("Nature token added to "+p.getPlayerName()+". You now have nature tokens: "+p.getPlayerNatureTokens());
		}
	}

}
