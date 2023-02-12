import java.util.ArrayList;
import java.util.Arrays;

public class PlayerMap {
	private static final int BOARD_HEIGHT = 20;
	private static final int BOARD_WIDTH = 20;
	private final ArrayList<HabitatTile> tilesInMap;
	private HabitatTile[][] tileBoardPosition = new HabitatTile[BOARD_HEIGHT][BOARD_WIDTH]; //position of tiles on map
	
	public PlayerMap() { //constructor
		tilesInMap = new ArrayList<>();
		makeStarterTiles();
	}

	public void makeStarterTiles() {
		HabitatTile[] starter = Generation.generateStarterHabitat();
		addTileToMap(starter[0], 8, 9); //places tiles in the middle of the map
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
	 * Used to set a full tile board.
	 * Do not use this method unless you are copying a full tile board into
	 * a new player.
	 * Just use the add tile to map method for adding tiles.
	 *
	 * @param board the board to set the map as
	 * @see PlayerMap#addTileToMap(HabitatTile, int, int)
	 */
	public void setTileBoard(HabitatTile[][] board) {
		tileBoardPosition = board;
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
		if (tile.getIsTokenPlaced()) {
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

	/**
	 * Adds all the possible tiles that can be placed according to the rules of
	 * the game to the map
	 */
	public void addPossibleTiles () {
		HabitatTile[][] tmpBoard = deepCopy(tileBoardPosition); //position of tiles on map
		for (int i = 1; i < BOARD_HEIGHT-1; i++) {
			for (int j = 1; j < BOARD_WIDTH-1; j++) {
				int indent;
				if (i % 2 == 0) {
					indent = 1;
				} else {
					indent = 0;
				}
				if (tmpBoard[i][j] == null & (
						tmpBoard[i][j-1] != null
						|| tmpBoard[i][j+1] != null
						|| tmpBoard[i-1][j] != null
						|| tmpBoard[i+1][j] != null)) {
					HabitatTile tile = new HabitatTile(HabitatTile.Habitat.Prairie, HabitatTile.Habitat.River, 3);
					tile.setFakeTile(true);
					addTileToMap(tile, i, j);
				}
			}
		}
	}
	
	private static HabitatTile[][] deepCopy(HabitatTile[][] original) {
		final HabitatTile[][] result = new HabitatTile[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}

}
