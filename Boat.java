package comp1110.ass1;

/**
 * This class represents a boat in the Vikings game.
 */
public class Boat {
    public static int NUM_BOATS;

    private final Colour colour;
    private Location location;

    public Boat(Colour colour, Location location) {
        this.colour = colour;
        this.location = location;
    }

    public Colour getColour() {
        return colour;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * @param boatString a two-character string representing the colour and
     *                   location of the boat e.g. Rx
     * @return a Boat instance with the specified colour and placement
     */
    public static Boat fromString(String boatString) {
        char charRep = boatString.charAt(0);
        char edge = boatString.charAt(1);
        Location loc = Location.fromEdge(edge);
        Colour colour = Colour.GREEN;
        switch (charRep) {
            case 'R':
                colour = Colour.RED;
                break;
            case 'Y':
                colour = Colour.YELLOW;
                break;
            case 'B':
                colour = Colour.BLUE;
                break;
            case 'G':
                colour = Colour.GREEN;
                break;
        }
        return new Boat(colour, loc);
    }

    /**
     * @param boardString A string representing the board
     * @return An array of all Boats found on the board
     */
    public static Boat[] fromBoardString(String boardString) {
        String boatString = boardString.substring(18);
        NUM_BOATS = boatString.length() / 2;
        Boat[] boats = new Boat[NUM_BOATS];
        for (int i = 0; i < NUM_BOATS; i++) {
            boats[i] = fromString(boatString.substring(i * 2, i * 2 + 2));
        }
        return boats;
    }

    @Override
    public String toString() {
        char edge = this.location.toEdge();
        char colour = this.colour.toChar();
        return "" + colour + edge;
    }
}
