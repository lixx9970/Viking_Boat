package comp1110.ass1;

/**
 * This class represents a tile on the Vikings game board.
 */
public class Tile {
    public static int NUM_POSITIONS = 9;

    /**
     * The type of the tile (N or O)
     */
    private final TileType tileType;

    /**
     * The position of the tile on the board
     */
    private final int position;

    /**
     * The tile's current orientation (0 - 3)
     */
    private int orientation;

    public Tile(TileType tileType, int orientation, int position) {
        this.tileType = tileType;
        this.orientation = orientation;
        this.position = position;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Decode a tile String to create a tile.
     * For example:: Tile("N1", 4)
     * would return a Tile with tileType N, orientation 1 and position 4.
     *
     * @param tileString two-character string representing a tile and orientation eg: N0
     * @param position   position of the tile on the board
     * @return a Tile instance with Tile type, orientation and position as specified.
     */
    public static Tile fromString(String tileString, int position) {
        char s1 = tileString.charAt(0);
        int orientation = Character.getNumericValue(tileString.charAt(1));
        TileType type;
        if (s1 == 'N') {
            type = TileType.N;
        } else type = TileType.O;
        return new Tile(type, orientation, position);
    }

    /**
     * @param boardString a well-formed board string representing a current game state
     * @return an array of tiles as specified in the board string
     */
    public static Tile[] fromBoardString(String boardString) {
        Tile[] tiles = new Tile[NUM_POSITIONS];
        for (int i = 0; i < NUM_POSITIONS; i++) {
            tiles[i] = fromString(boardString.substring(i * 2, i * 2 + 2), i);
        }
        return tiles;
    }

    @Override
    public String toString() {
        return "Tile " + tileType + " position " + position + " orientation " + orientation;
    }
}