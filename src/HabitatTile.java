import java.util.ArrayList;
import java.util.Objects;

/**
 * Deals with habitat tiles.
 * Stores information such as the habitats (and their corresponding colour),
 * the tile ID, the edges of the tiles (although managed by the
 * Edge class), and deals with the generation of habitat tiles.
 * @see Edge
 */
public class HabitatTile {
	public static final String ANSI_RESET = "\033[0m";
	public static final String WHITE = "\033[38;5;231m";
	public static final String WHITE_BG = "\033[48;5;231m";
	//private static final int NUMBER_OF_EDGES = 6;
	//TODO: change the edge class with just two instance variables for what the habitat types are

	/**
	 * The habitats that a tile can have.
	 * Stores also the ANSI codes for text and background colours.
	 */
    enum Habitat {
		Forest("\033[38;2;84;130;53m", "\033[48;2;84;130;53m"),
		Wetland("\033[48;2;198;224;180m", "\033[48;2;198;224;180m"),
		River("\033[34m", "\033[44m"),
		Mountain("\033[37m", "\033[47m"),
		Prairie("\033[93m", "\033[103m");
		private final String colour;
		private final String backgroundColour;
		Habitat(String colour, String backgroundColour) {
			this.colour = colour;
			this.backgroundColour = backgroundColour;
		}
		public String getColour() {
			return colour;
		}
		public String getBackgroundColour() {
			return backgroundColour;
		}
	}

	enum TileType {KEYSTONE, NON_KEYSTONE, FAKE}
	protected TileType keystoneType;
	
	protected WildlifeToken[] tokenOptions;
	private boolean isTokenPlaced = false;
	private WildlifeToken placedToken = null;
	private boolean isFakeTile;
    protected static int totalTileCounter = 0;  // used to assign tileID number, skips a bunch of tiles in Generation
    protected static int fakeTileCounter = 0;
    private static int placedTileCounter = 0; // used to exit game, counts tiles placed down on map
    protected int tileID;  // identifying number for a tile, used in Edge class
    private final Habitat habitat1;
    private final Habitat habitat2;
    //private Edge[] edges;  // stores what the 6 edges of the tile are connected to, if anything
	private ArrayList<Edge> edges;  // stores what the 6 edges of the tile are connected to, if anything

	/**
	 * Generates a habitat tile
	 *
	 * @param habitat1 The first habitat in the tile
	 * @param habitat2 The second habitat in the tile
	 * @param numTokens The number of tokens to place
	 */
	public HabitatTile(Habitat habitat1, Habitat habitat2, int numTokens) {
		this.setTileID();
		this.setFakeTile(false);
		this.habitat1 = habitat1;
		this.habitat2 = habitat2;
		this.setKeystoneType();
		this.setTokenOptions(numTokens);
		edges = Edge.makeEdges(tileID, habitat1, habitat2); //used for tile rotation
	}
	
	public static void modifyPlacedTileCounter(int i) {
		placedTileCounter += i;
	}
	public static int getPlacedTileCounter() {
		return placedTileCounter;
	}
	public static int getTileCounter() {
		return totalTileCounter;
	}
	public void setTileID() {
		this.tileID = totalTileCounter;
		totalTileCounter++;
	}
	public int getTileID() {
		return tileID;
	}
	public Habitat getHabitat1() {  // getters and setters
		return habitat1;
	}
	public Habitat getHabitat2() {
		return habitat2;
	}
	public void setKeystoneType() {
		if (habitat1 == habitat2) {
			this.keystoneType = TileType.KEYSTONE;
		}
		else {
			this.keystoneType = TileType.NON_KEYSTONE;
		}
	}
	public TileType getKeystoneType() {
		return keystoneType;
	}
	public void setTokenOptions(int numTokens) {
		if (this.getKeystoneType() == TileType.KEYSTONE) {
			numTokens = 1;
		}
		this.tokenOptions = Generation.generateTokenOptionsOnTiles(numTokens);
	}
	public WildlifeToken[] getTokenOptions() {
		return tokenOptions;
	}
	public boolean getIsTokenPlaced() {
		return isTokenPlaced;
	}
	public void setPlacedToken(WildlifeToken placedAnimal) {
		this.placedToken = placedAnimal;
		this.isTokenPlaced = true;
	}
	public WildlifeToken removePlacedToken() { //to be used if you spend a nature token to move an animal token
		if (!this.isTokenPlaced) {
			System.out.println("There is no token on this tile to remove. Please try a different tile.");
			return null;
		}
		else {
			WildlifeToken freed = this.placedToken;
			this.placedToken = null;
			this.isTokenPlaced = false;
			return freed;
		}
	}
	
	/**
	 * Sets whether the tile is a 'fake' tile (i.e. it is a grey tile that
	 * simply represents the possible places on the map that a tile can be
	 * placed)
	 *
	 * @param isFake whether the tile is 'fake' or not
	 */
	public void setFakeTile(boolean isFake) {
		if (isFake == true) {
			fakeTileCounter++;
		}
		isFakeTile = isFake;
	}

	public boolean isFakeTile() {
		return isFakeTile;
	}

	@Override
	public String toString() {
		return habitat1.name() + " + " + habitat2.name();
	}

	/**
	 * Overrides equals method.
	 * Returns true if two tiles have the same tile ID.
	 *
	 * @param o the reference object to which to compare
	 * @return true if both tiles have the same tile ID.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HabitatTile tile = (HabitatTile) o;
		return tileID == tile.tileID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tileID);
	}

	/**
	 * Returns a formatted string version of the habitat tile, so that it can
	 * be altered easily before printing.
	 *
	 * @return a string with ANSI colours.
	 */	
	public String toFormattedString() {
		if (isFakeTile) {
			final String GREY = "\033[37m";
			return GREY + "|||| |||| |||| ||||" + ANSI_RESET + "\n"
					+ GREY + "||||  " + String.format("%-3s", tileID) + "      ||||" + ANSI_RESET + "\n"
					+ GREY + "||||           ||||" + ANSI_RESET + "\n"
					+ GREY + "|||| |||| |||| ||||" + ANSI_RESET + "\n";
		}
		String first = habitat1.getBackgroundColour();
		String second = habitat2.getBackgroundColour();
		char[] animal = new char[3];
		String[] colour = new String[4];

		if (!isTokenPlaced) {
			for (int i = 0; i < tokenOptions.length; i++) {
				if (tokenOptions[i] != null) {
					animal[i] = tokenOptions[i].toChar();
					colour[i] = tokenOptions[i].getColour() + WHITE_BG;
				}
				else {
					animal[i] = ' ';
					colour[i] = "" + WHITE_BG;
				}
			}
			colour[3] = WHITE_BG;

		} else {
			animal = new char[]{placedToken.toChar(), ' ', ' ', ' '};
			colour = new String[]{placedToken.getBackgroundColour() + WHITE, WHITE, WHITE, WHITE, WHITE};
		}

		String full =  "    |    |    |    " + ANSI_RESET + "\n";
		return first + full +
					first + "    |" + ANSI_RESET +
					colour[0] + "  " + animal[0] + "  " + ANSI_RESET +
					colour[1] + " " + animal[1] + "  " + ANSI_RESET +
					first  + "|    " + ANSI_RESET + "\n" +
					second + "    |" + ANSI_RESET +
					colour[2] + "  " + animal[2] + ANSI_RESET +
				// inserts tile number and adds padding
					colour[3] + "   " +	String.format("%-3s", tileID) + ANSI_RESET +
					second  + "|    " + ANSI_RESET + "\n" +
					second + full;
	}
}