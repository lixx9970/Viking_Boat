package comp1110.ass1;

import javafx.scene.shape.StrokeLineCap;

/**
 * Edge locations on the board are represented in a cartesian coordinate system
 * (x,y) where both coordinates range from 0 to 6. Not every location in this
 * system is an allowable location for an edge, for example (1,1) is the centre
 * of tile 0, and (6,2) is at a 'corner' between tile positions 2 and 5.
 * The coordinates of the allowable edge locations are shown below.
 * The numbers "0", "1"... in quotation marks in the middle represent the tile
 * positions 0-8. These are listed for diagram purposes only (they are not used
 * in the location class).
 * <p>
 * +------(1,0)----+----(3,0)----+----(5,0)----+
 * (0,1)   "0"   (2,1)   "1"   (4,1)   "2"   (6,1)
 * +------(1,2)----+----(3,2)----+----(5,2)----+
 * (0,3)   "3"   (2,3)   "4"   (4,3)   "5"   (6,3)
 * +------(1,4)----+----(3,4)----+----(5,4)----+
 * (0,5)   "6"   (2,5)   "7"   (4,5)   "8"   (6,5)
 * +------(1,6)----+----(3,6)----+----(5,6)----+
 */
public class Location {
    static final int INVALID = -1;
    private final int x;
    private final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @param edge a character representing an edge
     * @return the location of the given edge
     */
    public static Location fromEdge(char edge) {

        //use structure of switch to determine the location coordinates of corresponding edge
        switch (edge){
            case 'a':
                return new Location (1,0);
            case 'b':
                return new Location (3,0);
            case 'c':
                return new Location (5,0);
            case 'd':
                return new Location (0,1);
            case 'e':
                return new Location(2,1);
            case 'f':
                return new Location(4,1);
            case 'g':
                return new Location(6,1);
            case 'h':
                return new Location(1,2);
            case 'i':
                return new Location(3,2);
            case 'j':
                return new Location(5,2);
            case 'k':
                return new Location(0,3);
            case 'l':
                return new Location(2,3);
            case 'm':
                return new Location(4,3);
            case 'n':
                return new Location(6,3);
            case 'o':
                return new Location(1,4);
            case 'p':
                return new Location(3,4);
            case 'q':
                return new Location(5,4);
            case 'r':
                return new Location(0,5);
            case 's':
                return new Location(2,5);
            case 't':
                return new Location(4,5);
            case 'u':
                return new Location(6,5);
            case 'v':
                return new Location(1,6);
            case 'w':
                return new Location(3,6);
            case 'x':
                return new Location(5,6);
        }
        //if parameter is an invalid edge, return an empty coordinates
        return new Location(0,0);
    }

    public char toEdge() {
        int x = this.getX();
        int y = this.getY();
        int index = (int) ((7 / 2.0) * y + (x - 1) / 2.0);
        char edge = (char) (index + 'a');
        return edge;
    }

    /**
     * Given a tile position, return the locations that border that tile
     *
     * @param position int representing a tile position (0-8)
     * @return an array of locations that border that tile position.
     */
    public static Location[] getEdgeLocationsForTilePosition(int position) {
        Location[] locations = new Location[4];
        String edges = "";

        switch (position) {
            case 0:
                edges = "aehd";
                break;
            case 1:
                edges = "bfie";
                break;
            case 2:
                edges = "cgjf";
                break;
            case 3:
                edges = "hlok";
                break;
            case 4:
                edges = "impl";
                break;
            case 5:
                edges = "jnqm";
                break;
            case 6:
                edges = "osvr";
                break;
            case 7:
                edges = "ptws";
                break;
            case 8:
                edges = "quxt";
                break;
        }
        for (int i = 0; i < edges.length(); i++) {
            locations[i] = fromEdge(edges.charAt(i));
        }
        return locations;
    }

    @Override
    public String toString() {
        return ("Location x: " + this.getX() + ", y: " + this.getY());
    }

    /**
     * @param loc another Location object
     * @return true if the two Locations are the same
     */
    public boolean isEqual(Location loc) {
        if(this.x == loc.x && this.y == loc.y){
            return true;
        }else {
            return false;
        }
    }
}