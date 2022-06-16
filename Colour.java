package comp1110.ass1;

/**
 * This enumeration represents a possible colour for a boat in the Vikings game.
 */
public enum Colour {
    RED('R'), GREEN('G'), YELLOW('Y'), BLUE('B');

    private final char characterRep;

    Colour(char characterRep) {
        this.characterRep = characterRep;
    }

    public char toChar() {
        return characterRep;
    }
}
