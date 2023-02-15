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
		Forest("\033[48;2;84;130;53m"),
		Wetland("\033[48;2;198;224;180m"),
		River("\033[44m"),
		Mountain("\033[47m"),
		Prairie("\033[103m");
		private final String backgroundColour;
		Habitat(String backgroundColour) {
			this.backgroundColour = backgroundColour;
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
	private boolean isFakeTile = false;
    private static int tileCounter = 0;  // counts number of tiles instantiated, used to assign a tileID number, modified in constructor
    private final int tileID;  // identifying number for a tile, used in Edge class
    private final Habitat habitat1;
    private final Habitat habitat2;
	private final ArrayList<Edge> edges;  // stores what the 6 edges of the tile are connected to, if anything

	/**
	 * Generates a habitat tile
	 *
	 * @param habitat1 The first habitat in the tile
	 * @param habitat2 The second habitat in the tile
	 * @param numTokens The number of tokens to place
	 */
	public HabitatTile(Habitat habitat1, Habitat habitat2, int numTokens) {
		this.tileID = tileCounter;
		tileCounter++;
		this.habitat1 = habitat1;
		this.habitat2 = habitat2;
		if (habitat1 == habitat2) {
			keystoneType = TileType.KEYSTONE;
			numTokens = 1;
		}
		else {
			keystoneType = TileType.NON_KEYSTONE;
		}
		tokenOptions = Generation.generateTokenOptionsOnTiles(numTokens);
		edges = Edge.makeEdges(tileID, habitat1, habitat2); //used for tile rotation
	}
	
	public static int getTileCounter() {
		return tileCounter;
	}
	public Habitat getHabitat1() {  // getters and setters
		return habitat1;
	}
	public Habitat getHabitat2() {
		return habitat2;
	}
	public int getTileID() {
		return tileID;
	}
	public TileType getKeystoneType() {
		return keystoneType;
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


	/**
	 * Sets whether the tile is a 'fake' tile (i.e. it is a grey tile that
	 * simply represents the possible places on the map that a tile can be
	 * placed)
	 *
	 * @param isFake whether the tile is 'fake' or not
	 */
	public void setFakeTile(boolean isFake) {
		isFakeTile = isFake;
	}

	public boolean isFakeTile() {
		return isFakeTile;
	}

	@Override
	public String toString() {
		if(keystoneType == TileType.KEYSTONE){
			return habitat1.name() + " Keystone";
		}
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
	public void rotateTile(){
		int input= Input.boundedInt(1,6,"In which position would you like to rotate to (1-6).");

		HabitatTile.Habitat[] temp = new Habitat[6];
		for (int i = 0; i < 6; i++) {
			temp[(i + input) % 6] = edges.get(i).getHabitatType();
		}

		for (int j = 0; j <6; j++) {
			edges.get(j).setHabitatType(temp[j]);
		}

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

		return 	edges.get(5).getHabitatType().backgroundColour + "    |    |" + ANSI_RESET +
				edges.get(0).getHabitatType().backgroundColour +  "    |    " + ANSI_RESET +"\n"+
				edges.get(4).getHabitatType().backgroundColour + "    |" + ANSI_RESET +
				colour[0] + "  " + animal[0] + "  " + ANSI_RESET +
				colour[1] + " " + animal[1] + "  " + ANSI_RESET +
				edges.get(1).getHabitatType().backgroundColour  + "|    " + ANSI_RESET + "\n" +
				edges.get(4).getHabitatType().backgroundColour + "    |" + ANSI_RESET +
				colour[2] + "  " + animal[2] + ANSI_RESET +
				// inserts tile number and adds padding
				colour[3] + "   " +	String.format("%-3s", tileID) + ANSI_RESET +
				edges.get(1).getHabitatType().backgroundColour        + "|    " + ANSI_RESET + "\n" +
				edges.get(3).getHabitatType().backgroundColour + "    |    " + ANSI_RESET +
				edges.get(2).getHabitatType().backgroundColour +  "|    |    " + ANSI_RESET +"\n";
	}
}