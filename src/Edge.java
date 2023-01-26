
public class Edge { //class that assists with the orientation of tile connections, for habitat corridors
	private int firstTile;
	private int firstEdge;
	private int secondTile;
	private int secondEdge;
	
	//Note: Edges of the hexagonal are numbered 1 (starting from the top edge, going clockwise) to 6 (left top edge)
	//Total 6 sides
	//If an edge is 0, it means it's not connected to anything currently
	
	public Edge(int firstTile, int firstEdge, int secondTile, int secondEdge) {
		errorCheck(firstTile, firstEdge, secondTile, secondEdge);
		this.firstTile = firstTile;
		this.firstEdge = firstEdge;
		this.secondTile = secondTile;
		this.secondEdge = secondEdge;
	}
	
	public void errorCheck(int firstTile, int firstEdge, int secondTile, int secondEdge) {
		if (firstTile < 0 || secondTile < 0) {
			throw new IllegalArgumentException("One of your tiles is numbered less than 0.");
		}
		else if (firstEdge < 1 || firstEdge > 6 || secondEdge < 1 || secondEdge > 6) {
			throw new IllegalArgumentException("One of your edges is outside the numbered range of 0-5.");
		}
	}
	
	
	

}